package com.zhao.myreader.ui.home.bookstore;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.zhao.myreader.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookStoreFragment extends Fragment {

    @BindView(R.id.rv_type_list)
    RecyclerView rvTypeList;
    @BindView(R.id.rv_book_list)
    RecyclerView rvBookList;


    private Unbinder unbinder;


    private BookStorePresenter mBookStorePresenter;


    public BookStoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_store, container, false);
        unbinder = ButterKnife.bind(this, view);
        mBookStorePresenter = new BookStorePresenter(this);
        mBookStorePresenter.start();
        return view;

    }

    public RecyclerView getRvTypeList() {
        return rvTypeList;
    }

    public RecyclerView getRvBookList() {
        return rvBookList;
    }
}
