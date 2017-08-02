package com.zhao.myreader.ui.search;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import com.zhao.myreader.R;
import com.zhao.myreader.base.BasePresenter;
import com.zhao.myreader.callback.ResultCallback;
import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.ui.bookinfo.BookInfoActivity;
import com.zhao.myreader.util.StringHelper;
import com.zhao.myreader.webapi.CommonApi;

import java.util.ArrayList;

/**
 * Created by zhao on 2017/7/26.
 */

public class SearchBookPrensenter implements BasePresenter {

    private SearchBookActivity mSearchBookActivity;
    private SearchBookAdapter mSearchBookAdapter;
    private String searchKey;//搜索关键字
    private ArrayList<Book> mBooks = new ArrayList<>();

    private int inputConfirm = 0;//搜索输入确认
    private int confirmTime = 1000;//搜索输入确认时间（毫秒）

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

    public SearchBookPrensenter(SearchBookActivity searchBookActivity) {
        mSearchBookActivity = searchBookActivity;
    }

    @Override
    public void start() {
        mSearchBookActivity.getTvTitleText().setText("搜索");
        mSearchBookActivity.getLlTitleBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchBookActivity.finish();
            }
        });

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
                if (StringHelper.isEmpty(searchKey)){
                    search();
                }
              /*  //每次输入确认标志+1
                inputConfirm++;
                //当前标志
                final int now = inputConfirm;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //等待确认时间
                            Thread.sleep(confirmTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (now == inputConfirm) {
                            searchKey = editable.toString();
                            mHandler.sendMessage(mHandler.obtainMessage(1));
                        }
                    }
                }).start();*/

            }
        });
        mSearchBookActivity.getLvSearchBooksList().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mSearchBookActivity, BookInfoActivity.class);
                intent.putExtra(APPCONST.BOOK,mBooks.get(i));
                mSearchBookActivity.startActivity(intent);
            }
        });
        mSearchBookActivity.getTvSearchConform().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

    }

    private void initSearchList() {
        mSearchBookAdapter = new SearchBookAdapter(mSearchBookActivity, R.layout.listview_search_book_item,mBooks);
        mSearchBookActivity.getLvSearchBooksList().setAdapter(mSearchBookAdapter);
        mSearchBookActivity.getLvSearchBooksList().setVisibility(View.VISIBLE);
        mSearchBookActivity.getLlSuggestBooksView().setVisibility(View.GONE);
        mSearchBookActivity.getPbLoading().setVisibility(View.GONE);

    }

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

    private void search() {
        mSearchBookActivity.getPbLoading().setVisibility(View.VISIBLE);
        if (StringHelper.isEmpty(searchKey)) {
            mSearchBookActivity.getPbLoading().setVisibility(View.GONE);
            mSearchBookActivity.getLvSearchBooksList().setVisibility(View.GONE);
            mSearchBookActivity.getLlSuggestBooksView().setVisibility(View.VISIBLE);
            mSearchBookActivity.getLvSearchBooksList().setAdapter(null);
        } else {
            mSearchBookActivity.getLvSearchBooksList().setVisibility(View.VISIBLE);
            mSearchBookActivity.getLlSuggestBooksView().setVisibility(View.GONE);
            getData();
        }

    }


}
