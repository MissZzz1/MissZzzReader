package com.zhao.myreader.ui.read;


import android.content.Intent;
import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhao.myreader.R;
import com.zhao.myreader.application.MyApplication;
import com.zhao.myreader.base.BaseActivity;
import com.zhao.myreader.databinding.ActivityReadBinding;

import butterknife.ButterKnife;
import butterknife.BindView;

public class ReadActivity extends BaseActivity {




    private ReadPresenter mReadPresenter;
    private ActivityReadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏应用程序的标题栏，即当前activity的label
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏
        binding = ActivityReadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mReadPresenter = new ReadPresenter(this);
        mReadPresenter.enable();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mReadPresenter.onActivityResult(requestCode, resultCode, data);
    }

    public RecyclerView getRvContent() {
        return binding.rvContent;
    }

    public TextView getTvChapterSort() {
        return binding.tvChapterSort;
    }

    public TextView getTvBookList() {
        return binding.tvBookList;
    }



    public SmartRefreshLayout getSrlContent() {
        return binding.srlContent;
    }


    public ProgressBar getPbLoading() {
        return binding.pbLoading;
    }


    public ListView getLvChapterList() {
        return binding.lvChapterList;
    }

    public DrawerLayout getDlReadActivity() {
        return binding.dlReadActivity;
    }

    public LinearLayout getLlChapterListView() {
        return binding.llChapterListView;
    }

    public ActivityReadBinding getBinding() {
        return binding;
    }
}
