package com.zhao.myreader.util;

import android.app.Activity;
import android.widget.Toast;

import com.zhao.myreader.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 防界面劫持提示
 * Created by zhao on 2017/7/7.
 */

/**
 * 防界面劫持工具
 * Created by zhao on 2017/7/12.
 */
public class Anti_hijackingUtils {

//    private int id = 12345678;//记录定时ID

    private static boolean home;
    private static boolean back;


    /**
     * 用于执行定时任务
     */
    private Timer timer = null;

    /**
     * 用于保存当前任务
     */
    private List<MyTimerTask> tasks = null;

    /**
     * 唯一实例
     */
    private static Anti_hijackingUtils anti_hijackingUtils;

    private Anti_hijackingUtils() {
        // 初始化
        tasks = new ArrayList<MyTimerTask>();
        timer = new Timer();
    }

    /**
     * 获取唯一实例
     *
     * @return 唯一实例
     */
    public static Anti_hijackingUtils getinstance() {
        if (anti_hijackingUtils == null) {
            anti_hijackingUtils = new Anti_hijackingUtils();
        }
        return anti_hijackingUtils;
    }

    /**
     * 在activity的onPause()方法中调用
     *
     * @param activity
     */
    public void onPause(final Activity activity) {
        MyTimerTask task = new MyTimerTask(activity);
        tasks.add(task);
        timer.schedule(task, 2000);
//       AlarmHelper.addAlarm(activity,2000,id);
    }

    /**
     * 在activity的onResume()方法中调用
     */
    public void onResume(final Activity activity) {
        if (tasks.size() > 0) {
            tasks.get(tasks.size() - 1).setCanRun(false);
            tasks.remove(tasks.size() - 1);
        }
//        AlarmHelper.removeAlarm(activity,id);
    }

    /**
     * 自定义TimerTask类
     */
    class MyTimerTask extends TimerTask {
        /**
         * 任务是否有效
         */
        private boolean canRun = true;
        private Activity activity;

        private void setCanRun(boolean canRun) {
            this.canRun = canRun;
        }

        public MyTimerTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (canRun) {
                        // 程序退到后台，进行风险警告
                        if (home || back){
                            Toast.makeText(activity, activity.getString(R.string.anti_hijacking_tips_home), Toast.LENGTH_LONG).show();

                            tasks.remove(MyTimerTask.this);
                          home = false;
                        }else {
                            Toast.makeText(activity, activity.getString(R.string.anti_hijacking_tips), Toast.LENGTH_LONG).show();
//                            TextHelper.showLongText(MyApplication.getApplication().getString(R.string.anti_hijacking_tips));
                            tasks.remove(MyTimerTask.this);
                        }
                    }
                }
            });
        }

    }


}

