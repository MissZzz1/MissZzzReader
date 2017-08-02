package com.zhao.myreader.ui.search;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhao.myreader.R;
import com.zhao.myreader.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
    @InjectView(R.id.gv_suggest_book)
    GridView gvSuggestBook;
    @InjectView(R.id.ll_refresh_suggest_books)
    LinearLayout llRefreshSuggestBooks;
    @InjectView(R.id.lv_search_books_list)
    ListView lvSearchBooksList;
    @InjectView(R.id.ll_suggest_books_view)
    LinearLayout llSuggestBooksView;
    @InjectView(R.id.pb_loading)
    ProgressBar pbLoading;

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

    public GridView getGvSuggestBook() {
        return gvSuggestBook;
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
}
