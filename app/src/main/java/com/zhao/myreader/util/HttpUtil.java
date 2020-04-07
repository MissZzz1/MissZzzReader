package com.zhao.myreader.util;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.zhao.myreader.application.MyApplication;
import com.zhao.myreader.application.TrustAllCerts;
import com.zhao.myreader.callback.HttpCallback;
import com.zhao.myreader.callback.JsonCallback;
import com.zhao.myreader.callback.URLConnectionCallback;
import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.common.URLCONST;
import com.zhao.myreader.entity.JsonModel;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.String.valueOf;

/**
 * Created by zhao on 2016/4/16.
 */

public class HttpUtil {

    private static String sessionid;
    //最好只使用一个共享的OkHttpClient 实例，将所有的网络请求都通过这个实例处理。
    //因为每个OkHttpClient 实例都有自己的连接池和线程池，重用这个实例能降低延时，减少内存消耗，而重复创建新实例则会浪费资源。
    private static OkHttpClient mClient;

    static final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }
    };

   static class TrustEveryoneManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());


            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }



    private static synchronized OkHttpClient getOkHttpClient(){
        if (mClient == null){

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30000, TimeUnit.SECONDS);
            builder.sslSocketFactory(createSSLSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
           mClient = builder
                    .build();
        }
        return mClient;

    }

    /**
     * 图片发送
     * @param address
     * @param callback
     */
    public static void sendBitmapGetRequest(final String address, final HttpCallback callback) {
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            @Override
            public void run() {
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-type", "application/octet-stream");
                    connection.setRequestProperty("Accept-Charset", "utf-8");
                    connection.setRequestProperty("contentType", "utf-8");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e("Http", "网络错误异常！!!!");
                    }
                    Log.d("Http", "connection success");
                    if (callback != null) {
                        callback.onFinish(in);
                    }
                } catch (Exception e) {
                    Log.e("Http", e.toString());
                    if (callback != null) {
                        callback.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }

        }).start();
    }

    /**
     * get请求
     * @param address
     * @param callback
     */
    public static void sendGetRequest(final String address, final HttpCallback callback) {
        new Thread(new Runnable() {
            HttpURLConnection connection = null;

            @Override
            public void run() {
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-type", "text/html");
                    connection.setRequestProperty("Accept-Charset", "utf-8");
                    connection.setRequestProperty("contentType", "utf-8");
                    connection.setConnectTimeout(60 * 1000);
                    connection.setReadTimeout(60 * 1000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e("Http", "网络错误异常！!!!");
                    }
                    InputStream in = connection.getInputStream();
                    Log.d("Http", "connection success");
                    if (callback != null) {
                        callback.onFinish(in);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Http", e.toString());
                    if (callback != null) {
                        callback.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 网络通信测试请求
     * @param address
     * @param callback
     */
    public static void sendTestGetRequest(final String address, final HttpCallback callback) {
        new Thread(new Runnable() {
            HttpURLConnection connection = null;

            @Override
            public void run() {
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-type", "text/html");
                    connection.setRequestProperty("Accept-Charset", "utf-8");
                    connection.setRequestProperty("contentType", "utf-8");
                    connection.setConnectTimeout(3 * 1000);
                    connection.setReadTimeout(3 * 1000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e("Http", "网络错误异常！!!!");
                    }
                    InputStream in = connection.getInputStream();
                    Log.d("Http", "connection success");
                    if (callback != null) {
                        callback.onFinish(in);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Http", e.toString());
                    if (callback != null) {
                        callback.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendGetRequest_okHttp(final String address, final HttpCallback callback) {
       MyApplication.getApplication().newThread(new Runnable() {
            @Override
            public void run() {
             /*   HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-type", "text/html");
                    connection.setRequestProperty("Accept-Charset", "gbk");
                    connection.setRequestProperty("contentType", "gbk");
                    connection.setConnectTimeout(5 * 1000);
                    connection.setReadTimeout(5 * 1000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e("Http", "网络错误异常！!!!");
                    }
                    InputStream in = connection.getInputStream();
                    Log.d("Http", "connection success");
                    if (callback != null) {
                        callback.onFinish(in);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Http", e.toString());
                    if (callback != null) {
                        callback.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }*/
                try{

                     OkHttpClient client = getOkHttpClient();
                    Request request = new Request.Builder()
                            .url(address)
                            .build();

                    Response response = client.newCall(request).execute();
                    callback.onFinish(response.body().byteStream());

                }catch(Exception e){
                    e.printStackTrace();
                    callback.onError(e);
                }
            }

        });
    }

    /**
     * post请求
     * @param address
     * @param output
     * @param callback
     */
    public static void sendPostRequest(final String address, final String output, final HttpCallback callback) {
        new Thread(new Runnable() {
            HttpURLConnection connection = null;

            @Override
            public void run() {
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(60 * 1000);
                    connection.setReadTimeout(60 * 1000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    if (output != null) {
                        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                        out.writeBytes(output);
                    }
                    InputStream in = connection.getInputStream();
                    if (callback != null) {
                        callback.onFinish(in);
                    }
                } catch (Exception e) {
                    if (callback != null) {
                        callback.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendPostRequest_okHttp(final String address, final String output, final HttpCallback callback) {
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            @Override
            public void run() {
                try {
                    MediaType contentType  = MediaType.parse("charset=utf-8");
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(contentType, output);
                    Request request = new Request.Builder()
                            .url(address)
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    callback.onFinish(response.body().byteStream());
                } catch (Exception e) {
                    if (callback != null) {
                        callback.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 多文件上传请求
     * @param url
     * @param files
     * @param params
     * @param callback
     */
    public static void uploadFile(String url, ArrayList<File> files, Map<String, Object> params, final HttpCallback callback){
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(File file : files){
            if(file != null){
                // MediaType.parse() 里面是上传的文件类型。
                RequestBody body = RequestBody.create(MediaType.parse("*/*"), file);
                String filename = file.getName();
                // 参数分别为， 请求key ，文件名称 ， RequestBody
                requestBody.addFormDataPart(file.getName(), file.getName(), body);
            }
        }

        if (params != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : params.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).tag(MyApplication.getApplication()).build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
                Log.i("Http" ,"onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    callback.onFinish(str);
                    Log.i("Http", response.message() + " , body " + str);
                } else {
                    JsonModel jsonModel = new JsonModel();
                    jsonModel.setSuccess(false);
//                    jsonModel.setResult(response.body().string());
                    callback.onFinish(new Gson().toJson(jsonModel));
                    Log.i("Http" ,response.message() + " error : body " + response.body().string());
                }
            }

        });

    }


    /**
     * 生成URL
     * @param p_url
     * @param params
     * @return
     */
    public static String makeURL(String p_url, Map<String, Object> params) {
        if (params == null) return p_url;
        StringBuilder url = new StringBuilder(p_url);
        Log.d("http", p_url);
        if (url.indexOf("?") < 0)
            url.append('?');
        for (String name : params.keySet()) {
            Log.d("http", name + "=" + params.get(name));
            url.append('&');
            url.append(name);
            url.append('=');
            try {
                if (URLCONST.isRSA) {
                    if (name.equals("token")) {
                        url.append(valueOf(params.get(name)));
                    } else {
                        url.append(StringHelper.encode(Base64.encodeToString(RSAUtilV2.encryptByPublicKey(valueOf(params.get(name)).getBytes(), APPCONST.publicKey), Base64.DEFAULT).replace("\n", "")));
                    }
                } else {
                    url.append(valueOf(params.get(name)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //不做URLEncoder处理
//			try {
//				url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        }
        return url.toString().replace("?&", "?");
    }

    /**
     * 生成URL（不加密）
     * @param p_url
     * @param params
     * @return
     */
    public static String makeURLNoRSA(String p_url, Map<String, Object> params) {
        if (params == null) return "";
        StringBuilder url = new StringBuilder(p_url);
        Log.d("http", p_url);
        if (url.indexOf("?") < 0)
            url.append('?');
        for (String name : params.keySet()) {
            Log.d("http", name + "=" + params.get(name));
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(valueOf(params.get(name)));
            //不做URLEncoder处理
//			try {
//				url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        }
        return url.toString().replace("?&", "?");
    }

    /**
     * 生成post输出参数串
     * @param params
     * @return
     */
    public static String makePostOutput(Map<String, Object> params) {
        StringBuilder output = new StringBuilder();
        Iterator<String> it = params.keySet().iterator();
        while (true) {
            String name = it.next();
            Log.d("http", name + "=" + params.get(name));
            output.append(name);
            output.append('=');
            try {
                if (URLCONST.isRSA) {
                    if (name.equals("token")) {
                        output.append(valueOf(params.get(name)));
                    } else {
                        output.append(StringHelper.encode(Base64.encodeToString(RSAUtilV2.encryptByPublicKey(valueOf(params.get(name)).getBytes(), APPCONST.publicKey), Base64.DEFAULT).replace("\n", "")));
                    }
                } else {
                    output.append(valueOf(params.get(name)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!it.hasNext()) {
                break;
            }
            output.append('&');
            //不做URLEncoder处理
//			try {
//				url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        }
        return output.toString();
    }

    public static void uploadFileRequest(final String actionUrl,
                                         final String[] filePaths,
                                         final Map<String, Object> params,
                                         final JsonCallback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadFile(actionUrl, params, filePaths, callback);
            }

        }).start();

    }


    /**
     * 测试URL可连接性
     * @param url
     * @param connectionCallback
     */
    public static void isURLConnection(String url, final URLConnectionCallback connectionCallback){
        sendTestGetRequest(url, new HttpCallback() {
            @Override
            public void onFinish(String response) {
                connectionCallback.onFinish(true);
            }

            @Override
            public void onFinish(InputStream in) {
                connectionCallback.onFinish(true);
            }

            @Override
            public void onFinish(Bitmap bm) {
                connectionCallback.onFinish(true);
            }

            @Override
            public void onError(Exception e) {
                connectionCallback.onFinish(false);

            }
        });
    }


    /**
     * 多文件上传的方法
     *
     * @param actionUrl：上传的路径
     * @param uploadFilePaths：需要上传的文件路径，数组
     * @return
     */
    private static void uploadFile(String actionUrl, Map<String, Object> params, String[] uploadFilePaths, JsonCallback callback) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String CHARSET = "utf-8"; //设置编码
        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try {
            Log.i("http", "开始上传文件");
            // 统一资源
            URL url = new URL(makeURL(actionUrl, params));
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(60000);
            urlConnection.setReadTimeout(60000);
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setDoInput(true);
            // 设置是否向httpUrlConnection输出
            httpURLConnection.setDoOutput(true);
            // Post 请求不能使用缓存
            httpURLConnection.setUseCaches(false);
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码连接参数
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 设置请求内容类型
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            // 设置DataOutputStream
            ds = new DataOutputStream(httpURLConnection.getOutputStream());
            for (int i = 0; i < uploadFilePaths.length; i++) {
                String uploadFile = uploadFilePaths[i];
                String filename = uploadFile.substring(uploadFile.lastIndexOf("/") + 1);
                //设置参数
                StringBuffer sb = new StringBuffer();
                sb.append(end);
                sb.append(twoHyphens + boundary + end);
                sb.append("Content-Disposition: form-data; " + "name=\"file" + i + "\";filename=\"" + filename + "\"" + end);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + end);
                sb.append(end);
                Log.i("http", "参数：" + sb.toString());
                //写入文件数据
                ds.write(sb.toString().getBytes());
                FileInputStream fStream = new FileInputStream(uploadFile);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
                int total = 0;
                while ((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                    total += length;
                }
                Log.i("http", "文件的大小：" + total);
                ds.writeBytes(end);
               /* close streams */
                fStream.close();
            }
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
           /* close streams */
            ds.flush();
            if (httpURLConnection.getResponseCode() >= 300) {
                callback.onError(new Exception(
                        "HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode()));
//               throw new Exception(
//                       "HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);
                tempLine = null;
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
                Log.i("http", resultBuffer.toString());
                if (callback != null) {
                    Gson gson = new Gson();
                    JsonModel jsonModel = gson.fromJson(resultBuffer.toString(), JsonModel.class);
                    if (URLCONST.isRSA && !StringHelper.isEmpty(jsonModel.getResult())) {
                        jsonModel.setResult(StringHelper.decode(new String(RSAUtilV2.decryptByPrivateKey(Base64.decode(jsonModel.getResult().replace("\n", ""), Base64.DEFAULT), APPCONST.privateKey))));
                    }
                    callback.onFinish(jsonModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * Trust every server - dont check for any certificate
     */
    public static void trustAllHosts() {
        final String TAG = "trustAllHosts";
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.i(TAG, "checkClientTrusted");
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.i(TAG, "checkServerTrusted");
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
