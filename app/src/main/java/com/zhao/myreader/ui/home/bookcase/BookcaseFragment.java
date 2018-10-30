package com.zhao.myreader.ui.home.bookcase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhao.myreader.R;
import com.zhao.myreader.custom.DragSortGridView;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookcaseFragment extends Fragment {


    @BindView(R.id.srl_content)
    SmartRefreshLayout srlContent;
    @BindView(R.id.ll_no_data_tips)
    LinearLayout llNoDataTips;
    @BindView(R.id.gv_book)
    DragSortGridView gvBook;

    Unbinder unbinder;

    private BookcasePresenter mBookcasePresenter;

    public BookcaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookcase, container, false);
        unbinder = ButterKnife.bind(this, view);
        mBookcasePresenter = new BookcasePresenter(this);
        mBookcasePresenter.start();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBookcasePresenter.getData();
    }

    public LinearLayout getLlNoDataTips() {
        return llNoDataTips;
    }

    public DragSortGridView getGvBook() {
        return gvBook;
    }

    public SmartRefreshLayout getSrlContent() {
        return srlContent;
    }
}
