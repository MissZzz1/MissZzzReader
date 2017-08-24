package com.zhao.myreader.creator;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhao.myreader.R;
import com.zhao.myreader.application.MyApplication;
import com.zhao.myreader.application.SysManager;
import com.zhao.myreader.entity.Setting;
import com.zhao.myreader.enums.Language;
import com.zhao.myreader.enums.ReadStyle;
import com.zhao.myreader.util.BrightUtil;
import com.zhao.myreader.util.StringHelper;

import butterknife.InjectView;

/**
 * Created by zhao on 2017/1/11.
 */

public class DialogCreator {

    private static ImageView ivLastSelectd = null;


    /**
     * 阅读详细设置对话框
     * @param context
     * @param setting
     * @param onReadStyleChangeListener
     * @param reduceSizeListener
     * @param increaseSizeListener
     * @param languageChangeListener
     * @param onFontClickListener
     * @return
     */
    public static Dialog createReadDetailSetting(final Context context, final Setting setting,
                                                 final OnReadStyleChangeListener onReadStyleChangeListener,
                                                 final View.OnClickListener reduceSizeListener,
                                                 final View.OnClickListener increaseSizeListener,
                                                 final View.OnClickListener languageChangeListener,
                                                 final View.OnClickListener onFontClickListener,
                                                 View.OnClickListener autoScrollListener) {
        final Dialog dialog = new Dialog(context, R.style.jmui_default_dialog_style);
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_read_setting_detail, null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        //触摸外部关闭
        view.findViewById(R.id.ll_bottom_view).setOnClickListener(null);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dialog.dismiss();
                return false;
            }
        });
        //设置全屏
        Window window = dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //阅读背景风格
        final ImageView ivCommonStyle = (ImageView) view.findViewById(R.id.iv_common_style);
        final ImageView ivLeatherStyle = (ImageView) view.findViewById(R.id.iv_leather_style);
        final ImageView ivProtectEyeStyle = (ImageView) view.findViewById(R.id.iv_protect_eye_style);
        final ImageView ivBreenStyle = (ImageView) view.findViewById(R.id.iv_breen_style);
        final ImageView ivBlueDeepStyle = (ImageView) view.findViewById(R.id.iv_blue_deep_style);
        switch (setting.getReadStyle()) {
            case common:
                ivCommonStyle.setSelected(true);
                ivLastSelectd = ivCommonStyle;
                break;
            case leather:
                ivLeatherStyle.setSelected(true);
                ivLastSelectd = ivLeatherStyle;
                break;
            case protectedEye:
                ivProtectEyeStyle.setSelected(true);
                ivLastSelectd = ivProtectEyeStyle;
                break;
            case breen:
                ivBreenStyle.setSelected(true);
                ivLastSelectd = ivBreenStyle;
                break;
            case blueDeep:
                ivBlueDeepStyle.setSelected(true);
                ivLastSelectd = ivBlueDeepStyle;
                break;
        }
        ivCommonStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStyle(ivCommonStyle, ReadStyle.common, onReadStyleChangeListener);
            }
        });
        ivLeatherStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStyle(ivLeatherStyle, ReadStyle.leather, onReadStyleChangeListener);
            }
        });
        ivProtectEyeStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStyle(ivProtectEyeStyle, ReadStyle.protectedEye, onReadStyleChangeListener);
            }
        });
        ivBlueDeepStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStyle(ivBlueDeepStyle, ReadStyle.blueDeep, onReadStyleChangeListener);
            }
        });
        ivBreenStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStyle(ivBreenStyle, ReadStyle.breen, onReadStyleChangeListener);
            }
        });

        //字体大小
        TextView tvSizeReduce = (TextView) view.findViewById(R.id.tv_reduce_text_size);
        TextView tvSizeIncrease = (TextView) view.findViewById(R.id.tv_increase_text_size);
        final TextView tvSize = (TextView) view.findViewById(R.id.tv_text_size);
        tvSize.setText(String.valueOf((int) setting.getReadWordSize()));
        tvSizeReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setting.getReadWordSize() > 1) {
                    tvSize.setText(String.valueOf((int) setting.getReadWordSize() - 1));
                    if (reduceSizeListener != null) {
                        reduceSizeListener.onClick(v);
                    }
                }
            }
        });
        tvSizeIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setting.getReadWordSize() < 40) {
                    tvSize.setText(String.valueOf((int) setting.getReadWordSize() + 1));
                    if (increaseSizeListener != null) {
                        increaseSizeListener.onClick(v);
                    }
                }
            }
        });

        //亮度调节
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.sb_brightness_progress);
        final TextView tvBrightFollowSystem = (TextView) view.findViewById(R.id.tv_system_brightness);
        seekBar.setProgress(setting.getBrightProgress());
        tvBrightFollowSystem.setSelected(setting.isBrightFollowSystem());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BrightUtil.setBrightness((Activity) context, BrightUtil.progressToBright(progress));
                tvBrightFollowSystem.setSelected(false);
                setting.setBrightProgress(progress);
                setting.setBrightFollowSystem(false);
                SysManager.saveSetting(setting);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //亮度跟随系统
        tvBrightFollowSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBrightFollowSystem.setSelected(!tvBrightFollowSystem.isSelected());
                if (tvBrightFollowSystem.isSelected()) {
                    BrightUtil.followSystemBright((Activity) context);
                    setting.setBrightFollowSystem(true);
                    SysManager.saveSetting(setting);
                } else {
                    BrightUtil.setBrightness((Activity) context, BrightUtil.progressToBright(setting.getBrightProgress()));
                    setting.setBrightFollowSystem(false);
                    SysManager.saveSetting(setting);
                }
            }
        });
        //简繁体
        final TextView tvSimplifiedAndTraditional = (TextView) view.findViewById(R.id.tv_simplified_and_traditional);
        if (setting.getLanguage() == Language.simplified) {
            tvSimplifiedAndTraditional.setText("繁");
        } else if (setting.getLanguage() == Language.traditional) {
            tvSimplifiedAndTraditional.setText("简");
        }
        tvSimplifiedAndTraditional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvSimplifiedAndTraditional.getText().toString().equals("简")) {
                    tvSimplifiedAndTraditional.setText("繁");
                } else {
                    tvSimplifiedAndTraditional.setText("简");
                }
                if (languageChangeListener != null) {
                    languageChangeListener.onClick(v);
                }
            }
        });

        //选择字体
        TextView tvFont = (TextView)view.findViewById(R.id.tv_text_font);
        tvFont.setOnClickListener(onFontClickListener);

        //自动滚屏速度
        SeekBar sbScrollSpeed = (SeekBar)view.findViewById(R.id.sb_auto_scroll_progress);
        TextView tvAutoScroll = (TextView)view.findViewById(R.id.tv_auto_scroll);
        sbScrollSpeed.setProgress(100 - setting.getAutoScrollSpeed() );
        sbScrollSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setting.setAutoScrollSpeed(100 - progress);
                SysManager.saveSetting(setting);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tvAutoScroll.setOnClickListener(autoScrollListener);

        dialog.show();
        return dialog;
    }

    private static void selectedStyle(ImageView curSelected, ReadStyle readStyle, OnReadStyleChangeListener listener) {
        ivLastSelectd.setSelected(false);
        ivLastSelectd = curSelected;
        curSelected.setSelected(true);
        if (listener != null) {
            listener.onChange(readStyle);
        }
    }


    /**
     * 阅读设置对话框
     *
     * @param context
     * @param isDayStyle
     * @param chapterProgress
     * @param backListener
     * @param lastChapterListener
     * @param nextChapterListener
     * @param chapterListListener
     * @param onClickNightAndDayListener
     * @param settingListener
     * @return
     */
    public static Dialog createReadSetting(final Context context, final boolean isDayStyle, int chapterProgress,
                                           View.OnClickListener backListener,
                                           View.OnClickListener lastChapterListener,
                                           View.OnClickListener nextChapterListener,
                                           View.OnClickListener chapterListListener,
                                           final OnClickNightAndDayListener onClickNightAndDayListener,
                                           View.OnClickListener settingListener,
                                           SeekBar.OnSeekBarChangeListener onSeekBarChangeListener,
                                           View.OnClickListener voiceOnClickListener) {
        final Dialog dialog = new Dialog(context, R.style.jmui_default_dialog_style);
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_read_setting, null);
        dialog.setContentView(view);
        LinearLayout llBack = (LinearLayout) view.findViewById(R.id.ll_title_back);
        TextView tvLastChapter = (TextView) view.findViewById(R.id.tv_last_chapter);
        TextView tvNextChapter = (TextView) view.findViewById(R.id.tv_next_chapter);
        SeekBar sbChapterProgress = (SeekBar) view.findViewById(R.id.sb_read_chapter_progress);
        LinearLayout llChapterList = (LinearLayout) view.findViewById(R.id.ll_chapter_list);
        LinearLayout llNightAndDay = (LinearLayout) view.findViewById(R.id.ll_night_and_day);
        LinearLayout llSetting = (LinearLayout) view.findViewById(R.id.ll_setting);
        final ImageView ivNightAndDay = (ImageView) view.findViewById(R.id.iv_night_and_day);
        final TextView tvNightAndDay = (TextView) view.findViewById(R.id.tv_night_and_day);
        ImageView ivVoice  = (ImageView)view.findViewById(R.id.iv_voice_read);

        view.findViewById(R.id.rl_title_view).setOnClickListener(null);
        view.findViewById(R.id.ll_bottom_view).setOnClickListener(null);

        Window window = dialog.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(dialog.getContext().getResources().getColor(R.color.sys_dialog_setting_bg));
        }

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dialog.dismiss();
                return false;
            }
        });
        if (!isDayStyle) {
            ivNightAndDay.setImageResource(R.mipmap.z4);
            tvNightAndDay.setText(context.getString(R.string.day));
        }

        llBack.setOnClickListener(backListener);
        tvLastChapter.setOnClickListener(lastChapterListener);
        tvNextChapter.setOnClickListener(nextChapterListener);
        sbChapterProgress.setProgress(chapterProgress);
        llChapterList.setOnClickListener(chapterListListener);
        llSetting.setOnClickListener(settingListener);
        sbChapterProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);
        ivVoice.setOnClickListener(voiceOnClickListener);
        llNightAndDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isDay;
                if (tvNightAndDay.getText().toString().equals(context.getString(R.string.day))) {
                    isDay = false;
                    ivNightAndDay.setImageResource(R.mipmap.ao);
                    tvNightAndDay.setText(context.getString(R.string.night));
                } else {
                    isDay = true;
                    ivNightAndDay.setImageResource(R.mipmap.z4);
                    tvNightAndDay.setText(context.getString(R.string.day));
                }
                if (onClickNightAndDayListener != null) {
                    onClickNightAndDayListener.onClick(dialog, view, isDay);
                }
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }


    /**
     * 创建一个普通对话框（包含确定、取消按键）
     *
     * @param context
     * @param title
     * @param mesage
     * @param cancelable       是否允许返回键取消
     * @param positiveListener 确定按键动作
     * @param negativeListener 取消按键动作
     * @return
     */
    public static AlertDialog createCommonDialog(Context context, String title, String mesage, boolean cancelable,
                                                 DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {

        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle(title);
        normalDialog.setCancelable(cancelable);
        normalDialog.setMessage(mesage);
        normalDialog.setPositiveButton("确定", positiveListener);
        normalDialog.setNegativeButton("取消", negativeListener);
        // 显示
        final AlertDialog alertDialog = normalDialog.create();
        MyApplication.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    alertDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return alertDialog;

    }

    /**
     * 创建一个普通对话框（包含key1、key2按键）
     *
     * @param context
     * @param title
     * @param mesage
     * @param key1
     * @param key2
     * @param positiveListener key1按键动作
     * @param negativeListener key2按键动作
     */
    public static void createCommonDialog(Context context, String title, String mesage,
                                          String key1, String key2,
                                          DialogInterface.OnClickListener positiveListener,
                                          DialogInterface.OnClickListener negativeListener) {
        try {

            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
//        normalDialog.setIcon(R.drawable.icon_dialog);
            normalDialog.setTitle(title);
            normalDialog.setCancelable(true);
            if (mesage != null) {
                normalDialog.setMessage(mesage);
            }
            normalDialog.setPositiveButton(key1, positiveListener);
            normalDialog.setNegativeButton(key2, negativeListener);
            // 显示
//        final AlertDialog alertDialog = normalDialog.create();
            MyApplication.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
//                    final AlertDialog alertDialog = normalDialog.create();
                        normalDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return alertDialog;
    }

    /**
     * 单按键对话框
     *
     * @param context
     * @param title
     * @param mesage
     * @param key
     * @param positiveListener
     */
    public static void createCommonDialog(Context context, String title, String mesage,
                                          String key, DialogInterface.OnClickListener positiveListener
    ) {
        try {
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
//        normalDialog.setIcon(R.drawable.icon_dialog);
            normalDialog.setTitle(title);
            normalDialog.setCancelable(false);
            if (mesage != null) {
                normalDialog.setMessage(mesage);
            }
            normalDialog.setPositiveButton(key, positiveListener);

            // 显示
//        final AlertDialog alertDialog = normalDialog.create();
            MyApplication.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
//                    final AlertDialog alertDialog = normalDialog.create();
                        normalDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return alertDialog;
    }


    /**
     * 创建一个进度对话框（圆形、旋转）
     *
     * @param context
     * @param title
     * @param message
     * @return
     */
    public static ProgressDialog createProgressDialog
    (Context context, String title, String message/*,
             DialogInterface.OnClickListener positiveListener,DialogInterface.OnClickListener negativeListener*/) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        if (!StringHelper.isEmpty(title)) {
            progressDialog.setTitle(title);
        }
        if (!StringHelper.isEmpty(message)) {
            progressDialog.setMessage(message);
        }
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      /*  progressDialog.setPositiveButton("确定",positiveListener);
        progressDialog.setNegativeButton("取消",negativeListener);*/
        // 显示
        MyApplication.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    progressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return progressDialog;
    }


    /**
     * 三按键对话框
     *
     * @param context
     * @param title
     * @param msg
     * @param btnText1
     * @param btnText2
     * @param btnText3
     * @param positiveListener
     * @param neutralListener
     * @param negativeListener
     * @return
     */
    public static AlertDialog createThreeButtonDialog(Context context, String title, String msg,
                                                      String btnText1, String btnText2, String btnText3,
                                                      DialogInterface.OnClickListener neutralListener,
                                                      DialogInterface.OnClickListener negativeListener,
                                                      DialogInterface.OnClickListener positiveListener) {
      /*  final EditText et = new EditText(context);*/

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        if (!StringHelper.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        //  第一个按钮
        builder.setNeutralButton(btnText1, neutralListener);
        //  中间的按钮
        builder.setNegativeButton(btnText2, negativeListener);
        //  第三个按钮
        builder.setPositiveButton(btnText3, positiveListener);

        AlertDialog dialog = builder.show();

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        //  Diglog的显示
        return dialog;
    }


    public interface OnClickPositiveListener {
        void onClick(Dialog dialog, View view);
    }

    public interface OnClickNegativeListener {
        void onClick(Dialog dialog, View view);
    }

    public interface OnClickNightAndDayListener {
        void onClick(Dialog dialog, View view, boolean isDayStyle);
    }

    public interface OnReadStyleChangeListener {
        void onChange(ReadStyle readStyle);
    }

    public interface OnBrightFollowSystemChangeListener {
        void onChange(boolean isFollowSystem);
    }


}
