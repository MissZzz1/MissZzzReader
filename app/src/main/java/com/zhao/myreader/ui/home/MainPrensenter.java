package com.zhao.myreader.ui.home;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.zhao.myreader.base.BasePresenter;
import com.zhao.myreader.ui.home.bbs.BBSFragment;
import com.zhao.myreader.ui.home.bookcase.BookcaseFragment;
import com.zhao.myreader.ui.home.bookstore.BookStoreFragment;
import com.zhao.myreader.ui.search.SearchBookActivity;

import java.util.ArrayList;

/**
 * Created by zhao on 2017/7/25.
 */

public class MainPrensenter implements BasePresenter {

    private MainActivity mMainActivity;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] tabTitle = {"社区","书架","书城"};

    public MainPrensenter(MainActivity mainActivity){
        mMainActivity = mainActivity;
    }

    @Override
    public void start() {
        init();
        mMainActivity.getIvSearch().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mMainActivity, SearchBookActivity.class);
                mMainActivity.startActivity(intent);
            }
        });

    }

    /**
     * 初始化
     */
    private void init(){
        mFragments.add(new BBSFragment());
        mFragments.add(new BookcaseFragment());
        mFragments.add(new BookStoreFragment());
        mMainActivity.getVpContent().setAdapter(new FragmentPagerAdapter(mMainActivity.getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {

                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitle[position];
            }

        });
        mMainActivity.getTlTabMenu().setupWithViewPager( mMainActivity.getVpContent());
        mMainActivity.getVpContent().setCurrentItem(1);
    }
}
