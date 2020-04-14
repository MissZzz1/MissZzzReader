package com.zhao.myreader.ui.read;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;


import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import com.zhao.myreader.R;
import com.zhao.myreader.application.MyApplication;
import com.zhao.myreader.application.SysManager;
import com.zhao.myreader.base.BaseActivity;
import com.zhao.myreader.base.BasePresenter;
import com.zhao.myreader.callback.ResultCallback;
import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.creator.DialogCreator;
import com.zhao.myreader.entity.Setting;
import com.zhao.myreader.enums.BookSource;
import com.zhao.myreader.enums.Font;
import com.zhao.myreader.enums.Language;
import com.zhao.myreader.enums.ReadStyle;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.greendao.entity.Chapter;
import com.zhao.myreader.greendao.service.BookService;
import com.zhao.myreader.greendao.service.ChapterService;
import com.zhao.myreader.ui.font.FontsActivity;
import com.zhao.myreader.util.BrightUtil;
import com.zhao.myreader.util.DateHelper;
import com.zhao.myreader.util.StringHelper;
import com.zhao.myreader.util.TextHelper;
import com.zhao.myreader.webapi.CommonApi;

import java.util.ArrayList;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhao on 2017/7/27.
 */

public class ReadPresenter implements BasePresenter {



    private ReadActivity mReadActivity;
    private Book mBook;
    private ArrayList<Chapter> mChapters = new ArrayList<>();
    private ArrayList<Chapter> mInvertedOrderChapters = new ArrayList<>();
    private ChapterService mChapterService;
    private BookService mBookService;
    //    private ChapterContentAdapter mChapterContentAdapter;
    private ReadContentAdapter mReadContentAdapter;
    private ChapterTitleAdapter mChapterTitleAdapter;
    private Setting mSetting;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean isFirstInit = true;

    private boolean settingChange;//是否是设置改变
    private boolean autoScrollOpening = false;//是否开启自动滑动


    private float pointX;
    private float pointY;
    private float scrolledX;
    private float scrolledY;

    private long lastOnClickTime;//上次点击时间
    private long doubleOnClickConfirmTime = 200;//双击确认时间

    private float settingOnClickValidFrom;
    private float settingOnClickValidTo;


    private Dialog mSettingDialog;//设置视图
    private Dialog mSettingDetailDialog;//详细设置视图

    private int curSortflag = 0; //0正序  1倒序

