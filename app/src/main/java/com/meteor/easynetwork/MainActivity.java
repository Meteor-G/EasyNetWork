package com.meteor.easynetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.meteor.network.NetCreator;
import com.meteor.network.base.BaseObserver;

import java.util.WeakHashMap;

import rx.Observable;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tv_result);
        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final WeakHashMap map = new WeakHashMap();
                map.put("name", "12346");
                NetApi.getInstance().post("TestHandler", map, new BaseObserver() {
                    @Override
                    public void onSuccess(String data) {
                        Log.e("gll", data);
                        tvResult.setText(data);

                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("gll", msg);
                        tvResult.setText(msg + "");
                    }
                });

            }
        });
    }
}
