package com.zhao.myreader.ui.search;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhao.myreader.R;
import com.zhao.myreader.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.gujun.android.taggroup.TagGroup;

public class SearchBookActivity extends BaseActivity {

    @InjectView(R.id.ll_title_back)
    LinearLayout llTitleBack;
    @InjectView(R.id.tv_title_text)
    TextView tvTitleText;
    @InjectView(R.id.system_title)
    LinearLayout systemTitle;
    @InjectView(R.id.et_search_key)
    EditText etSearchKey;
    @InjectView(R.id.tv_search_conform)
    TextView tvSearchConform;

    @InjectView(R.id.ll_refresh_suggest_books)
    LinearLayout llRefreshSuggestBooks;
    @InjectView(R.id.lv_search_books_list)
    ListView lvSearchBooksList;
    @InjectView(R.id.ll_suggest_books_view)
    LinearLayout llSuggestBooksView;
    @InjectView(R.id.pb_loading)
    ProgressBar pbLoading;
    @InjectView(R.id.lv_history_list)
    ListView lvHistoryList;
    @InjectView(R.id.ll_clear_history)
    LinearLayout llClearHistory;
    @InjectView(R.id.ll_history_view)
    LinearLayout llHistoryView;
    @InjectView(R.id.tg_suggest_book)
    TagGroup tgSuggestBook;

    private SearchBookPrensenter mSearchBookPrensenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        ButterKnife.inject(this);
        setStatusBar(R.color.sys_line);
        mSearchBookPrensenter = new SearchBookPrensenter(this);
        mSearchBookPrensenter.start();
    }

    @Override
    public void onBackPressed() {
       if (!mSearchBookPrensenter.onBackPressed()){
           super.onBackPressed();
       }
    }

    public LinearLayout getLlTitleBack() {
        return llTitleBack;
    }

    public TextView getTvTitleText() {
        return tvTitleText;
    }

    public LinearLayout getSystemTitle() {
        return systemTitle;
    }

    public EditText getEtSearchKey() {
        return etSearchKey;
    }

    public TextView getTvSearchConform() {
        return tvSearchConform;
    }

    public TagGroup getTgSuggestBook() {
        return tgSuggestBook;
    }

    public LinearLayout getLlRefreshSuggestBooks() {
        return llRefreshSuggestBooks;
    }

    public ListView getLvSearchBooksList() {
        return lvSearchBooksList;
    }

    public LinearLayout getLlSuggestBooksView() {
        return llSuggestBooksView;
    }

    public ProgressBar getPbLoading() {
        return pbLoading;
    }

    public ListView getLvHistoryList() {
        return lvHistoryList;
    }

    public LinearLayout getLlClearHistory() {
        return llClearHistory;
    }

    public LinearLayout getLlHistoryView() {
        return llHistoryView;
    }
}
