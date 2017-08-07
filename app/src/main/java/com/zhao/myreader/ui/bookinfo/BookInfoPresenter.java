package com.zhao.myreader.ui.bookinfo;

import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.zhao.myreader.base.BasePresenter;
import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.greendao.service.BookService;
import com.zhao.myreader.ui.read.ReadActivity;
import com.zhao.myreader.util.StringHelper;
import com.zhao.myreader.util.TextHelper;

/**
 * Created by zhao on 2017/7/27.
 */

public class BookInfoPresenter implements BasePresenter {

    private BookInfoActivity mBookInfoActivity;
    private Book mBook;
    private BookService mBookService;

    public BookInfoPresenter(BookInfoActivity bookInfoActivity){
        mBookInfoActivity  = bookInfoActivity;
        mBookService = new BookService();
    }

    @Override
    public void start() {
        mBook = (Book) mBookInfoActivity.getIntent().getSerializableExtra(APPCONST.BOOK);
        init();


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
        mBookInfoActivity.getLlTitleBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBookInfoActivity.finish();
            }
        });
        mBookInfoActivity.getBtnAddBookcase().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringHelper.isEmpty(mBook.getId())){
                    mBookService.addBook(mBook);
                    TextHelper.showText("成功加入书架");
                    mBookInfoActivity.getBtnAddBookcase().setText("不追了");
                }else {
                    mBookService.deleteBookById(mBook.getId());
                    TextHelper.showText("成功移除书籍");
                    mBookInfoActivity.getBtnAddBookcase().setText("加入书架");
                }

            }
        });
        mBookInfoActivity.getBtnReadBook().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mBookInfoActivity, ReadActivity.class);
                intent.putExtra(APPCONST.BOOK,mBook);
                mBookInfoActivity.startActivity(intent);

            }
        });
        Glide.with(mBookInfoActivity)
                .load(mBook.getImgUrl())
                .into(mBookInfoActivity.getIvBookImg());
    }

    private boolean isBookCollected(){
        Book book = mBookService.findBookByAuthorAndName(mBook.getName(),mBook.getAuthor());
        if (book == null){
            return false;
        }else {
            mBook.setId(book.getId());
            return true;
        }
    }


}
