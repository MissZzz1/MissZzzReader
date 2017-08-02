package com.zhao.myreader.ui.bookinfo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhao.myreader.R;
import com.zhao.myreader.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BookInfoActivity extends BaseActivity {

    @InjectView(R.id.ll_title_back)
    LinearLayout llTitleBack;
    @InjectView(R.id.tv_title_text)
    TextView tvTitleText;
    @InjectView(R.id.system_title)
    LinearLayout systemTitle;
    @InjectView(R.id.iv_book_img)
    ImageView ivBookImg;
    @InjectView(R.id.tv_book_name)
    TextView tvBookName;
    @InjectView(R.id.tv_book_author)
    TextView tvBookAuthor;
    @InjectView(R.id.tv_book_type)
    TextView tvBookType;
    @InjectView(R.id.tv_book_desc)
    TextView tvBookDesc;
    @InjectView(R.id.btn_add_bookcase)
    Button btnAddBookcase;
    @InjectView(R.id.btn_read_book)
    Button btnReadBook;

    private BookInfoPresenter mBookInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        ButterKnife.inject(this);
        setStatusBar(R.color.sys_line);
        mBookInfoPresenter = new BookInfoPresenter(this);
        mBookInfoPresenter.start();

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

    public ImageView getIvBookImg() {
        return ivBookImg;
    }

    public TextView getTvBookName() {
        return tvBookName;
    }

    public TextView getTvBookAuthor() {
        return tvBookAuthor;
    }

    public TextView getTvBookType() {
        return tvBookType;
    }

    public TextView getTvBookDesc() {
        return tvBookDesc;
    }

    public Button getBtnAddBookcase() {
        return btnAddBookcase;
    }

    public Button getBtnReadBook() {
        return btnReadBook;
    }
}
