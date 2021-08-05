package com.zhao.myreader.ui.search;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhao.myreader.R;
import com.zhao.myreader.base.BaseActivity;
import com.zhao.myreader.databinding.ActivitySearchBookBinding;


import me.gujun.android.taggroup.TagGroup;

public class SearchBookActivity extends BaseActivity {



    private SearchBookPrensenter mSearchBookPrensenter;
    private ActivitySearchBookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setStatusBar(R.color.sys_line);
        mSearchBookPrensenter = new SearchBookPrensenter(this);
        mSearchBookPrensenter.enable();
        

    }

    @Override
    public void onBackPressed() {
       if (!mSearchBookPrensenter.onBackPressed()){
           super.onBackPressed();
       }
    }

    public LinearLayout getLlTitleBack() {
        return binding.title.llTitleBack;
    }

    public TextView getTvTitleText() {
        return binding.title.tvTitleText;
    }



    public EditText getEtSearchKey() {
        return binding.etSearchKey;
    }

    public TextView getTvSearchConform() {
        return binding.tvSearchConform;
    }

    public TagGroup getTgSuggestBook() {
        return binding.tgSuggestBook;
    }

    public LinearLayout getLlRefreshSuggestBooks() {
        return binding.llRefreshSuggestBooks;
    }

    public ListView getLvSearchBooksList() {
        return binding.lvSearchBooksList;
    }

    public LinearLayout getLlSuggestBooksView() {
        return binding.llSuggestBooksView;
    }

    public ProgressBar getPbLoading() {
        return binding.pbLoading;
    }

    public ListView getLvHistoryList() {
        return binding.lvHistoryList;
    }

    public LinearLayout getLlClearHistory() {
        return binding.llClearHistory;
    }

    public LinearLayout getLlHistoryView() {
        return binding.llHistoryView;
    }

    public ActivitySearchBookBinding getBinding() {
        return binding;
    }
}
