package com.meteor.network;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.meteor.network.callback.FileResponseResult;
import com.meteor.network.callback.HttpCallback;
import com.meteor.network.download.DownloadInfo;
import com.meteor.network.download.DownloadManager;
import com.meteor.network.interceptor.BaseInterceptor;
import com.meteor.network.interceptor.HeaderInterceptor;
import com.meteor.network.listener.DownloadFileListener;
import com.meteor.network.request.CommonRequest;
import com.meteor.network.subscriber.CommonResultSubscriber;
import com.meteor.network.upload.UploadFileRequestBody;
import com.meteor.network.upload.UploadSubscriber;
import com.meteor.network.utils.HttpsUtils;
import com.meteor.network.utils.Utils;

import java.io.File;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author GLL
 * @data: 2020-08-07 18:50
 * @description
 */
public final class HttpManager {

    private static Context mContext;
    private OkHttpClient.Builder okHttpClientBuilder;                 //okhttp请求的客户端
    private Retrofit.Builder retrofitBuilder;                         //Retrofit请求Builder
    private volatile static HttpManager singleton = null;
    private CommonRequest mDefultCommonRequest;

    private HttpManager() {
        okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.hostnameVerifier(new DefaultHostnameVerifier());
        retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());//增加RxJava2CallAdapterFactory
        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create());//坑   addConverterFactory有先后顺序之分,如果想获取到String,这个必须写前边
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create()); // 添加Gson转换器
    }

    public static void init(Context context, HttpSlot httpSlot) {
        Utils.checkNotNull(context, "mContext == null");
        Utils.checkNotNull(httpSlot, "httpSlot == null");
        HttpManager.mContext = context;
        getInstance().setDefultClient(httpSlot);
    }

    /**
     * 获取全局上下文
     */
    public Context getContext() {
        testInitialize();
        return mContext;
    }

    private void testInitialize() {
        if (mContext == null)
            throw new ExceptionInInitializerError("请先在全局Application中调用 EasyHttp.init() 初始化！");
    }

    public static HttpManager getInstance() {
        if (singleton == null) {
            synchronized (HttpManager.class) {
                if (singleton == null) {
                    singleton = new HttpManager();
                }
            }
        }
        return singleton;
    }


    private void setDefultClient(HttpSlot httpSlot) {
        okHttpClientBuilder = generateOkClient(httpSlot);
        retrofitBuilder = generateRetrofit(httpSlot);
        mDefultCommonRequest = getDefultApi();
    }

    /**
     * 此类是用于主机名验证的基接口。 在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，
     * 则验证机制可以回调此接口的实现程序来确定是否应该允许此连接。策略可以是基于证书的或依赖于其他验证方案。
     * 当验证 URL 主机名使用的默认规则失败时使用这些回调。如果主机名是可接受的，则返回 true
     */
    public class DefaultHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public CommonRequest getDefultCommonApi() {
        return getInstance().mDefultCommonRequest;
    }

    /**
     * 对外暴露 OkHttpClient,方便自定义
     */
    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return getInstance().okHttpClientBuilder;
    }

    /**
     * 对外暴露 Retrofit,方便自定义
     */
    public Retrofit.Builder getRetrofitBuilder() {
        return getInstance().retrofitBuilder;
    }

    public OkHttpClient getOkHttpClient() {
        return getInstance().okHttpClientBuilder.build();
    }

    /**
     * 根据当前的请求参数，生成对应的OkClient
     */
    private OkHttpClient.Builder generateOkClient(HttpSlot httpSlot) {
        long readTimeOut = httpSlot.getReadTimeOut();
        long writeTimeOut = httpSlot.getReadTimeOut();
        long connectTimeout = httpSlot.getConnectTimeout();
        HttpsUtils.SSLParams sslParams = httpSlot.getSslParams();
        HostnameVerifier hostnameVerifier = httpSlot.getHostnameVerifier();
        Proxy proxy = httpSlot.getProxy();
        HashMap<String, String> headers = httpSlot.getCommonHeaders();
        HashMap<String, String> commonParams = httpSlot.getCommonParams();
        List<Interceptor> interceptors = httpSlot.getInterceptors();
        List<Interceptor> networkInterceptors = httpSlot.getNetworkInterceptors();
        if (readTimeOut <= 0 && writeTimeOut <= 0 && connectTimeout <= 0 && sslParams == null
                && hostnameVerifier == null && proxy == null && headers.isEmpty()) {
            return getOkHttpClientBuilder();
        } else {
            final OkHttpClient.Builder newClientBuilder = getOkHttpClient().newBuilder();
            if (readTimeOut > 0)
                newClientBuilder.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
            if (writeTimeOut > 0)
                newClientBuilder.writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS);
            if (connectTimeout > 0)
                newClientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
            if (hostnameVerifier != null) newClientBuilder.hostnameVerifier(hostnameVerifier);
            if (sslParams != null)
                newClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
            if (proxy != null) newClientBuilder.proxy(proxy);

            //添加头  头添加放在最前面方便其他拦截器可能会用到
            newClientBuilder.addInterceptor(new HeaderInterceptor(headers));
            newClientBuilder.addInterceptor(new BaseInterceptor(commonParams));
            for (Interceptor interceptor : interceptors) {
                newClientBuilder.addInterceptor(interceptor);
            }
            if (networkInterceptors.size() > 0) {
                for (Interceptor interceptor : networkInterceptors) {
                    newClientBuilder.addNetworkInterceptor(interceptor);
                }
            }
            return newClientBuilder;
        }
    }

    /**
     * 根据当前的请求参数，生成对应的Retrofit
     */
    private Retrofit.Builder generateRetrofit(HttpSlot httpSlot) {
        List<CallAdapter.Factory> adapterFactories = httpSlot.getCallAdapterFactory();
        List<Converter.Factory> converterFactories = httpSlot.getConverterFactories();
        String baseUrl = httpSlot.getBaseUrl();
        if (converterFactories.isEmpty() && adapterFactories.isEmpty() && TextUtils.isEmpty(baseUrl)) {
            return getRetrofitBuilder();
        } else {
            final Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
            if (!TextUtils.isEmpty(baseUrl)) retrofitBuilder.baseUrl(baseUrl);
            if (!converterFactories.isEmpty()) {
                for (Converter.Factory converterFactory : converterFactories) {
                    retrofitBuilder.addConverterFactory(converterFactory);
                }
            } else {
                //获取全局的对象重新设置
                Retrofit.Builder newBuilder = getRetrofitBuilder();
                if (!TextUtils.isEmpty(baseUrl)) {
                    newBuilder.baseUrl(baseUrl);
                }
                List<Converter.Factory> listConverterFactory = newBuilder.build().converterFactories();
                for (Converter.Factory factory : listConverterFactory) {
                    retrofitBuilder.addConverterFactory(factory);
                }
            }
            if (!adapterFactories.isEmpty()) {
                for (CallAdapter.Factory adapterFactory : adapterFactories) {
                    retrofitBuilder.addCallAdapterFactory(adapterFactory);
                }
            } else {
                //获取全局的对象重新设置
                Retrofit.Builder newBuilder = getRetrofitBuilder();
                List<CallAdapter.Factory> listAdapterFactory = newBuilder.baseUrl(baseUrl).build().callAdapterFactories();
                for (CallAdapter.Factory factory : listAdapterFactory) {
                    retrofitBuilder.addCallAdapterFactory(factory);
                }
            }
            return retrofitBuilder;
        }
    }

    private CommonRequest getApi(HttpSlot httpSlot) {
        OkHttpClient.Builder okHttpClientBuilder;
        Retrofit.Builder retrofitBuilder;
        if (httpSlot == null) {
            return getInstance().mDefultCommonRequest;
//            okHttpClientBuilder = getOkHttpClientBuilder();
//            retrofitBuilder = getRetrofitBuilder();
        } else {
            okHttpClientBuilder = generateOkClient(httpSlot);
            retrofitBuilder = generateRetrofit(httpSlot);
        }
        OkHttpClient okHttpClient = okHttpClientBuilder.build();
        retrofitBuilder.client(okHttpClient);
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(CommonRequest.class);
    }

    private CommonRequest getDefultApi() {
        OkHttpClient.Builder okHttpClientBuilder = getOkHttpClientBuilder();
        Retrofit.Builder retrofitBuilder = getRetrofitBuilder();
        OkHttpClient okHttpClient = okHttpClientBuilder.build();
        retrofitBuilder.client(okHttpClient);
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(CommonRequest.class);
    }

    //----------------------------------------get-------------------------------------------


    /**
     * @param url
     * @param parameters
     * @param callback
     */

    public void get(String url, Map<String, Object> parameters, HttpCallback callback) {
        get(null, url, parameters, callback);
    }

    public void get(HttpSlot httpSlot, String url, Map<String, Object> parameters, HttpCallback callback) {
        if (parameters == null || parameters.size() == 0) {
            getApi(httpSlot)
                    .doGet(url)
                    .compose(schedulerTransformer)
                    .subscribe(new CommonResultSubscriber(callback));
        } else {
            getApi(httpSlot)
                    .doGet(url, parameters)
                    .compose(schedulerTransformer)
                    .subscribe(new CommonResultSubscriber(callback));
        }
    }


    /**
     * RxJava形式
     *
     * @param url
     * @param parameters
     * @return
     */
    public Observable get(String url, Map<String, Object> parameters) {
        return get(null, url, parameters);
    }

    public Observable get(HttpSlot httpSlot, String url, Map<String, Object> parameters) {

        if (parameters == null || parameters.size() == 0) {
            return getApi(httpSlot)
                    .doGet(url)
                    .compose(schedulerTransformer);
        } else {
            return getApi(httpSlot)
                    .doGet(url, parameters)
                    .compose(schedulerTransformer);
        }
    }

    /**
     * 全地址
     *
     * @param fullUrl
     * @param parameters
     * @param callback
     */
    public void getFullPath(String fullUrl, Map<String, Object> parameters, HttpCallback callback) {
        getFullPath(null, fullUrl, parameters, callback);
    }

    public void getFullPath(HttpSlot httpSlot, String fullUrl, Map<String, Object> parameters, HttpCallback callback) {
        if (parameters == null || parameters.size() == 0) {
            getApi(httpSlot)
                    .doGetFullPath(fullUrl)
                    .compose(schedulerTransformer)
                    .subscribe(new CommonResultSubscriber(callback));
        } else {
            getApi(httpSlot)
                    .doGetFullPath(fullUrl, parameters)
                    .compose(schedulerTransformer)
                    .subscribe(new CommonResultSubscriber(callback));
        }
    }


    //----------------------------------------post-------------------------------------------

    /**
     * 注意，此方法传到服务器的是一个json串
     *
     * @param url
     * @param t
     * @param callback
     * @param <T>
     */
    public <T> void postByBody(String url, T t, HttpCallback callback) {
        postByBody(null, url, t, callback);
    }

    public <T> void postByBody(HttpSlot httpSlot, String url, T t, HttpCallback callback) {
        String parameters = new Gson().toJson(t);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parameters);
        getApi(httpSlot)
                .doPost(url, body)
                .compose(schedulerTransformer)
                .subscribe(new CommonResultSubscriber(callback));
    }

    /**
     * RxJava形式
     *
     * @param url
     * @param parameters
     * @return
     */
    public Observable post(String url, Map<String, Object> parameters) {
        return post(null, url, parameters);
    }

    public Observable post(HttpSlot httpSlot, String url, Map<String, Object> parameters) {
        return getApi(httpSlot)
                .doPost(url, parameters)
                .compose(schedulerTransformer);
    }

    public void post(String url, Map<String, Object> parameters, HttpCallback callback) {
        post(null, url, parameters, callback);
    }

    public void post(HttpSlot httpSlot, String url, Map<String, Object> parameters, HttpCallback callback) {
        getApi(httpSlot)
                .doPost(url, parameters)
                .compose(schedulerTransformer)
                .subscribe(new CommonResultSubscriber(callback));
    }

    public void postFullPath(String fullUrl, Map<String, Object> parameters, HttpCallback callback) {
        postFullPath(null, fullUrl, parameters, callback);
    }

    public void postFullPath(HttpSlot httpSlot, String fullUrl, Map<String, Object> parameters, HttpCallback callback) {
        getApi(httpSlot)
                .doPostFullPath(fullUrl, parameters)
                .compose(schedulerTransformer)
                .subscribe(new CommonResultSubscriber(callback));
    }


    //----------------------------------------download-----------------------------------------

    public void download(String url, String savePath, DownloadFileListener listener) {
        DownloadInfo info = new DownloadInfo(url, savePath);
        info.setState(DownloadInfo.START);
        info.setListener(listener);
        DownloadManager downloadManager = DownloadManager.getInstance();

        downloadManager.startDown(info);
    }

    public void download(DownloadInfo info) {
        DownloadManager downloadManager = DownloadManager.getInstance();
        downloadManager.startDown(info);
    }

    //----------------------------------------upload-------------------------------------------
    public void uploadFile(String url, String filePath, String fileDes, boolean isFullUrl,
                           final FileResponseResult callback) {
        uploadFile(null, url, filePath, fileDes, isFullUrl, callback);
    }

    public void uploadFile(HttpSlot httpSlot, String url, String filePath, String fileDes, boolean isFullUrl,
                           final FileResponseResult callback) {
        final File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data; charset=utf-8"), fileDes);
        RequestBody requestBody = UploadFileRequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        if (isFullUrl) {
            getApi(httpSlot).uploadFileFullPath(url, description, part)
                    .compose(schedulerTransformer)
                    .subscribe(new UploadSubscriber(callback));
        } else {
            getApi(httpSlot).uploadFile(url, description, part)
                    .compose(schedulerTransformer)
                    .subscribe(new UploadSubscriber(callback));
        }
    }

    public void uploadFiles(String url, List<String> filePathList, boolean isFullUrl,
                            final FileResponseResult callback) {
        uploadFiles(null, url, filePathList, isFullUrl, callback);
    }

    public void uploadFiles(HttpSlot httpSlot, String url, List<String> filePathList, boolean isFullUrl,
                            final FileResponseResult callback) {
        if (filePathList == null || filePathList.size() == 0) {
            return;
        }
        List<File> fileList = new ArrayList<>();
        long totalSize = 0;
        for (String filePath : filePathList) {
            File file = new File(filePath);
            if (!file.exists()) {
                continue;
            }
            totalSize += file.length();
            fileList.add(file);
        }

        HashMap<String, RequestBody> params = new HashMap<>();
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            RequestBody body =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            params.put("file[]\"; filename=\"" + file.getName(), body);
        }
        if (isFullUrl) {
            getApi(httpSlot).uploadFilesFullPath(url, params)
                    .compose(schedulerTransformer)
                    .subscribe(new UploadSubscriber(callback));
        } else {
            getApi(httpSlot).uploadFiles(url, params)
                    .compose(schedulerTransformer)
                    .subscribe(new UploadSubscriber(callback));
        }
    }


    private Observable.Transformer schedulerTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object o) {
            return ((Observable) o)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };


}
