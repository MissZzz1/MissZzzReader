package com.zhao.myreader.webapi;

import com.zhao.myreader.callback.ResultCallback;
import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.common.URLCONST;
import com.zhao.myreader.util.TianLaiReadUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhao on 2017/7/24.
 */

public class CommonApi extends BaseApi{


    /**
     * 获取章节列表
     * @param url
     * @param callback
     */
    public static void getBookChapters(String url, final ResultCallback callback){

        getCommonReturnHtmlStringApi(url, null, "GBK", new ResultCallback() {
            @Override
            public void onFinish(Object o, int code) {
                callback.onFinish(TianLaiReadUtil.getChaptersFromHtml((String) o),0);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);

            }
        });
    }

    /**
     * 获取章节正文
     * @param url
     * @param callback
     */
    public static void getChapterContent(String url, final ResultCallback callback){
        int tem = url.indexOf("\"");
        if (tem != -1){
            url = url.substring(0,tem);
        }
        getCommonReturnHtmlStringApi(URLCONST.nameSpace + url, null, "GBK", new ResultCallback() {
            @Override
            public void onFinish(Object o, int code) {
                callback.onFinish(TianLaiReadUtil.getContentFormHtml((String)o),0);

            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    /**
     * 搜索小说
     * @param key
     * @param callback
     */
    public static void search(String key, final ResultCallback callback){
        Map<String,Object> params = new HashMap<>();
        params.put("s",APPCONST.s);
        params.put("q", key);
        params.put("click","1");
        getCommonReturnHtmlStringApi(URLCONST.method_buxiu_search, params, "utf-8", new ResultCallback() {
            @Override
            public void onFinish(Object o, int code) {
                callback.onFinish(TianLaiReadUtil.getBooksFromSearchHtml((String)o),code);

            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);

            }
        });
    }


}
