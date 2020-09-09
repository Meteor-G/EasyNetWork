package com.meteor.easynetwork.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.meteor.easynetwork.R;
import com.meteor.easynetwork.api.API;
import com.meteor.easynetwork.api.callback.CommonCallback;
import com.meteor.easynetwork.bean.BaseBean;
import com.meteor.easynetwork.bean.ServerCallbackModel;
import com.meteor.easynetwork.bean.User;
import com.meteor.easynetwork.bean.UserForLogin;
import com.meteor.easynetwork.bean.WanBean;
import com.meteor.network.callback.HttpCallback;

import java.util.List;


/**
 * Created by lipingfa on 2017/6/16.
 */
public class BaseRequestFragment extends Fragment implements View.OnClickListener {
    protected View rootView;
    protected Button btnBaseRequestGet;
    protected TextView tvBaseRequestGet;
    protected EditText etBaseRequestPostName;
    protected EditText etBaseRequestPostPassword;
    protected Button btnBaseRequestPost;
    protected TextView tvBaseRequestPost;
    protected Button btnBaseRequestPostBody;


    private static final String TAG = "BaseRequestFragment";

    public static BaseRequestFragment newInstance() {

        Bundle args = new Bundle();

        BaseRequestFragment fragment = new BaseRequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.base_request_fra, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        btnBaseRequestGet = (Button) rootView.findViewById(R.id.btn_base_request_get);
        btnBaseRequestGet.setOnClickListener(BaseRequestFragment.this);
        tvBaseRequestGet = (TextView) rootView.findViewById(R.id.tv_base_request_get);
        etBaseRequestPostName = (EditText) rootView.findViewById(R.id.et_base_request_post_name);
        etBaseRequestPostPassword = (EditText) rootView.findViewById(R.id.et_base_request_post_password);
        btnBaseRequestPost = (Button) rootView.findViewById(R.id.btn_base_request_post_map);
        btnBaseRequestPost.setOnClickListener(BaseRequestFragment.this);
        tvBaseRequestPost = (TextView) rootView.findViewById(R.id.tv_base_request_post);
        btnBaseRequestPostBody = (Button) rootView.findViewById(R.id.btn_base_request_post_body);
        btnBaseRequestPostBody.setOnClickListener(BaseRequestFragment.this);
        rootView.findViewById(R.id.btn_base_request_https).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_base_request_https:
                testHttps();
                break;
            case R.id.btn_base_request_get:
                testGet();
                break;
            case R.id.btn_base_request_post_map:
                testPostByMap();
                break;
            case R.id.btn_base_request_post_body:
                testPostByMapByBody();
                break;
        }
    }

    private void testHttps() {
//        API.testHttps(new HttpCallback() {
//            @Override
//            public void onResolve(Object data) {
//                Log.e(TAG, "testHttps:" + data.toString());
//            }
//
//            @Override
//            public void onFailed(int errCode, String msg) {
//                Log.e(TAG, "testHttps:" + msg);
//            }
//        });
    }

    /**
     * get请求
     */
    private void testGet() {
        //解析json
//        API.testGet(new HttpCallback<ServerCallbackModel<User>>() {
//
//            @Override
//            public void onResolve(ServerCallbackModel<User> data) {
//                Log.e(TAG, "GET:" + data.toString());
//            }
//
//            @Override
//            public void onFailed(int errCode, String msg) {
//                Log.e(TAG, "GET:" + errCode + msg);
//            }
//        });
        //不解析json
        API.testGet(new HttpCallback<String>() {

            @Override
            public void onResolve(String data) {
                tvBaseRequestGet.setText(data);
            }

            @Override
            public void onFailed(int errCode, String msg) {
                tvBaseRequestGet.setText(msg);
            }

        });
    }

    /**
     * post请求
     */
    private void testPostByMap() {
        String name = etBaseRequestPostName.getText().toString().trim();
        String password = etBaseRequestPostPassword.getText().toString().trim();
        //解析json
//        API.testPost(name, password, new HttpCallback<ServerCallbackModel<String>>() {
//
//
//            @Override
//            public void onResolve(ServerCallbackModel<String> data) {
//                showToast("accessToken:" + data.getData());
//            }
//
//            @Override
//            public void onFailed(int errCode, String msg) {
//                Log.e("gllGet", errCode + msg);
//            }
//        });
        //不解析json
        API.testPost(name, password, new CommonCallback<String>() {

            @Override
            public void onSuccess(String data) {
                tvBaseRequestPost.setText(data);
            }

            @Override
            public void onFailure(int error_code, String error_message) {

            }
        });
    }

    private void testPostByMapByBody() {
        String name = etBaseRequestPostName.getText().toString().trim();
        String password = etBaseRequestPostPassword.getText().toString().trim();
        UserForLogin userForLogin = new UserForLogin(name, password);
        //解析json
//        API.testPost(userForLogin, new HttpCallback<ServerCallbackModel<String>>() {
//
////            @Override
////            public void onResolve(BaseBean<List<WanBean>> data) {
////                List<WanBean> wanBeans = data.getData();
////                for (WanBean wanBean : wanBeans) {
////                    Log.e("gllGet", wanBean.toString());
////                }
////
////            }
//
//            @Override
//            public void onResolve(ServerCallbackModel<String> data) {
//                showToast("accessToken:" + data.getData());
//            }
//
//            @Override
//            public void onFailed(int errCode, String msg) {
//                Log.e("gllGet", errCode + msg);
//            }
//        });
        //不解析json
        API.testPost(userForLogin, new CommonCallback<String>() {

            @Override
            public void onSuccess(String data) {
                tvBaseRequestPost.setText(data);
            }

            @Override
            public void onFailure(int error_code, String error_message) {

            }
        });
    }

    private void showToast(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }
}

