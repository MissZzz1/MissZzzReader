package com.zhao.myreader.util;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.creator.DialogCreator;

import java.util.HashMap;
import java.util.Map;

import static android.app.DownloadManager.Request.VISIBILITY_HIDDEN;

/**
 * Created by hmt on 2016/12/20.
 */

public class DownloadMangerUtils {

    public static final String FILE_DIR = "gxdw";
    private static Map<Long, BroadcastReceiver> mBroadcastReceiverMap = new HashMap<>();

    /**
     * 文件下载
     *
     * @param context
     * @param fileDir
     * @param url
     * @param fileName
     */
    public static void downloadFile(Context context, String fileDir, String url, String fileName) {

        try {
            Log.d("http download:", url);
            //String Url = "10.10.123.16:8080/gxqdw_ubap/mEmailController.thumb?getAttachmentStream&fileId=1&fileName=自我探索——我是谁.ppt&emailId=36&token=d1828248-cc71-4719-8218-adc31ffc9cca&type=inbox&fileSize=14696446";

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            // DownloadManager.Request.setDestinationInExternalPublicDir();

            request.setDescription(fileName);
            request.setTitle("附件");
            // in order for this if to run, you must use the android 3.2 to compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                // request.setNotificationVisibility(VISIBILITY_HIDDEN);
            }
            // int i = Build.VERSION.SDK_INT;
            if (Build.VERSION.SDK_INT > 17) {
                request.setDestinationInExternalPublicDir(fileDir, fileName);
            } else {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    request.setDestinationInExternalPublicDir(fileDir, fileName);
                } else {
                    Log.d("download", "android版本过低，不存在外部存储，下载路径无法指定,默认路径：/data/data/com.android.providers.downloads/cache/");
                    DialogCreator.createCommonDialog(context, "文件下载", "android版本过低或系统兼容性问题，不存在外部存储，无法指定下载路径，文件下载到系统默认路径,请到文件管理搜索文件名",
                            "关闭", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                }
            }
            // get download service and enqueue file
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
            lowVersionNoSDDownload(context, url, fileName);
        }
    }

    /**
     * 低版本无外置存储下载
     *
     * @param context
     * @param url
     * @param fileName
     */
    private static void lowVersionNoSDDownload(Context context, String url, String fileName) {
        try {
            Log.d("http download:", url);
            //String Url = "10.10.123.16:8080/gxqdw_ubap/mEmailController.thumb?getAttachmentStream&fileId=1&fileName=自我探索——我是谁.ppt&emailId=36&token=d1828248-cc71-4719-8218-adc31ffc9cca&type=inbox&fileSize=14696446";

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            // DownloadManager.Request.setDestinationInExternalPublicDir();

            request.setDescription(fileName);
            request.setTitle("附件");
            // in order for this if to run, you must use the android 3.2 to compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                // request.setNotificationVisibility(VISIBILITY_HIDDEN);
            }
            // int i = Build.VERSION.SDK_INT;

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                request.setDestinationInExternalPublicDir(APPCONST.FILE_DIR, fileName);
            } else {
                Log.d("download", "android版本过低，不存在外部存储，下载路径无法指定,默认路径：/data/data/com.android.providers.downloads/cache/");
                DialogCreator.createCommonDialog(context, "文件下载", "android版本过低或系统兼容性问题，不存在外部存储，无法指定下载路径，文件下载到系统默认路径,请到文件管理搜索文件名",
                        "关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
            // get download service and enqueue file
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
            TextHelper.showText("下载错误:" + e.toString());
        }

    }


    /**
     * 文件下载（有回调，无通知）
     *
     * @param context
     * @param fileDir
     * @param fileName
     * @param url
     * @param listener
     */
    public static void downloadFileByFinishListener(Context context, String fileDir, String fileName, String url,
                                                    final DownloadCompleteListener listener) {
        try {
            if (isPermission(context)) {
                Log.d("http download:", url);
//        String Url = "10.10.123.16:8080/gxqdw_ubap/mEmailController.thumb?getAttachmentStream&fileId=1&fileName=自我探索——我是谁.ppt&emailId=36&token=d1828248-cc71-4719-8218-adc31ffc9cca&type=inbox&fileSize=14696446";

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//        DownloadManager.Request.setDestinationInExternalPublicDir();

      /*  request.setDescription(fileName);
        request.setTitle("附件");*/
// in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setNotificationVisibility(VISIBILITY_HIDDEN);
                }
//        int i = Build.VERSION.SDK_INT;
                if (Build.VERSION.SDK_INT > 17) {
                    request.setDestinationInExternalPublicDir(fileDir, fileName);
                } else {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        request.setDestinationInExternalPublicDir(fileDir, fileName);
                    } else {
                        Log.i("download", "android版本过低，不存在外部存储，下载路径无法指定,默认路径：/data/data/com.android.providers.downloads/cache/");
                    }
                }
// get download service and enqueue file
                final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                final long id = manager.enqueue(request);
                // 注册广播监听系统的下载完成事件。
                IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

                final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        if (ID == id) {
                            listener.onFinish(manager.getUriForDownloadedFile(id));
                            context.unregisterReceiver(mBroadcastReceiverMap.get(id));
                            mBroadcastReceiverMap.remove(id);
                   /* Toast.makeText(getApplicationContext(), "任务:" + Id + " 下载完成!", Toast.LENGTH_LONG).show();*/
                        }
                    }
                };
                context.registerReceiver(broadcastReceiver, intentFilter);
                mBroadcastReceiverMap.put(id, broadcastReceiver);
            }
        } catch (Exception e) {
            lowVersionNoSDDownloadFileByFinishListener(context, fileDir, fileName, url, listener);
//            listener.onError(e.toString());
        }

    }

    /**
     * 低版本无外置存储文件下载（有回调，无通知）
     *
     * @param context
     * @param fileDir
     * @param fileName
     * @param url
     * @param listener
     */
    private static void lowVersionNoSDDownloadFileByFinishListener(Context context, String fileDir, String fileName, String url,
                                                                   final DownloadCompleteListener listener) {
        try {

            Log.d("http download:", url);
//        String Url = "10.10.123.16:8080/gxqdw_ubap/mEmailController.thumb?getAttachmentStream&fileId=1&fileName=自我探索——我是谁.ppt&emailId=36&token=d1828248-cc71-4719-8218-adc31ffc9cca&type=inbox&fileSize=14696446";

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//        DownloadManager.Request.setDestinationInExternalPublicDir();

      /*  request.setDescription(fileName);
        request.setTitle("附件");*/
// in order for this if to run, you must use the android 3.2 to compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setNotificationVisibility(VISIBILITY_HIDDEN);
            }
//        int i = Build.VERSION.SDK_INT;

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                request.setDestinationInExternalPublicDir(APPCONST.FILE_DIR, fileName);
            } else {
                Log.d("download", "android版本过低，不存在外部存储，下载路径无法指定,默认路径：/data/data/com.android.providers.downloads/cache/");
            }

