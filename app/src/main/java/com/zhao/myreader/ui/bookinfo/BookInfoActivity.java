package com.zhao.myreader.ui.bookinfo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhao.myreader.R;
import com.zhao.myreader.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.BindView;

public class BookInfoActivity extends BaseActivity {

    @BindView(R.id.ll_title_back)
    LinearLayout llTitleBack;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.system_title)
    LinearLayout systemTitle;
    @BindView(R.id.iv_book_img)
    ImageView ivBookImg;
    @BindView(R.id.tv_book_name)
    TextView tvBookName;
    @BindView(R.id.tv_book_author)
    TextView tvBookAuthor;
    @BindView(R.id.tv_book_type)
    TextView tvBookType;
    @BindView(R.id.tv_book_desc)
    TextView tvBookDesc;
    @BindView(R.id.btn_add_bookcase)
    Button btnAddBookcase;
    @BindView(R.id.btn_read_book)
    Button btnReadBook;

    private BookInfoPresenter mBookInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        ButterKnife.bind(this);
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
