package com.zhao.myreader.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.zhao.myreader.application.ActivityManage;
import com.zhao.myreader.util.Anti_hijackingUtils;
import com.zhao.myreader.util.SystemBarTintManager;


/**
 * Created by zhao on 2016/4/16.
 */
public class BaseActivity extends Activity {

    public static int width = 0;
    public static int height = 0;
    public static boolean home;
    public static boolean back;
    private boolean catchHomeKey = false;
    private boolean disallowAntiHijacking;//暂停防界面劫持

    private  static boolean closeAntiHijacking;//关闭防界面劫持


    private InputMethodManager mInputMethodManager; //输入管理器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //将每一个Activity都加入activity管理器
        ActivityManage.addActivity(this);
        Log.d("ActivityName: ",getLocalClassName());
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕宽高
        if(height == 0 || height == 0){
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            width = dm.widthPixels;
            height = dm.heightPixels;
        }

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static void setCloseAntiHijacking(boolean closeAntiHijacking) {
        BaseActivity.closeAntiHijacking = closeAntiHijacking;
    }

    @Override
    protected void onDestroy() {
        ActivityManage.removeActivity(this);
        super.onDestroy();

    }


    @Override
    protected void onPause() {
        if (!disallowAntiHijacking && !closeAntiHijacking) {
            Anti_hijackingUtils.getinstance().onPause(this);//防界面劫持提示任务
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (!closeAntiHijacking) {
            Anti_hijackingUtils.getinstance().onResume(this);//注销防界面劫持提示任务
        }
        BaseActivity.home = false;
        BaseActivity.back = false;
        disallowAntiHijacking = false;
        super.onResume();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            back = true;//以便于判断是否按返回键触发界面劫持提示
        }
        return super.onKeyDown(keyCode, event);
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

    public void setDisallowAntiHijacking(boolean disallowAntiHijacking) {
        this.disallowAntiHijacking = disallowAntiHijacking;
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

    public InputMethodManager getmInputMethodManager() {
        return mInputMethodManager;
    }






}
