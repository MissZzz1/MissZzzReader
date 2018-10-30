package com.zhao.myreader.ui.font;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhao.myreader.R;
import com.zhao.myreader.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.BindView;

public class FontsActivity extends BaseActivity {

    @BindView(R.id.ll_title_back)
    LinearLayout llTitleBack;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.system_title)
    LinearLayout systemTitle;
    @BindView(R.id.lv_fonts)
    ListView lvFonts;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    private FontsPresenter mFontsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fonts);
        ButterKnife.bind(this);
        setStatusBar(R.color.sys_line);
        mFontsPresenter = new FontsPresenter(this);
        mFontsPresenter.start();
    }

    public ProgressBar getPbLoading() {
        return pbLoading;
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

    public ListView getLvFonts() {
        return lvFonts;
    }
}
