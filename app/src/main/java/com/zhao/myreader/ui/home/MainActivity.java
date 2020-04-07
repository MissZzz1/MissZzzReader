package com.zhao.myreader.ui.home;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zhao.myreader.R;

import com.zhao.myreader.base.BaseActivity;
import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.custom.CircleImageView;
import com.zhao.myreader.util.SystemBarTintManager;
import com.zhao.myreader.util.TextHelper;

import org.jsoup.Connection;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends FragmentActivity {


    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.tl_tab_menu)
    TabLayout tlTabMenu;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_common_title)
    RelativeLayout rlCommonTitle;
    @BindView(R.id.tv_edit_finish)
    TextView tvEditFinish;
    @BindView(R.id.rl_edit_titile)
    RelativeLayout rlEditTitile;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    private MainPrensenter mMainPrensenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setStatusBar(R.color.sys_line);
        mMainPrensenter = new MainPrensenter(this);
        mMainPrensenter.start();

    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - APPCONST.exitTime > APPCONST.exitConfirmTime) {
            TextHelper.showText("再按一次退出");
            APPCONST.exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MyApplication.checkVersionByServer(this);
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

    public RelativeLayout getRlCommonTitle() {
        return rlCommonTitle;
    }

    public TextView getTvEditFinish() {
        return tvEditFinish;
    }

    public RelativeLayout getRlEditTitile() {
        return rlEditTitile;
    }


    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    /**
     * 设置状态栏颜色
     * @param colorId
     */
    public void setStatusBar(int colorId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(colorId);//通知栏所需颜色ID
        }
    }

}