    private int curCacheChapterNum = 0;//缓存章节数


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    init();
                    break;
                case 2:
                    mReadActivity.getPbLoading().setVisibility(View.GONE);
                    mReadActivity.getSrlContent().finishLoadMore();
                    break;
                case 3:
                    int position = msg.arg1;
                    mReadActivity.getRvContent().scrollToPosition(position);
                    if (position >= mChapters.size() - 1) {
                        delayTurnToChapter(position);
                    }
                    mReadActivity.getPbLoading().setVisibility(View.GONE);
                    break;
                case 4:
                    position = msg.arg1;
                    mReadActivity.getRvContent().scrollToPosition(position);
                    if (mBook.getHisttoryChapterNum() < position) {
                        delayTurnToChapter(position);
                    }
                    mReadActivity.getPbLoading().setVisibility(View.GONE);
                    break;
                case 5:
                    saveLastChapterReadPosition(msg.arg1);
                    break;
                case 6:
                    mReadActivity.getRvContent().scrollBy(0, mBook.getLastReadPosition());
                    mBook.setLastReadPosition(0);
                    if (!StringHelper.isEmpty(mBook.getId())) {
                        mBookService.updateEntity(mBook);
                    }
                    break;
                case 7:
                    if (mLinearLayoutManager != null) {
                        mReadActivity.getRvContent().scrollBy(0, 2);
                    }
                    break;
                case 8:
                    showSettingView();
                    break;
                case 9:
                    updateDownloadProgress((TextView)msg.obj);
                    break;
            }
        }
    };


    public ReadPresenter(ReadActivity readActivity) {
        mReadActivity = readActivity;
        mBookService = new BookService();
        mChapterService = new ChapterService();
        mSetting = SysManager.getSetting();
    }


    @Override
    public void start() {
        if (mSetting.isDayStyle()) {
            mReadActivity.getDlReadActivity().setBackgroundResource(mSetting.getReadBgColor());
        } else {
            mReadActivity.getDlReadActivity().setBackgroundResource(R.color.sys_night_bg);
        }
        if (!mSetting.isBrightFollowSystem()) {
            BrightUtil.setBrightness(mReadActivity, mSetting.getBrightProgress());
        }
        mBook = (Book) mReadActivity.getIntent().getSerializableExtra(APPCONST.BOOK);
        if (StringHelper.isEmpty(mBook.getSource())){
            mBook.setSource(BookSource.tianlai.toString());
            mBookService.updateEntity(mBook);
        }
        settingOnClickValidFrom = BaseActivity.height / 4;
        settingOnClickValidTo = BaseActivity.height / 4 * 3;
        mReadActivity.getSrlContent().setEnableLoadMore(false);
        mReadActivity.getSrlContent().setEnableRefresh(false);
        mReadActivity.getSrlContent().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                settingChange = true;
                getData();
            }
        });
        mReadActivity.getPbLoading().setVisibility(View.VISIBLE);


        mReadActivity.getLvChapterList().setOnItemClickListener((adapterView, view, i, l) -> {
            //关闭侧滑菜单
            mReadActivity.getDlReadActivity().closeDrawer(GravityCompat.START);
            final int position;
            if (curSortflag == 0) {
                position = i;
            } else {
                position = mChapters.size() - 1 - i;
            }
            if (StringHelper.isEmpty(mChapters.get(position).getContent())) {
                mReadActivity.getPbLoading().setVisibility(View.VISIBLE);
                CommonApi.getChapterContent(mChapters.get(position).getUrl(), new ResultCallback() {
                    @Override
                    public void onFinish(Object o, int code) {
                        mChapters.get(position).setContent((String) o);
                        mChapterService.saveOrUpdateChapter(mChapters.get(position));
                        mHandler.sendMessage(mHandler.obtainMessage(4, position, 0));
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            } else {
//                    mReadActivity.getLvContent().setSelection(position);
                mReadActivity.getRvContent().scrollToPosition(position);
                if (position > mBook.getHisttoryChapterNum()) {
                    delayTurnToChapter(position);
                }
            }

        });
        mReadActivity.getRvContent().addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, final int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //页面初始化的时候不要执行
                if (!isFirstInit) {
                    MyApplication.getApplication().newThread(new Runnable() {
                        @Override
                        public void run() {
                            saveLastChapterReadPosition(dy);
                        }
                    });
                } else {
                    isFirstInit = false;
                }
            }
        });

        mReadActivity.getTvChapterSort().setOnClickListener(view -> {
            if (curSortflag == 0) {//当前正序
                mReadActivity.getTvChapterSort().setText(mReadActivity.getString(R.string.positive_sort));
                curSortflag = 1;
                changeChapterSort();
            } else {//当前倒序
                mReadActivity.getTvChapterSort().setText(mReadActivity.getString(R.string.inverted_sort));
                curSortflag = 0;
                changeChapterSort();
            }
        });

        //关闭手势滑动
        mReadActivity.getDlReadActivity().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mReadActivity.getDlReadActivity().addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //打开手势滑动
                mReadActivity.getDlReadActivity().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //关闭手势滑动
                mReadActivity.getDlReadActivity().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        getData();
    }

    /**
     * 保存最后阅读章节的进度
     *
     * @param dy
     */
    private void saveLastChapterReadPosition(int dy) {
        if (mLinearLayoutManager == null) return;

        if (mLinearLayoutManager.findFirstVisibleItemPosition() != mLinearLayoutManager.findLastVisibleItemPosition()
                || dy == 0) {
            mBook.setLastReadPosition(0);
        } else {
            mBook.setLastReadPosition(mBook.getLastReadPosition() + dy);
        }
        mBook.setHisttoryChapterNum(mLinearLayoutManager.findLastVisibleItemPosition());
        if (!StringHelper.isEmpty(mBook.getId())) {
            mBookService.updateEntity(mBook);
        }
    }

    /**
     * 初始化阅读界面点击事件
     */
    private void initReadViewOnClick() {
        mReadContentAdapter.setmOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pointY = event.getRawY();
                return false;
            }
        });

        mReadContentAdapter.setmOnClickItemListener(new ReadContentAdapter.OnClickItemListener() {
            @Override
            public void onClick(View view, final int positon) {
                if (pointY > settingOnClickValidFrom && pointY < settingOnClickValidTo) {
                    autoScrollOpening = false;
//                    int progress = mReadActivity.getLvContent().getLastVisiblePosition() * 100 / (mChapters.size() - 1);
                    long curOnClickTime = DateHelper.getLongDate();
                    if (curOnClickTime - lastOnClickTime < doubleOnClickConfirmTime) {
                        autoScroll();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(doubleOnClickConfirmTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (!autoScrollOpening) {
                                    mHandler.sendMessage(mHandler.obtainMessage(8));
                                }

                            }
                        }).start();
                    }
                    lastOnClickTime = curOnClickTime;
                } else if (pointY > settingOnClickValidTo) {

//                    mReadActivity.getLvContent().scrollListBy(BaseActivity.height);
                    mReadActivity.getRvContent().scrollBy(0, BaseActivity.height);
                } else if (pointY < settingOnClickValidFrom) {

//                    mReadActivity.getLvContent().scrollListBy(-BaseActivity.height);
                    mReadActivity.getRvContent().scrollBy(0, -BaseActivity.height);
                }
            }
        });
    }

    /**
     * 显示设置视图
     */
    private void showSettingView() {
        autoScrollOpening = false;
        if (mSettingDialog != null) {
            mSettingDialog.show();
        } else {
            int progress = mLinearLayoutManager.findLastVisibleItemPosition() * 100 / (mChapters.size() - 1);
            mSettingDialog = DialogCreator.createReadSetting(mReadActivity, mSetting.isDayStyle(), progress, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//返回
                            mReadActivity.finish();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//上一章
//                            int curPosition = mReadActivity.getLvContent().getLastVisiblePosition();
                            int curPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                            if (curPosition > 0) {
                                mReadActivity.getRvContent().scrollToPosition(curPosition - 1);
                            }
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//下一章
//                            int curPosition = mReadActivity.getLvContent().getLastVisiblePosition();
                            int curPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                            if (curPosition < mChapters.size() - 1) {
                                mReadActivity.getRvContent().scrollToPosition(curPosition + 1);
                                delayTurnToChapter(curPosition + 1);
                            }
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//目录
                            initChapterTitleList();
                            mReadActivity.getDlReadActivity().openDrawer(GravityCompat.START);
                            mSettingDialog.dismiss();

                        }
                    }, new DialogCreator.OnClickNightAndDayListener() {
                        @Override
                        public void onClick(Dialog dialog, View view, boolean isDayStyle) {//日夜切换

                            changeNightAndDaySetting(isDayStyle);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//设置
                            showSettingDetailView();
                        }
                    }, new SeekBar.OnSeekBarChangeListener() {//阅读进度
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            mReadActivity.getPbLoading().setVisibility(View.VISIBLE);
                            final int chapterNum = (mChapters.size() - 1) * i / 100;
                            getChapterContent(mChapters.get(chapterNum), new ResultCallback() {
                                @Override
                                public void onFinish(Object o, int code) {
                                    mChapters.get(chapterNum).setContent((String) o);
                                    mChapterService.saveOrUpdateChapter(mChapters.get(chapterNum));
                                    mHandler.sendMessage(mHandler.obtainMessage(4, chapterNum, 0));
                                }

                                @Override
                                public void onError(Exception e) {
                                    mHandler.sendMessage(mHandler.obtainMessage(1));
                                }
                            });

                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    }
                    , null, new DialogCreator.OnClickDownloadAllChapterListener() {//缓存整本
                        @Override
                        public void onClick(Dialog dialog, View view, TextView tvDownloadProgress) {
                            if (StringHelper.isEmpty(mBook.getId())){
                                addBookToCaseAndDownload(tvDownloadProgress);
                            }else {
                                getAllChapterData(tvDownloadProgress);
                            }

                        }
                    });
        }

    }

    /**
     * 添加到书架并缓存整本
     * @param tvDownloadProgress
     */
    private void addBookToCaseAndDownload(final TextView tvDownloadProgress){
        DialogCreator.createCommonDialog(mReadActivity, mReadActivity.getString(R.string.tip), mReadActivity.getString(R.string.download_no_add_tips), true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBookService.addBook(mBook);
                getAllChapterData(tvDownloadProgress);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 显示详细设置视图
     */
    private void showSettingDetailView() {
        mSettingDialog.dismiss();
        if (mSettingDetailDialog != null) {
            mSettingDetailDialog.show();
        } else {
            mSettingDetailDialog = DialogCreator.createReadDetailSetting(mReadActivity, mSetting,
                    new DialogCreator.OnReadStyleChangeListener() {
                        @Override
                        public void onChange(ReadStyle readStyle) {
                            changeStyle(readStyle);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reduceTextSize();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            increaseTextSize();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSetting.getLanguage() == Language.simplified) {
                                mSetting.setLanguage(Language.traditional);
                            } else {
                                mSetting.setLanguage(Language.simplified);
                            }
                            SysManager.saveSetting(mSetting);
                            settingChange = true;
                            init();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mReadActivity, FontsActivity.class);
                            mReadActivity.startActivityForResult(intent, APPCONST.REQUEST_FONT);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            autoScroll();
                            mSettingDetailDialog.dismiss();
                        }
                    });
        }
    }

    /**
     * 延迟跳转章节(防止跳到章节尾部)
     */
    private void delayTurnToChapter(final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                    mHandler.sendMessage(mHandler.obtainMessage(4, position, 0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 延迟跳转章节位置
     */
    private void delayTurnToLastChapterReadPosion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    mHandler.sendMessage(mHandler.obtainMessage(6));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    /**
     * 字体结果回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case APPCONST.REQUEST_FONT:
                if (resultCode == RESULT_OK) {
                    mSetting.setFont((Font) data.getSerializableExtra(APPCONST.FONT));
                    settingChange = true;
                    initContent();
                }
                break;
        }
    }


    /**
     * 初始化
     */
    private void init() {
        initContent();
        initChapterTitleList();
    }

    /**
     * 初始化主内容视图
     */
    private void initContent() {
        if (mSetting.isDayStyle()) {
            mReadActivity.getDlReadActivity().setBackgroundResource(mSetting.getReadBgColor());
        } else {
            mReadActivity.getDlReadActivity().setBackgroundResource(R.color.sys_night_bg);
        }
        if (mReadContentAdapter == null) {
//            mChapterContentAdapter = new ChapterContentAdapter(mReadActivity, R.layout.listview_chapter_content_item, mChapters, mBook);
//            mReadActivity.getLvContent().setAdapter(mChapterContentAdapter);
            //设置布局管理器
            mLinearLayoutManager = new LinearLayoutManager(mReadActivity);
            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mReadActivity.getRvContent().setLayoutManager(mLinearLayoutManager);
            mReadContentAdapter = new ReadContentAdapter(mReadActivity, R.layout.listview_chapter_content_item, mChapters, mBook);
            initReadViewOnClick();
            mReadActivity.getRvContent().setAdapter(mReadContentAdapter);
        } else {
            mReadContentAdapter.notifyDataSetChangedBySetting();
        }
        if (!settingChange) {
//            mReadActivity.getLvContent().setSelection(mBook.getHisttoryChapterNum());
            mReadActivity.getRvContent().scrollToPosition(mBook.getHisttoryChapterNum());
            delayTurnToLastChapterReadPosion();

        } else {
            settingChange = false;
        }
        mReadActivity.getPbLoading().setVisibility(View.GONE);
        mReadActivity.getSrlContent().finishLoadMore();

    }

    /**
     * 改变章节列表排序（正倒序）
     */
    private void changeChapterSort() {
        //设置布局管理器
        if (curSortflag == 0) {
            mChapterTitleAdapter = new ChapterTitleAdapter(mReadActivity, R.layout.listview_chapter_title_item, mChapters);
        } else {
            mChapterTitleAdapter = new ChapterTitleAdapter(mReadActivity, R.layout.listview_chapter_title_item, mInvertedOrderChapters);
        }
        mReadActivity.getLvChapterList().setAdapter(mChapterTitleAdapter);

    }

    /**
     * 初始化章节目录视图
     */
    private void initChapterTitleList() {
        if (mSetting.isDayStyle()) {
            mReadActivity.getTvBookList().setTextColor(mReadActivity.getResources().getColor(mSetting.getReadWordColor()));
            mReadActivity.getTvChapterSort().setTextColor(mReadActivity.getResources().getColor(mSetting.getReadWordColor()));
        } else {
            mReadActivity.getTvBookList().setTextColor(mReadActivity.getResources().getColor(R.color.sys_night_word));
            mReadActivity.getTvChapterSort().setTextColor(mReadActivity.getResources().getColor(R.color.sys_night_word));
        }
        if (mSetting.isDayStyle()) {
            mReadActivity.getLlChapterListView().setBackgroundResource(mSetting.getReadBgColor());
        } else {
            mReadActivity.getLlChapterListView().setBackgroundResource(R.color.sys_night_bg);
        }
        int selectedPostion, curChapterPosition;

        //设置布局管理器
        if (curSortflag == 0) {
            mChapterTitleAdapter = new ChapterTitleAdapter(mReadActivity, R.layout.listview_chapter_title_item, mChapters);
//            curChapterPosition = mReadActivity.getRvContent().getLastVisiblePosition();
            curChapterPosition = mLinearLayoutManager.findLastVisibleItemPosition();
            selectedPostion = curChapterPosition - 5;
            if (selectedPostion < 0) selectedPostion = 0;
            if (mChapters.size() - 1 - curChapterPosition < 5) selectedPostion = mChapters.size();
            mChapterTitleAdapter.setCurChapterPosition(curChapterPosition);
        } else {
            mChapterTitleAdapter = new ChapterTitleAdapter(mReadActivity, R.layout.listview_chapter_title_item, mInvertedOrderChapters);
//            curChapterPosition = mChapters.size() - 1 - mReadActivity.getLvContent().getLastVisiblePosition();
            curChapterPosition = mChapters.size() - 1 - mLinearLayoutManager.findLastVisibleItemPosition();
            selectedPostion = curChapterPosition - 5;
            if (selectedPostion < 0) selectedPostion = 0;
            if (mChapters.size() - 1 - curChapterPosition < 5) selectedPostion = mChapters.size();
            mChapterTitleAdapter.setCurChapterPosition(curChapterPosition);
        }
        mReadActivity.getLvChapterList().setAdapter(mChapterTitleAdapter);
        mReadActivity.getLvChapterList().setSelection(selectedPostion);

    }


    /**
     * 章节数据网络同步
     */
    private void getData() {

        CommonApi.getBookChapters(mBook, new ResultCallback() {
            @Override
            public void onFinish(Object o, int code) {
                mChapters = (ArrayList<Chapter>) mChapterService.findBookAllChapterByBookId(mBook.getId());
                final ArrayList<Chapter> chapters = (ArrayList<Chapter>) o;
                mBook.setChapterTotalNum(chapters.size());
                if (!StringHelper.isEmpty(mBook.getId())) {
                    mBookService.updateEntity(mBook);
                }
                updateAllOldChapterData(chapters);
                mInvertedOrderChapters.clear();
                mInvertedOrderChapters.addAll(mChapters);
                Collections.reverse(mInvertedOrderChapters);
                if (mChapters.size() == 0) {
                    TextHelper.showLongText("该书查询不到任何章节");
                    mReadActivity.getPbLoading().setVisibility(View.GONE);
                    settingChange = false;
                } else {
                    if (mBook.getHisttoryChapterNum() < 0) mBook.setHisttoryChapterNum(0);
                    else if (mBook.getHisttoryChapterNum() >= chapters.size())
                        mBook.setHisttoryChapterNum(chapters.size() - 1);
                    getChapterContent(mChapters.get(mBook.getHisttoryChapterNum()), new ResultCallback() {
                        @Override
                        public void onFinish(Object o, int code) {
                            mChapters.get(mBook.getHisttoryChapterNum()).setContent((String) o);
                            mChapterService.saveOrUpdateChapter(mChapters.get(mBook.getHisttoryChapterNum()));
                            mHandler.sendMessage(mHandler.obtainMessage(1));
//                        getAllChapterData();
                        }

                        @Override
                        public void onError(Exception e) {
                            mHandler.sendMessage(mHandler.obtainMessage(1));

                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
//                settingChange = true;
                mChapters = (ArrayList<Chapter>) mChapterService.findBookAllChapterByBookId(mBook.getId());
                mHandler.sendMessage(mHandler.obtainMessage(1));
            }
        });
    }

    /**
     * 更新所有章节
     *
     * @param newChapters
     */
    private void updateAllOldChapterData(ArrayList<Chapter> newChapters) {
        int i;
        for (i = 0; i < mChapters.size() && i < newChapters.size(); i++) {
            Chapter oldChapter = mChapters.get(i);
            Chapter newChapter = newChapters.get(i);
            if (!oldChapter.getTitle().equals(newChapter.getTitle())) {
                oldChapter.setTitle(newChapter.getTitle());
                oldChapter.setUrl(newChapter.getUrl());
                oldChapter.setContent(null);
                mChapterService.updateEntity(oldChapter);
            }
        }
        if (mChapters.size() < newChapters.size()) {
            int start = mChapters.size();
            for (int j = mChapters.size(); j < newChapters.size(); j++) {
                newChapters.get(j).setId(StringHelper.getStringRandom(25));
                newChapters.get(j).setBookId(mBook.getId());
                mChapters.add(newChapters.get(j));
//                mChapterService.addChapter(newChapters.get(j));
            }
            mChapterService.addChapters(mChapters.subList(start,mChapters.size()));
        } else if (mChapters.size() > newChapters.size()) {
            for (int j = newChapters.size(); j < mChapters.size(); j++) {
                mChapterService.deleteEntity(mChapters.get(j));
            }
            mChapters.subList(0, newChapters.size());
        }
    }

    /**
     * 缓存所有章节
     */
    private void getAllChapterData(final TextView tvDownloadProgress) {
        curCacheChapterNum = 0;
        MyApplication.getApplication().newThread(new Runnable() {
            @Override
            public void run() {
                for (final Chapter chapter : mChapters) {
                    if (StringHelper.isEmpty(chapter.getContent())) {
                        getChapterContent(chapter, new ResultCallback() {
                            @Override
                            public void onFinish(Object o, int code) {
                                chapter.setContent((String) o);
                                mChapterService.saveOrUpdateChapter(chapter);
                                curCacheChapterNum ++;
                                mHandler.sendMessage(mHandler.obtainMessage(9,tvDownloadProgress));
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }else {
                        curCacheChapterNum ++;
                    }
                }
                if (curCacheChapterNum == mChapters.size()){
                    TextHelper.showText(mReadActivity.getString(R.string.download_already_all_tips));
                }
            }
        });
    }


    private void updateDownloadProgress(TextView tvDownloadProgress){
        try {
            tvDownloadProgress.setText(curCacheChapterNum * 100 / mChapters.size() + " %");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取章节内容
     *
     * @param chapter
     * @param resultCallback
     */
    private void getChapterContent(final Chapter chapter, ResultCallback resultCallback) {
        if (StringHelper.isEmpty(chapter.getBookId())) chapter.setId(mBook.getId());
        if (!StringHelper.isEmpty(chapter.getContent())) {
            if (resultCallback != null) {
                resultCallback.onFinish(chapter.getContent(), 0);
            }
        } else {
            if (resultCallback != null) {
                CommonApi.getChapterContent(chapter.getUrl(), resultCallback);
            } else {
                CommonApi.getChapterContent(chapter.getUrl(), new ResultCallback() {
                    @Override
                    public void onFinish(final Object o, int code) {
                        chapter.setContent((String) o);
                        mChapterService.saveOrUpdateChapter(chapter);
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                });
            }
        }
    }


    /**
     * 白天夜间改变
     *
     * @param isCurDayStyle
     */
    private void changeNightAndDaySetting(boolean isCurDayStyle) {
        mSetting.setDayStyle(!isCurDayStyle);
        SysManager.saveSetting(mSetting);
        settingChange = true;
        init();
    }

    /**
     * 缩小字体
     */
    private void reduceTextSize() {
        if (mSetting.getReadWordSize() > 1) {
            mSetting.setReadWordSize(mSetting.getReadWordSize() - 1);
            SysManager.saveSetting(mSetting);
            settingChange = true;
            initContent();
        }
    }

    /**
     * 增大字体
     */
    private void increaseTextSize() {
        if (mSetting.getReadWordSize() < 40) {
            mSetting.setReadWordSize(mSetting.getReadWordSize() + 1);
            SysManager.saveSetting(mSetting);
            settingChange = true;
            initContent();
        }
    }

    /**
     * 改变阅读风格
     *
     * @param readStyle
     */
    private void changeStyle(ReadStyle readStyle) {
        settingChange = true;
        if (!mSetting.isDayStyle()) mSetting.setDayStyle(true);
        mSetting.setReadStyle(readStyle);
        switch (readStyle) {
            case common:
                mSetting.setReadBgColor(R.color.sys_common_bg);
                mSetting.setReadWordColor(R.color.sys_common_word);
                break;
            case leather:
                mSetting.setReadBgColor(R.mipmap.theme_leather_bg);
                mSetting.setReadWordColor(R.color.sys_leather_word);
                break;
            case protectedEye:
                mSetting.setReadBgColor(R.color.sys_protect_eye_bg);
                mSetting.setReadWordColor(R.color.sys_protect_eye_word);
                break;
            case breen:
                mSetting.setReadBgColor(R.color.sys_breen_bg);
                mSetting.setReadWordColor(R.color.sys_breen_word);
                break;
            case blueDeep:
                mSetting.setReadBgColor(R.color.sys_blue_deep_bg);
                mSetting.setReadWordColor(R.color.sys_blue_deep_word);
                break;
        }
        SysManager.saveSetting(mSetting);
        init();
    }


    /**
     * 自动滚动
     */
    private void autoScroll() {
        autoScrollOpening = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (autoScrollOpening) {
                    try {
                        Thread.sleep(mSetting.getAutoScrollSpeed() + 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendMessage(mHandler.obtainMessage(7));

                }
            }
        }).start();
    }


}
