package com.zhao.myreader.ui.font;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhao.myreader.R;
import com.zhao.myreader.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FontsActivity extends BaseActivity {

    @InjectView(R.id.ll_title_back)
    LinearLayout llTitleBack;
    @InjectView(R.id.tv_title_text)
    TextView tvTitleText;
    @InjectView(R.id.system_title)
    LinearLayout systemTitle;
    @InjectView(R.id.lv_fonts)
    ListView lvFonts;
    @InjectView(R.id.pb_loading)
    ProgressBar pbLoading;

    private FontsPresenter mFontsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fonts);
        ButterKnife.inject(this);
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
