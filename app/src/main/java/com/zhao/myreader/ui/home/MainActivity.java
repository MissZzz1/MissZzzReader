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


import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.custom.CircleImageView;
import com.zhao.myreader.databinding.ActivityMainBinding;
import com.zhao.myreader.util.SystemBarTintManager;
import com.zhao.myreader.util.TextHelper;




public class MainActivity extends FragmentActivity {



    private MainPrensenter mMainPrensenter;

    private ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setStatusBar(R.color.sys_line);
        mMainPrensenter = new MainPrensenter(this);
        mMainPrensenter.enable();

    }



    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - APPCONST.exitTime > APPCONST.exitConfirmTime) {
            TextHelper.showText("再按一次退出");
            APPCONST.exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }




    public CircleImageView getCivAvatar() {
        return binding.civAvatar;
    }

    public TabLayout getTlTabMenu() {
        return binding.tlTabMenu;
    }

    public ImageView getIvSearch() {
        return binding.ivSearch;
    }

    public ViewPager getVpContent() {
        return binding.vpContent;
    }

    public RelativeLayout getRlCommonTitle() {
        return binding.rlCommonTitle;
    }

    public TextView getTvEditFinish() {
        return binding.tvEditFinish;
    }

    public RelativeLayout getRlEditTitile() {
        return binding.rlEditTitile;
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
