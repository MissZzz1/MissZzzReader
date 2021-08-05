package com.zhao.myreader.webapi;

import com.zhao.myreader.callback.ResultCallback;
import com.zhao.myreader.common.URLCONST;
import com.zhao.myreader.enums.BookSource;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.util.crawler.BiQuGeReadUtil;
import com.zhao.myreader.util.crawler.DingDianReadUtil;
import com.zhao.myreader.util.crawler.TianLaiReadUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhao on 2017/7/24.
 */

public class BookStoreApi extends BaseApi{


    /**
     * 获取书城小说分类列表
     * @param url
     * @param callback
     */
    public static void getBookTypeList(String url, final ResultCallback callback){

        getCommonReturnHtmlStringApi(url, null, "GBK", new ResultCallback() {
            @Override
            public void onFinish(Object o, int code) {
                callback.onFinish(BiQuGeReadUtil.getBookTypeList((String) o),0);



            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);

            }
        });
    }


    /**
     * 获取某一分类小说排行榜列表
     * @param url
     * @param callback
     */
    public static void getBookRankList(String url, final ResultCallback callback){

        getCommonReturnHtmlStringApi(url, null, "GBK", new ResultCallback() {
            @Override
            public void onFinish(Object o, int code) {
                callback.onFinish(BiQuGeReadUtil.getBookRankList((String) o),0);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);

            }
        });
    }


    /**
     * 获取小说详细信息
     * @param book
     * @param callback
     */
    public static void getBookInfo(Book book, final ResultCallback callback){

        getCommonReturnHtmlStringApi(book.getChapterUrl(), null, "GBK", new ResultCallback() {
            @Override
            public void onFinish(Object o, int code) {
                if (BookSource.dingdian.toString().equals(book.getSource())){
                    callback.onFinish(DingDianReadUtil.getBookInfo((String) o,book),0);

                }else if(BookSource.biquge.toString().equals(book.getSource())){

                    callback.onFinish(BiQuGeReadUtil.getBookInfo((String) o,book),0);
                }

            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);

            }
        });
    }


    /**
     * 获取顶点小说排行榜
     * @param url
     * @param callback
     */
    public static void getDdBookRank(String url, final ResultCallback callback){

        getCommonReturnHtmlStringApi(url, null, "GBK", new ResultCallback() {
            @Override
            public void onFinish(Object o, int code) {
                callback.onFinish(DingDianReadUtil.getRank((String) o),0);



            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);

            }
        });
    }






}