// get download service and enqueue file
            final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            final long id = manager.enqueue(request);
            // 注册广播监听系统的下载完成事件。
            IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

            final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (ID == id) {
                        listener.onFinish(manager.getUriForDownloadedFile(id));
                        context.unregisterReceiver(mBroadcastReceiverMap.get(id));
                        mBroadcastReceiverMap.remove(id);
                   /* Toast.makeText(getApplicationContext(), "任务:" + Id + " 下载完成!", Toast.LENGTH_LONG).show();*/
                    }
                }
            };
            context.registerReceiver(broadcastReceiver, intentFilter);
            mBroadcastReceiverMap.put(id, broadcastReceiver);

        } catch (Exception e) {
            listener.onError(e.toString());
        }

    }

    /**
     * 文件下载（有回调，有通知）
     *
     * @param context
     * @param fileDir
     * @param fileName
     * @param url
     * @param title
     * @param listener
     */
    public static void downloadFileOnNotificationByFinishListener(final Context context, final String fileDir, final String fileName, final String url,
                                                                  final String title, final DownloadCompleteListener listener) {
        try {
            if (isPermission(context)) {
                Log.d("http download:", url);

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setTitle(title);

                // in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                }

                if (Build.VERSION.SDK_INT > 17) {
                   /* File file = new File(Environment.getExternalStorageDirectory() + "/gxdw/apk/app_gxdw_186.apk");
                    if (!file.exists()){
                      boolean flag = file.createNewFile();
                    }*/
                    request.setDestinationInExternalPublicDir(fileDir, fileName);

                } else {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        request.setDestinationInExternalPublicDir(fileDir, fileName);
                    } else {
                        Log.d("download", "android版本过低，不存在外部存储，下载路径无法指定,默认路径：/data/data/com.android.providers.downloads/cache/");
                    }
                }

// get download service and enqueue file
                final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                final long id = manager.enqueue(request);
                // 注册广播监听系统的下载完成事件。
                IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

                final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        if (ID == id) {
                            listener.onFinish(manager.getUriForDownloadedFile(id));
                            context.unregisterReceiver(mBroadcastReceiverMap.get(id));
                            mBroadcastReceiverMap.remove(id);
                   /* Toast.makeText(getApplicationContext(), "任务:" + Id + " 下载完成!", Toast.LENGTH_LONG).show();*/
                        }
                    }
                };
                context.registerReceiver(broadcastReceiver, intentFilter);
                mBroadcastReceiverMap.put(id, broadcastReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
            lowVersionDownloadFileOnNotificationByFinishListener(context, fileDir, fileName, url, title, listener);
        }
    }

    /**
     * 低版本文件下载（有回调，有通知）
     *
     * @param context
     * @param fileDir
     * @param fileName
     * @param url
     * @param title
     * @param listener
     */
    private static void lowVersionDownloadFileOnNotificationByFinishListener(final Context context, final String fileDir, final String fileName, final String url,
                                                                             final String title, final DownloadCompleteListener listener) {


        try {

            Log.d("http download:", url);

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(title);

// in order for this if to run, you must use the android 3.2 to compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

            }

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                request.setDestinationInExternalPublicDir(APPCONST.FILE_DIR, fileName);
            } else {
                Log.d("download", "android版本过低，不存在外部存储，下载路径无法指定,默认路径：/data/data/com.android.providers.downloads/cache/");
            }


           // get download service and enqueue file
            final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            final long id = manager.enqueue(request);
            // 注册广播监听系统的下载完成事件。
            IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

            final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (ID == id) {
                        listener.onFinish(manager.getUriForDownloadedFile(id));
                        context.unregisterReceiver(mBroadcastReceiverMap.get(id));
                        mBroadcastReceiverMap.remove(id);
                    }
                }
            };
            context.registerReceiver(broadcastReceiver, intentFilter);
            mBroadcastReceiverMap.put(id, broadcastReceiver);

        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(e.toString());
        }


    }


    /**
     * 读写权限判断
     *
     * @param context
     * @return
     */
    public static boolean isPermission(Context context) {
        boolean permission = false;
        if (Build.VERSION.SDK_INT >= 23) {
            int checkReadPhoneStatePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkReadPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                // 弹出对话框接收权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                TextHelper.showText("当前应用未拥有存储设备读写权限");
            } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 弹出对话框接收权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                TextHelper.showText("当前应用未拥有存储设备读写权限");
            } else {
                permission = true;
            }
        } else {
            permission = true;
        }
        return permission;
    }

    public interface DownloadCompleteListener {
        void onFinish(Uri uri);

        void onError(String s);
    }


}
