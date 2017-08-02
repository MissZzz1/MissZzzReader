package com.zhao.myreader.ui.home;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.zhao.myreader.R;
import com.zhao.myreader.application.MyApplication;
import com.zhao.myreader.base.BaseActivity;
import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.custom.CircleImageView;
import com.zhao.myreader.util.TextHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity {

    @InjectView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @InjectView(R.id.tl_tab_menu)
    TabLayout tlTabMenu;
    @InjectView(R.id.iv_search)
    ImageView ivSearch;
    @InjectView(R.id.vp_content)
    ViewPager vpContent;

    private MainPrensenter mMainPrensenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setStatusBar(R.color.sys_line);
        mMainPrensenter = new MainPrensenter(this);
        mMainPrensenter.start();
    }

    @Override
    public void onBackPressed(){

        if(System.currentTimeMillis()  - APPCONST.exitTime > APPCONST.exitConfirmTime){
            TextHelper.showText("再按一次退出");
            APPCONST.exitTime = System.currentTimeMillis();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.checkVersion(this);
    }

    public CircleImageView getCivAvatar() {
        return civAvatar;
    }

    public TabLayout getTlTabMenu() {
        return tlTabMenu;
    }

    public ImageView getIvSearch() {
        return ivSearch;
    }

    public ViewPager getVpContent() {
        return vpContent;
    }

}
