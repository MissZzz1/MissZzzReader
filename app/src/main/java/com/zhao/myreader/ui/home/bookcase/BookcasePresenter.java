package com.zhao.myreader.ui.home.bookcase;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhao.myreader.R;
import com.zhao.myreader.base.BasePresenter;
import com.zhao.myreader.callback.ResultCallback;
import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.creator.DialogCreator;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.greendao.entity.Chapter;
import com.zhao.myreader.greendao.service.BookService;
import com.zhao.myreader.greendao.service.ChapterService;
import com.zhao.myreader.ui.read.ReadActivity;
import com.zhao.myreader.ui.search.SearchBookActivity;
import com.zhao.myreader.util.StringHelper;
import com.zhao.myreader.webapi.CommonApi;

import java.util.ArrayList;

/**
 * Created by zhao on 2017/7/25.
 */

public class BookcasePresenter implements BasePresenter {

    private BookcaseFragment mBookcaseFragment;
    private ArrayList<Book> mBooks;//书目数组
    private BookcaseAdapter mBookcaseAdapter;
    private BookService mBookService;
    private ChapterService mChapterService;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mBookcaseAdapter.notifyDataSetChanged();
                    mBookcaseFragment.getSrlContent().finishRefresh();
                    break;

            }
        }
    };

    public BookcasePresenter(BookcaseFragment bookcaseFragment) {
        mBookcaseFragment = bookcaseFragment;
        mBookService = new BookService();
        mChapterService = new ChapterService();
    }

    @Override
    public void start() {
        mBookcaseFragment.getSrlContent().setEnableHeaderTranslationContent(false);
        mBookcaseFragment.getSrlContent().setEnableLoadmore(false);
        mBookcaseFragment.getSrlContent().setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initNoReadNum();
            }
        });
        mBookcaseFragment.getGvBook().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mBookcaseFragment.getContext(), ReadActivity.class);
                intent.putExtra(APPCONST.BOOK, mBooks.get(i));
                mBooks.get(i).setNoReadNum(0);
                mBookService.updateEntity(mBooks.get(i));
                mBookcaseFragment.startActivity(intent);

            }
        });
        mBookcaseFragment.getLlNoDataTips().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mBookcaseFragment.getContext(), SearchBookActivity.class);
                mBookcaseFragment.startActivity(intent);
            }
        });
        mBookcaseFragment.getGvBook().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                DialogCreator.createCommonDialog(mBookcaseFragment.getContext(), "删除书籍", "确定删除《" + mBooks.get(position).getName() + "》及其所有缓存吗？",
                        true, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mBookService.deleteBook(mBooks.get(position));
                                mBooks.remove(position);
                                init();
                                dialogInterface.dismiss();

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                return true;
            }
        });
    }

    private void init() {
        initBook();
        if (mBooks == null || mBooks.size() == 0) {
            mBookcaseFragment.getGvBook().setVisibility(View.GONE);
            mBookcaseFragment.getLlNoDataTips().setVisibility(View.VISIBLE);
        } else {
            mBookcaseAdapter = new BookcaseAdapter(mBookcaseFragment.getContext(), R.layout.gridview_book_item, mBooks);
            mBookcaseFragment.getGvBook().setAdapter(mBookcaseAdapter);
            mBookcaseFragment.getLlNoDataTips().setVisibility(View.GONE);
            mBookcaseFragment.getGvBook().setVisibility(View.VISIBLE);
        }
    }


    public void getData() {
        init();
        initNoReadNum();
    }

    private void initBook() {
        mBooks = (ArrayList<Book>) mBookService.getAllBooks();
        for (int i = 0; i < mBooks.size(); i++) {
            if (mBooks.get(i).getSortCode() != i + 1) {
                mBooks.get(i).setSortCode(i + 1);
                mBookService.updateEntity(mBooks.get(i));
            }
        }
    }

    private void initNoReadNum() {
        for (final Book book : mBooks) {
            CommonApi.getBookChapters(book.getChapterUrl(), new ResultCallback() {
                @Override
                public void onFinish(Object o, int code) {
                    final ArrayList<Chapter> chapters = (ArrayList<Chapter>) o;
                    int noReadNum = chapters.size() - book.getChapterTotalNum();
                    if (noReadNum > 0) {
                        book.setNoReadNum(noReadNum);
                        mHandler.sendMessage(mHandler.obtainMessage(1));
                    } else {
                        book.setNoReadNum(0);
                    }
                    mBookService.updateEntity(book);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }

    private void setThemeColor(int colorPrimary, int colorPrimaryDark) {
//        mToolbar.setBackgroundResource(colorPrimary);
        mBookcaseFragment.getSrlContent().setPrimaryColorsId(colorPrimary, android.R.color.white);
        if (Build.VERSION.SDK_INT >= 21) {
            mBookcaseFragment.getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(mBookcaseFragment.getContext(), colorPrimaryDark));
        }
    }

}
