package com.zhao.myreader.ui.bookinfo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhao.myreader.R;
import com.zhao.myreader.base.BaseActivity;
import com.zhao.myreader.databinding.ActivityBookInfoBinding;


public class BookInfoActivity extends BaseActivity {



    private BookInfoPresenter mBookInfoPresenter;
    private ActivityBookInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBookInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setStatusBar(R.color.sys_line);
        mBookInfoPresenter = new BookInfoPresenter(this);
        mBookInfoPresenter.start();



    }

    public LinearLayout getLlTitleBack() {
        return binding.title.llTitleBack;
    }

    public TextView getTvTitleText() {
        return binding.title.tvTitleText;
    }


    public ImageView getIvBookImg() {
        return binding.ivBookImg;
    }

    public TextView getTvBookName() {
        return binding.tvBookName;
    }

    public TextView getTvBookAuthor() {
        return binding.tvBookAuthor;
    }

    public TextView getTvBookType() {
        return binding.tvBookType;
    }

    public TextView getTvBookDesc() {
        return binding.tvBookDesc;
    }

    public Button getBtnAddBookcase() {
        return binding.btnAddBookcase;
    }

    public Button getBtnReadBook() {
        return binding.btnReadBook;
    }

    public ActivityBookInfoBinding getBinding() {
        return binding;
    }
}
