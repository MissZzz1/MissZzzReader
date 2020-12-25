package com.zhao.myreader.ui.home.bookcase;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import com.zhao.myreader.custom.DragSortGridView;
import com.zhao.myreader.databinding.FragmentBookcaseBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookcaseFragment extends Fragment {






    private BookcasePresenter mBookcasePresenter;
    private FragmentBookcaseBinding binding;

    public BookcaseFragment() {
        mBookcasePresenter = new BookcasePresenter(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookcaseBinding.inflate(inflater,container,false);
        mBookcasePresenter.enable();


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }



    public LinearLayout getLlNoDataTips() {
        return binding.llNoDataTips;
    }

    public DragSortGridView getGvBook() {
        return binding.gvBook;
    }

    public SmartRefreshLayout getSrlContent() {
        return binding.srlContent;
    }

    public FragmentBookcaseBinding getBinding() {
        return binding;
    }
}
