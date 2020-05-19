package com.zhao.myreader.ui.home.bookstore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.zhao.myreader.base.BasePresenter;
import com.zhao.myreader.callback.ResultCallback;
import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.common.URLCONST;
import com.zhao.myreader.entity.bookstore.BookType;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.ui.bookinfo.BookInfoActivity;
import com.zhao.myreader.util.TextHelper;
import com.zhao.myreader.webapi.BookStoreApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao on 2017/7/25.
 */

public class BookStorePresenter extends BasePresenter {

    private BookStoreFragment mBookStoreFragment;

    private LinearLayoutManager mLinearLayoutManager;
    private BookStoreBookTypeAdapter mBookStoreBookTypeAdapter;
    private List<BookType> mBookTypes;


    private BookStoreBookAdapter mBookStoreBookAdapter;
    private List<Book> bookList;

    private BookType curType;




    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    initTypeList();

                    break;

                case 2:

                    initBookList();

                    break;


            }
        }
    };

     BookStorePresenter(BookStoreFragment bookStoreFragment){
         super(bookStoreFragment.getContext(),bookStoreFragment.getLifecycle());
         mBookStoreFragment = bookStoreFragment;

    }


    public void init() {

         //无需加载更多
         mBookStoreFragment.getSrlBookList().setEnableLoadMore(false);

         //小说列表下拉刷新事件
         mBookStoreFragment.getSrlBookList().setOnRefreshListener(refreshLayout -> {

             getBooksData();
         });

         getData();


    }


    /**
     * 获取页面数据
     */
    private void getData(){
        mBookStoreFragment.getBinding().pbLoading.setVisibility(View.VISIBLE);
         BookStoreApi.getBookTypeList(URLCONST.nameSpace_biquge, new ResultCallback() {
             @Override
             public void onFinish(Object o, int code) {
                 mBookTypes = (ArrayList<BookType>)o;
                 curType = mBookTypes.get(0);
                 mHandler.sendMessage(mHandler.obtainMessage(1));
                 getBooksData();
             }

             @Override
             public void onError(Exception e) {
                 TextHelper.showText(e.getMessage());

             }
         });


    }

    /**
     * 获取小数列表数据
     */
    private void getBooksData(){

        BookStoreApi.getBookRankList(curType.getUrl(), new ResultCallback() {
            @Override
            public void onFinish(Object o, int code) {
                bookList= (ArrayList<Book>)o;

                mHandler.sendMessage(mHandler.obtainMessage(2));
            }

            @Override
            public void onError(Exception e) {
                TextHelper.showText(e.getMessage());

            }
        });
    }





    /**
     * 初始化类别列表
     */
    private void initTypeList(){
        mBookStoreFragment.getBinding().pbLoading.setVisibility(View.GONE);
        //设置布局管理器
        mLinearLayoutManager = new LinearLayoutManager(mBookStoreFragment.getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBookStoreFragment.getRvTypeList().setLayoutManager(mLinearLayoutManager);
        mBookStoreBookTypeAdapter = new BookStoreBookTypeAdapter(mBookStoreFragment.getActivity(), mBookTypes);
        mBookStoreFragment.getRvTypeList().setAdapter(mBookStoreBookTypeAdapter);

        //点击事件
        mBookStoreBookTypeAdapter.setOnItemClickListener((pos, view) -> {
            curType = mBookTypes.get(pos);
            mBookStoreFragment.getBinding().pbLoading.setVisibility(View.VISIBLE);
            getBooksData();

        });




    }



    /**
     * 初始化小说列表
     */
    private void initBookList(){
        mBookStoreFragment.getBinding().pbLoading.setVisibility(View.GONE);
        //设置布局管理器
        mLinearLayoutManager = new LinearLayoutManager(mBookStoreFragment.getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBookStoreFragment.getRvBookList().setLayoutManager(mLinearLayoutManager);
        mBookStoreBookAdapter = new BookStoreBookAdapter(mBookStoreFragment.getActivity(),bookList);
        mBookStoreFragment.getRvBookList().setAdapter(mBookStoreBookAdapter);

        //点击事件
        mBookStoreBookAdapter.setOnItemClickListener((pos, view) -> {

            Intent intent = new Intent(mBookStoreFragment.getActivity(), BookInfoActivity.class);
            intent.putExtra(APPCONST.BOOK, bookList.get(pos));
            mBookStoreFragment.getActivity().startActivity(intent);


        });

        //刷新动作完成
        mBookStoreFragment.getSrlBookList().finishRefresh();





    }


}
