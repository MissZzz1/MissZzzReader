package com.zhao.myreader.ui.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.zhao.myreader.R;

import com.zhao.myreader.base.BasePresenter;
import com.zhao.myreader.callback.ResultCallback;
import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.greendao.entity.SearchHistory;
import com.zhao.myreader.greendao.service.SearchHistoryService;
import com.zhao.myreader.ui.bookinfo.BookInfoActivity;

import com.zhao.myreader.util.StringHelper;
import com.zhao.myreader.webapi.CommonApi;

import java.util.ArrayList;
import java.util.Collections;



import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by zhao on 2017/7/26.
 */

public class SearchBookPrensenter implements BasePresenter {

    private SearchBookActivity mSearchBookActivity;
    private SearchBookAdapter mSearchBookAdapter;
    private String searchKey;//搜索关键字
    private ArrayList<Book> mBooks = new ArrayList<>();
    private ArrayList<SearchHistory> mSearchHistories = new ArrayList<>();
    private ArrayList<String> mSuggestions = new ArrayList<>();

    private SearchHistoryService mSearchHistoryService;

    private SearchHistoryAdapter mSearchHistoryAdapter;



    private int inputConfirm = 0;//搜索输入确认
    private int confirmTime = 1000;//搜索输入确认时间（毫秒）

    private static String[] suggestion = {"不朽凡人", "圣墟", "我是至尊" ,"龙王传说", "太古神王", "一念永恒", "雪鹰领主", "大主宰"};


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    search();
                    break;
                case 2:
                    initSearchList();
                    break;
                case 3:
                    mSearchBookActivity.getLvSearchBooksList().setAdapter(null);
                    mSearchBookActivity.getPbLoading().setVisibility(View.GONE);
                    break;
            }
        }
    };

    SearchBookPrensenter(SearchBookActivity searchBookActivity) {
        mSearchBookActivity = searchBookActivity;
        mSearchHistoryService = new SearchHistoryService();
        Collections.addAll(mSuggestions, suggestion);
    }

    @Override
    public void start() {

        mSearchBookActivity.getTvTitleText().setText("搜索");
        mSearchBookActivity.getLlTitleBack().setOnClickListener(view -> mSearchBookActivity.finish());

        mSearchBookActivity.getEtSearchKey().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                searchKey = editable.toString();
                if (StringHelper.isEmpty(searchKey)) {
                    search();
                }

            }

        });

        mSearchBookActivity.getEtSearchKey().setOnKeyListener((v, keyCode, event) -> {
            //是否是回车键
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                //隐藏键盘
                ((InputMethodManager) mSearchBookActivity.getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(mSearchBookActivity.getCurrentFocus()
                                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //搜索
                search();
            }
            return false;
        });


        mSearchBookActivity.getLvSearchBooksList().setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(mSearchBookActivity, BookInfoActivity.class);
            intent.putExtra(APPCONST.BOOK, mBooks.get(i));
            mSearchBookActivity.startActivity(intent);
        });
        mSearchBookActivity.getTvSearchConform().setOnClickListener(view -> search());
        mSearchBookActivity.getTgSuggestBook().setOnTagClickListener(tag -> {
            mSearchBookActivity.getEtSearchKey().setText(tag);
            search();
        });
        mSearchBookActivity.getLvHistoryList().setOnItemClickListener((parent, view, position, id) -> {
            mSearchBookActivity.getEtSearchKey().setText(mSearchHistories.get(position).getContent());
            search();
        });
        mSearchBookActivity.getLlClearHistory().setOnClickListener(v -> {
            mSearchHistoryService.clearHistory();
            initHistoryList();
        });
        initSuggestionBook();
        initHistoryList();
    }


    /**
     * 初始化建议书目
     */
    private void initSuggestionBook() {

        mSearchBookActivity.getTgSuggestBook().setTags(suggestion);
    }

    /**
     * 初始化历史列表
     */
    private void initHistoryList() {
        mSearchHistories = mSearchHistoryService.findAllSearchHistory();
        if (mSearchHistories == null || mSearchHistories.size() == 0) {
            mSearchBookActivity.getLlHistoryView().setVisibility(View.GONE);
        } else {
            mSearchHistoryAdapter = new SearchHistoryAdapter(mSearchBookActivity, R.layout.listview_search_history_item, mSearchHistories);
            mSearchBookActivity.getLvHistoryList().setAdapter(mSearchHistoryAdapter);
            mSearchBookActivity.getLlHistoryView().setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化搜索列表
     */
    private void initSearchList() {
        mSearchBookAdapter = new SearchBookAdapter(mSearchBookActivity, R.layout.listview_search_book_item, mBooks);
        mSearchBookActivity.getLvSearchBooksList().setAdapter(mSearchBookAdapter);
        mSearchBookActivity.getLvSearchBooksList().setVisibility(View.VISIBLE);
        mSearchBookActivity.getLlSuggestBooksView().setVisibility(View.GONE);
        mSearchBookActivity.getLlHistoryView().setVisibility(View.GONE);
        mSearchBookActivity.getPbLoading().setVisibility(View.GONE);

    }

    /**
     * 获取搜索数据
     */
    private void getData() {
        mBooks.clear();
        CommonApi.search(searchKey, new ResultCallback() {

            @Override
            public void onFinish(Object o, int code) {
                mBooks = (ArrayList<Book>) o;
                mHandler.sendMessage(mHandler.obtainMessage(2));
            }

            @Override
            public void onError(Exception e) {
                mHandler.sendMessage(mHandler.obtainMessage(3));
            }
        });

    }

    /**
     * 搜索
     */
    private void search() {
        mSearchBookActivity.getPbLoading().setVisibility(View.VISIBLE);
        if (StringHelper.isEmpty(searchKey)) {
            mSearchBookActivity.getPbLoading().setVisibility(View.GONE);
            mSearchBookActivity.getLvSearchBooksList().setVisibility(View.GONE);
            mSearchBookActivity.getLlSuggestBooksView().setVisibility(View.VISIBLE);
            initHistoryList();
            mSearchBookActivity.getLvSearchBooksList().setAdapter(null);
        } else {
            mSearchBookActivity.getLvSearchBooksList().setVisibility(View.VISIBLE);
            mSearchBookActivity.getLlSuggestBooksView().setVisibility(View.GONE);
            mSearchBookActivity.getLlHistoryView().setVisibility(View.GONE);
            getData();
            mSearchHistoryService.addOrUpadteHistory(searchKey);
        }
    }

    boolean onBackPressed() {
        if (StringHelper.isEmpty(searchKey)) {
            return false;
        } else {
            mSearchBookActivity.getEtSearchKey().setText("");
            return true;
        }
    }


}

