package com.zhao.myreader.ui.read;


import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhao.myreader.R;
import com.zhao.myreader.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ReadActivity extends BaseActivity {


    @InjectView(R.id.pb_loading)
    ProgressBar pbLoading;
    @InjectView(R.id.srl_content)
    SmartRefreshLayout srlContent;
    @InjectView(R.id.lv_content)
    ListView lvContent;
    @InjectView(R.id.lv_chapter_list)
    ListView lvChapterList;

    @InjectView(R.id.dl_read_activity)
    DrawerLayout dlReadActivity;
    @InjectView(R.id.ll_chapter_list_view)
    LinearLayout llChapterListView;
    @InjectView(R.id.tv_book_list)
    TextView tvBookList;
    @InjectView(R.id.tv_chapter_sort)
    TextView tvChapterSort;

    private ReadPresenter mReadPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏应用程序的标题栏，即当前activity的label
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏
        setContentView(R.layout.activity_read);
        ButterKnife.inject(this);
        mReadPresenter = new ReadPresenter(this);
        mReadPresenter.start();

    }




    public TextView getTvChapterSort() {
        return tvChapterSort;
    }

    public TextView getTvBookList() {
        return tvBookList;
    }

    public ReadPresenter getmReadPresenter() {
        return mReadPresenter;

    }

    public SmartRefreshLayout getSrlContent() {
        return srlContent;
    }


    public ProgressBar getPbLoading() {
        return pbLoading;
    }

    public ListView getLvContent() {
        return lvContent;
    }

    public ListView getLvChapterList() {
        return lvChapterList;
    }

    public DrawerLayout getDlReadActivity() {
        return dlReadActivity;
    }

    public LinearLayout getLlChapterListView() {
        return llChapterListView;
    }
}
