package com.zhao.myreader.util;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;


import androidx.core.content.FileProvider;

import com.zhao.myreader.R;

import java.io.File;

/**
 * Created by zhao on 2017/3/3.
 */

public class UriFileUtil {

    /**
     * 获取URI
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), context.getString(R.string.sys_file_provider), file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getFilePathByUri(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    /**

     * Get a file path from a Uri. This will get the the path for Storage Access

     * Framework Documents, as well as the _data field for the MediaStore and

     * other file-based ContentProviders.

     *

     * @param context The context.

     * @param uri The Uri to query.

     * @author paulburke

     */

    public static String getPath(final Context context, final Uri uri) {



        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;



        // DocumentProvider

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider

            if (isExternalStorageDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);

                final String[] split = docId.split(":");

                final String type = split[0];



                if ("primary".equalsIgnoreCase(type)) {

                    return Environment.getExternalStorageDirectory() + "/" + split[1];

                }



                // TODO handle non-primary volumes

            }

            // DownloadsProvider

            else if (isDownloadsDocument(uri)) {



                final String id = DocumentsContract.getDocumentId(uri);

                final Uri contentUri = ContentUris.withAppendedId(

                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));



                return getDataColumn(context, contentUri, null, null);

            }

            // MediaProvider

            else if (isMediaDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);

                final String[] split = docId.split(":");

                final String type = split[0];



                Uri contentUri = null;

                if ("image".equals(type)) {

                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                } else if ("video".equals(type)) {

                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                } else if ("audio".equals(type)) {

                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                }



                final String selection = "_id=?";

                final String[] selectionArgs = new String[] {

                        split[1]

                };



                return getDataColumn(context, contentUri, selection, selectionArgs);

            }

        }

        // MediaStore (and general)

        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            return getDataColumn(context, uri, null, null);

        }

        // File

        else if ("file".equalsIgnoreCase(uri.getScheme())) {

            return uri.getPath();

        }



        return null;

    }



    /**

     * Get the value of the data column for this Uri. This is useful for

     * MediaStore Uris, and other file-based ContentProviders.

     *

     * @param context The context.

     * @param uri The Uri to query.

     * @param selection (Optional) Filter used in the query.

     * @param selectionArgs (Optional) Selection arguments used in the query.

     * @return The value of the _data column, which is typically a file path.

     */

    public static String getDataColumn(Context context, Uri uri, String selection,

                                       String[] selectionArgs) {



        Cursor cursor = null;

        final String column = "_data";

        final String[] projection = {

                column

        };



        try {

            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,

                    null);

            if (cursor != null && cursor.moveToFirst()) {

                final int column_index = cursor.getColumnIndexOrThrow(column);

                return cursor.getString(column_index);

            }

        } finally {

            if (cursor != null)

                cursor.close();

        }

        return null;

    }





    /**

     * @param uri The Uri to check.

     * @return Whether the Uri authority is ExternalStorageProvider.

     */

    public static boolean isExternalStorageDocument(Uri uri) {

        return "com.android.externalstorage.documents".equals(uri.getAuthority());

    }



    /**

     * @param uri The Uri to check.

     * @return Whether the Uri authority is DownloadsProvider.

     */

    public static boolean isDownloadsDocument(Uri uri) {

        return "com.android.providers.downloads.documents".equals(uri.getAuthority());

    }



    /**

     * @param uri The Uri to check.

     * @return Whether the Uri authority is MediaProvider.

     */

    public static boolean isMediaDocument(Uri uri) {

        return "com.android.providers.media.documents".equals(uri.getAuthority());

    }
}
