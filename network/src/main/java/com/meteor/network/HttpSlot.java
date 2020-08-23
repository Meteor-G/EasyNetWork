package com.meteor.network;

import com.meteor.network.utils.HttpsUtils;
import com.meteor.network.utils.Utils;

import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;

import okhttp3.Interceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;

/**
 * @author GLL
 * @data: 2020-08-10 14:07
 * @description
 */
public class HttpSlot {
    private String mBaseUrl;
    private HashMap<String, String> mCommonParams = new HashMap<>();
    private HashMap<String, String> mCommonHeaders = new HashMap<>();
    private HostnameVerifier hostnameVerifier;
    private HttpsUtils.SSLParams sslParams;
    private long readTimeOut;
    private long writeTimeout;
    private long connectTimeout;
    private List<Interceptor> interceptors;
    private Proxy proxy;
    private List<Converter.Factory> converterFactories;
    private List<CallAdapter.Factory> callAdapterFactory;
    private List<Interceptor> networkInterceptors;

    private HttpSlot() {

    }

    public List<Interceptor> getNetworkInterceptors() {
        return networkInterceptors;
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }


    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public HttpsUtils.SSLParams getSslParams() {
        return sslParams;
    }

    public long getReadTimeOut() {
        return readTimeOut;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public List<Converter.Factory> getConverterFactories() {
        return converterFactories;
    }

    public List<CallAdapter.Factory> getCallAdapterFactory() {
        return callAdapterFactory;
    }


    public void setBaseUrl(String mBaseUrl) {
        this.mBaseUrl = mBaseUrl;
    }

    public HashMap<String, String> getCommonParams() {
        return mCommonParams;
    }

    public void setCommonParams(HashMap<String, String> mCommonParams) {
        this.mCommonParams = mCommonParams;
    }

    public HashMap<String, String> getCommonHeaders() {
        return mCommonHeaders;
    }

    public void setCommonHeaders(HashMap<String, String> mCommonHeaders) {
        this.mCommonHeaders = mCommonHeaders;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    public void setSslParams(HttpsUtils.SSLParams sslParams) {
        this.sslParams = sslParams;
    }

    public void setReadTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public void setConverterFactories(List<Converter.Factory> converterFactories) {
        this.converterFactories = converterFactories;
    }

    public void setCallAdapterFactory(List<CallAdapter.Factory> callAdapterFactory) {
        this.callAdapterFactory = callAdapterFactory;
    }

    public void setNetworkInterceptors(List<Interceptor> networkInterceptors) {
        this.networkInterceptors = networkInterceptors;
    }

    public static class Builder {
        private String mBaseUrl;
        private HashMap<String, String> mCommonParams = new HashMap<>();
        private HashMap<String, String> mCommonHeaders = new HashMap<>();
        private HostnameVerifier hostnameVerifier;
        private HttpsUtils.SSLParams sslParams;
        private long readTimeOut = 10 * 1000;
        private long writeTimeout = 10 * 1000;
        private long connectTimeout = 10 * 1000;
        private List<Interceptor> interceptors = new ArrayList<>();
        private Proxy proxy;
        private List<Converter.Factory> converterFactories = new ArrayList<>();
        private List<CallAdapter.Factory> callAdapterFactory = new ArrayList<>();
        private List<Interceptor> networkInterceptors = new ArrayList<>();

        public Builder() {
        }

        public HttpSlot.Builder setBaseUrl(String mBaseUrl) {
            this.mBaseUrl = mBaseUrl;
            return this;
        }

        /**
         * https的全局访问规则
         */
        public HttpSlot.Builder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        /**
         * https的全局自签名证书
         */
        public HttpSlot.Builder setCertificates(InputStream... certificates) {
            setCertificates(null, null, certificates);
            return this;
        }

        /**
         * https双向认证证书
         */
        public HttpSlot.Builder setCertificates(InputStream bksFile, String password, InputStream... certificates) {
            this.sslParams = HttpsUtils.getSslSocketFactory(certificates, bksFile, password);
            return this;
        }

        /**
         * 添加全局网络拦截器
         */
        public HttpSlot.Builder addNetworkInterceptor(Interceptor interceptor) {
            networkInterceptors.add(Utils.checkNotNull(interceptor, "interceptor == null"));
            return this;
        }

        /**
         * 全局读取超时时间
         */
        public HttpSlot.Builder setReadTimeOut(long readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        /**
         * 全局写入超时时间
         */
        public HttpSlot.Builder setWriteTimeOut(long writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        /**
         * 全局连接超时时间
         */
        public HttpSlot.Builder setConnectTimeout(long connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }


        /**
         * 添加全局公共请求参数
         */
        public HttpSlot.Builder addCommonParams(HashMap<String, String> commonParams) {
            if (commonParams != null && commonParams.size() != 0) {
                mCommonParams.putAll(commonParams);
            }
            return this;
        }

        /**
         * 获取全局公共请求参数
         */
        public HashMap<String, String> getCommonParams() {
            return mCommonParams;
        }

        /**
         * 获取全局公共请求头
         */
        public HashMap<String, String> getCommonHeaders() {
            return mCommonHeaders;
        }

        /**
         * 添加全局公共请求参数
         */
        public HttpSlot.Builder addCommonHeaders(HashMap<String, String> commonHeaders) {
            if (commonHeaders != null && commonHeaders.size() != 0) {
                mCommonHeaders.putAll(commonHeaders);
            }
            return this;
        }

        /**
         * 添加全局拦截器
         */
        public HttpSlot.Builder addInterceptor(Interceptor interceptor) {
            interceptors.add(Utils.checkNotNull(interceptor, "interceptor == null"));
            return this;
        }

        /**
         * 全局设置代理
         */
        public HttpSlot.Builder setOkproxy(Proxy proxy) {
            Utils.checkNotNull(proxy, "proxy == null");
            this.proxy = proxy;
            return this;
        }

        /**
         * 全局设置Converter.Factory,默认GsonConverterFactory.create()
         */
        public HttpSlot.Builder addConverterFactory(Converter.Factory factory) {
            converterFactories.add(Utils.checkNotNull(factory, "factory == null"));
            return this;
        }

        /**
         * 全局设置CallAdapter.Factory,默认RxJavaCallAdapterFactory.create()
         */
        public HttpSlot.Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            callAdapterFactory.add(Utils.checkNotNull(factory, "factory == null"));
            return this;
        }

        public HttpSlot build() {
            HttpSlot httpSlot = new HttpSlot();
            httpSlot.setBaseUrl(mBaseUrl);
            httpSlot.setCallAdapterFactory(callAdapterFactory);
            httpSlot.setConverterFactories(converterFactories);
            httpSlot.setCommonHeaders(mCommonHeaders);
            httpSlot.setCommonParams(mCommonParams);
            httpSlot.setConnectTimeout(connectTimeout);
            httpSlot.setReadTimeOut(readTimeOut);
            httpSlot.setWriteTimeout(writeTimeout);
            httpSlot.setHostnameVerifier(hostnameVerifier);
            httpSlot.setInterceptors(interceptors);
            httpSlot.setNetworkInterceptors(networkInterceptors);
            httpSlot.setProxy(proxy);
            httpSlot.setSslParams(sslParams);
            return httpSlot;
        }
    }


    @Override
    public String toString() {
        return "HttpSlot{" +
                "mBaseUrl='" + mBaseUrl + '\'' +
                ", mCommonParams=" + mCommonParams +
                ", mCommonHeaders=" + mCommonHeaders +
                ", hostnameVerifier=" + hostnameVerifier +
                ", sslParams=" + sslParams +
                ", readTimeOut=" + readTimeOut +
                ", writeTimeout=" + writeTimeout +
                ", connectTimeout=" + connectTimeout +
                ", interceptors=" + interceptors +
                ", proxy=" + proxy +
                ", converterFactories=" + converterFactories +
                ", callAdapterFactory=" + callAdapterFactory +
                ", networkInterceptors=" + networkInterceptors +
                '}';
    }
}
