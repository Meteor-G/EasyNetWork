package com.meteor.easynetwork;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.meteor.easynetwork.ui.adapter.MainVpAdapter;
import com.meteor.easynetwork.ui.fragment.BaseRequestFragment;
import com.meteor.easynetwork.ui.fragment.FileDownloadFragment;
import com.meteor.easynetwork.ui.fragment.FileUploadFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected TabLayout tlMain;
    protected ViewPager vpMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tlMain = (TabLayout) findViewById(R.id.tl_main);
        vpMain = (ViewPager) findViewById(R.id.vp_main);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(BaseRequestFragment.newInstance());
        fragmentList.add(FileUploadFragment.newInstance());
        fragmentList.add(FileDownloadFragment.newInstance());
        String[] titles = {"基本请求", "文件上传", "文件下载"};
        MainVpAdapter adapter = new MainVpAdapter(getSupportFragmentManager(), fragmentList, titles);
        vpMain.setAdapter(adapter);
//        vpReportIncomeSetting.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlReportIncomeSetting));

        tlMain.setupWithViewPager(vpMain);
        tlMain.setTabMode(TabLayout.MODE_SCROLLABLE);
        tlMain.setTabTextColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorAccent));
        tlMain.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
    }

}
