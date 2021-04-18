package com.zhao.myreader.ui.bookinfo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.bumptech.glide.Glide;
import com.zhao.myreader.base.BasePresenter;
import com.zhao.myreader.callback.ResultCallback;
import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.enums.BookSource;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.greendao.service.BookService;
import com.zhao.myreader.ui.read.ReadActivity;
import com.zhao.myreader.util.StringHelper;
import com.zhao.myreader.util.TextHelper;
import com.zhao.myreader.util.crawler.BiQuGeReadUtil;
import com.zhao.myreader.webapi.BookStoreApi;

import static androidx.lifecycle.Lifecycle.State.STARTED;

/**
 * Created by zhao on 2017/7/27.
 */

public class BookInfoPresenter extends  BasePresenter {

    private BookInfoActivity mBookInfoActivity;
    private Book mBook;
    private BookService mBookService;




    private Handler mHandle = new Handler(message -> {
        switch (message.what){
            case 1:
                init();
                break;
        }
        return false;
    });

    public BookInfoPresenter(BookInfoActivity bookInfoActivity){
        super(bookInfoActivity,bookInfoActivity.getLifecycle());
        mBookInfoActivity  = bookInfoActivity;
        mBookService = new BookService();


    }

     @Override
     public void create() {

            mBook = (Book) mBookInfoActivity.getIntent().getSerializableExtra(APPCONST.BOOK);
            if (StringHelper.isEmpty(mBook.getSource()) || BookSource.tianlai.toString().equals(mBook.getSource())){
                init();

            }else if(BookSource.biquge.toString().equals(mBook.getSource())){
                getData();
            }

    }


    private void getData(){
        BookStoreApi.getBookInfo(mBook, new ResultCallback() {
            @Override
            public void onFinish(Object o, int code) {
                mBook = (Book)o;
                mHandle.sendMessage(mHandle.obtainMessage(1));
            }

            @Override
            public void onError(Exception e) {
                TextHelper.showText(e.getMessage());

            }
        });


    }

    private void init(){
        mBookInfoActivity.getTvTitleText().setText(mBook.getName());
        mBookInfoActivity.getTvBookAuthor().setText(mBook.getAuthor());
        mBookInfoActivity.getTvBookDesc().setText(mBook.getDesc());
        mBookInfoActivity.getTvBookType().setText(mBook.getType());
        mBookInfoActivity.getTvBookName().setText(mBook.getName());
        if (isBookCollected()){
            mBookInfoActivity.getBtnAddBookcase().setText("不追了");
        }else {
            mBookInfoActivity.getBtnAddBookcase().setText("加入书架");
        }
        mBookInfoActivity.getLlTitleBack().setOnClickListener(view -> mBookInfoActivity.finish());
        mBookInfoActivity.getBtnAddBookcase().setOnClickListener(view -> {
            if (StringHelper.isEmpty(mBook.getId())){
                mBookService.addBook(mBook);
                TextHelper.showText("成功加入书架");
                mBookInfoActivity.getBtnAddBookcase().setText("不追了");
            }else {
                mBookService.deleteBookById(mBook.getId());
                mBook.setId(null);
                TextHelper.showText("成功移除书籍");
                mBookInfoActivity.getBtnAddBookcase().setText("加入书架");
            }

        });
        mBookInfoActivity.getBtnReadBook().setOnClickListener(view -> {
            Intent intent = new Intent(mBookInfoActivity, ReadActivity.class);
            intent.putExtra(APPCONST.BOOK,mBook);
            mBookInfoActivity.startActivity(intent);

        });
        Glide.with(mBookInfoActivity)
                .load(mBook.getImgUrl())
                .into(mBookInfoActivity.getIvBookImg());
    }

    private boolean isBookCollected(){
        Book book = mBookService.findBookByAuthorAndName(mBook.getName(),mBook.getAuthor(),mBook.getSource());
        if (book == null){
            return false;
        }else {
            mBook.setId(book.getId());
            return true;
        }
    }


}
