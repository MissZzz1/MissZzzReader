package com.zhao.myreader.ui.read;

import android.app.Dialog;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.zhao.myreader.R;
import com.zhao.myreader.application.MyApplication;
import com.zhao.myreader.application.SysManager;
import com.zhao.myreader.base.BaseActivity;
import com.zhao.myreader.base.BasePresenter;
import com.zhao.myreader.callback.ResultCallback;
import com.zhao.myreader.common.APPCONST;
import com.zhao.myreader.creator.DialogCreator;
import com.zhao.myreader.entity.Setting;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.greendao.entity.Chapter;
import com.zhao.myreader.greendao.service.BookService;
import com.zhao.myreader.greendao.service.ChapterService;
import com.zhao.myreader.util.StringHelper;
import com.zhao.myreader.util.TextHelper;
import com.zhao.myreader.webapi.CommonApi;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by zhao on 2017/7/27.
 */

public class ReadPresenter implements BasePresenter {

    private ReadActivity mReadActivity;
    private Book mBook;
    private ArrayList<Chapter> mChapters = new ArrayList<>();
    private ChapterService mChapterService;
    private BookService mBookService;
    private ChapterContentAdapter mChapterContentAdapter;
    private ChapterTitleAdapter mChapterTitleAdapter;
    private Setting mSetting;

    private boolean settingChange;//是否是设置改变

    private float pointX;
    private float pointY;

    private float settingOnClickValidFrom;
    private float settingOnClickValidTo;


    private Dialog mDialog;

    private int curSortflag = 0; //0正序  1倒序


    private int num = -1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    init();
                    break;
                case 2:
                    mReadActivity.getPbLoading().setVisibility(View.GONE);
                    mReadActivity.getSrlContent().finishLoadmore();
                    break;
                case 3:
                    mReadActivity.getLvContent().setSelection(msg.arg1);
                    mReadActivity.getPbLoading().setVisibility(View.GONE);
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
        mReadActivity.getDlReadActivity().setBackgroundColor(mReadActivity.getResources().getColor(mSetting.getReadBgColor()));

        mBook = (Book) mReadActivity.getIntent().getSerializableExtra(APPCONST.BOOK);
        settingOnClickValidFrom = BaseActivity.height / 4;
        settingOnClickValidTo = BaseActivity.height / 4 * 3;
        mReadActivity.getSrlContent().setEnableAutoLoadmore(true);
        mReadActivity.getSrlContent().setEnableRefresh(false);
        mReadActivity.getSrlContent().setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getData();
            }
        });
        mReadActivity.getPbLoading().setVisibility(View.VISIBLE);
        mReadActivity.getLvContent().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pointX = motionEvent.getX();
                pointY = motionEvent.getY();
                return false;
            }
        });
        mReadActivity.getLvContent().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (pointY > settingOnClickValidFrom && pointY < settingOnClickValidTo) {
                    if (mDialog == null) {
                        int progress = mReadActivity.getLvContent().getLastVisiblePosition() * 100 / (mChapters.size() - 1) ;
                        mDialog = DialogCreator.createReadSetting(mReadActivity, mSetting.isDayStyle(), progress, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mReadActivity.finish();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int curPosition = mReadActivity.getLvContent().getLastVisiblePosition();
                                if (curPosition > 0) {
                                    mReadActivity.getLvContent().setSelection(curPosition - 1);
                                }
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int curPosition = mReadActivity.getLvContent().getLastVisiblePosition();
                                if (curPosition < mChapters.size() - 1) {
                                    mReadActivity.getLvContent().setSelection(curPosition + 1);
                                }
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //打开侧滑菜单
                                initChapterTitleList();
                                mReadActivity.getDlReadActivity().openDrawer(GravityCompat.START);
                                mDialog.dismiss();

                            }
                        }, new DialogCreator.OnClickNightAndDayListener() {
                            @Override
                            public void onClick(Dialog dialog, View view, boolean isDayStyle) {
                                changeNightAndDaySetting(isDayStyle);
                                init();

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }, new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                mReadActivity.getPbLoading().setVisibility(View.VISIBLE);
                                final int chapterNum = (mChapters.size() - 1) * i / 100;
                                mBook.setHisttoryChapterNum(chapterNum);
                                getChapterContent(mChapters.get(chapterNum), new ResultCallback() {
                                    @Override
                                    public void onFinish(Object o, int code) {
                                        mChapters.get(chapterNum).setContent((String) o);
                                        mChapterService.saveOrUpdateChapter(mChapters.get(chapterNum));
                                        mHandler.sendMessage(mHandler.obtainMessage(1));
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
                        });
                    } else {
                        mDialog.show();
                    }
                }else  if ( pointY > settingOnClickValidTo) {
                    mReadActivity.getLvContent().smoothScrollBy(BaseActivity.height,200);
                }else if (pointX > settingOnClickValidFrom){
                    mReadActivity.getLvContent().smoothScrollBy(-BaseActivity.height,200);
                }
            }
        });
        mReadActivity.getLvChapterList().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                //关闭侧滑菜单
                mReadActivity.getDlReadActivity().closeDrawer(GravityCompat.START);
                if (StringHelper.isEmpty(mChapters.get(i).getContent())){
                    mReadActivity.getPbLoading().setVisibility(View.VISIBLE);
                    CommonApi.getChapterContent(mChapters.get(i).getUrl(), new ResultCallback() {
                        @Override
                        public void onFinish(Object o, int code) {
                            mChapters.get(i).setContent((String)o);
                            mHandler.sendMessage(mHandler.obtainMessage(3,i ,0));
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }else {
                    mReadActivity.getLvContent().setSelection(i);
                }

            }
        });

        mReadActivity.getTvChapterSort().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curSortflag == 0){//当前正序
                    mReadActivity.getTvChapterSort().setText(mReadActivity.getString(R.string.inverted_sort));
                    Collections.reverse(mChapters);
                    initChapterTitleList();
                }else {//当前倒序
                    mReadActivity.getTvChapterSort().setText(mReadActivity.getString(R.string.inverted_sort));
                    Collections.reverse(mChapters);
                    initChapterTitleList();
                }
            }
        });
        mChapters = (ArrayList<Chapter>) mChapterService.findBookAllChapterByBookId(mBook.getId());
        getData();

    }

    private void init(){
        initContent();
        initChapterTitleList();
    }

    /**
     * 初始化主内容视图
     */
    private void initContent() {

        if (mChapterContentAdapter == null) {
            mChapterContentAdapter = new ChapterContentAdapter(mReadActivity, R.layout.listview_chapter_content_item, mChapters);
            initSetting();
            mReadActivity.getLvContent().setAdapter(mChapterContentAdapter);
        } else {
            initSetting();
            mChapterContentAdapter.notifyDataSetChanged();
        }
        if (!settingChange) {
            mReadActivity.getLvContent().setSelection(mBook.getHisttoryChapterNum());
        }else {
            settingChange = false;
        }
        mReadActivity.getPbLoading().setVisibility(View.GONE);
        mReadActivity.getSrlContent().finishLoadmore();
    }

    /**
     * 初始化章节目录视图
     */
    private void initChapterTitleList(){
        mReadActivity.getTvBookList().setTextColor(mReadActivity.getResources().getColor(mSetting.getReadWordColor()));
        mReadActivity.getTvChapterSort().setTextColor(mReadActivity.getResources().getColor(mSetting.getReadWordColor()));
        mReadActivity.getLlChapterListView().setBackgroundColor(mReadActivity.getResources().getColor(mSetting.getReadBgColor()));

        if (mChapterTitleAdapter == null){
            //设置布局管理器
            mChapterTitleAdapter = new ChapterTitleAdapter(mReadActivity, R.layout.listview_chapter_title_item,mChapters);
            mReadActivity.getLvChapterList().setAdapter(mChapterTitleAdapter);
        }else {
            mChapterTitleAdapter.notifyDataSetChanged();
        }

        mReadActivity.getLvChapterList().setSelection(mReadActivity.getLvContent().getLastVisiblePosition());

    }


    /**
     * 章节数据网络同步
     */
    private void getData() {

        CommonApi.getBookChapters(mBook.getChapterUrl(), new ResultCallback() {
            @Override
            public void onFinish(Object o, int code) {
                final ArrayList<Chapter> chapters = (ArrayList<Chapter>) o;
                if (mChapters.size() < chapters.size()) {
                    mChapters.addAll(chapters.subList(mChapters.size(), chapters.size()));
                }
                if (mChapters.size() == 0){
                    TextHelper.showLongText("该书查询不到任何章节");
                    mReadActivity.getPbLoading().setVisibility(View.GONE);
                }else {
                    if (mBook.getHisttoryChapterNum() < 0) mBook.setHisttoryChapterNum(0);
                    getChapterContent(chapters.get(mBook.getHisttoryChapterNum()), new ResultCallback() {
                        @Override
                        public void onFinish(Object o, int code) {
                            chapters.get(mBook.getHisttoryChapterNum()).setContent((String) o);
                            mChapterService.saveOrUpdateChapter(chapters.get(mBook.getHisttoryChapterNum()));
                            mHandler.sendMessage(mHandler.obtainMessage(1));
//                        getAllChapterData();
                        }

                        @Override
                        public void onError(Exception e) {
                            mHandler.sendMessage(mHandler.obtainMessage(1));
                        }
                    });
                    for (Chapter chapter : mChapters){
                        chapter.setBookId(mBook.getId());
                        if (StringHelper.isEmpty(chapter.getId())){
                            mChapterService.addChapter(chapter);
                        }
                    }
                }



            }

            @Override
            public void onError(Exception e) {
                mHandler.sendMessage(mHandler.obtainMessage(2));
            }
        });
    }

    /**
     * 缓存所有章节
     */
    private void getAllChapterData(){
        MyApplication.getApplication().newThread(new Runnable() {
            @Override
            public void run() {
                for (final Chapter chapter : mChapters){
                    getChapterContent(chapter,null);

                }
            }
        });

    }

    private void getChapterContent(final Chapter chapter, ResultCallback resultCallback){

        if (!StringHelper.isEmpty(chapter.getContent())){
            if (resultCallback != null){
                resultCallback.onFinish(chapter.getContent(),0);
            }
        }else {
            if (resultCallback != null){
                CommonApi.getChapterContent(chapter.getUrl(), resultCallback);
            }else {
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
     * @param isCurDayStyle
     */
    private void changeNightAndDaySetting(boolean isCurDayStyle) {
        if (isCurDayStyle) {
            mSetting.setReadWordColor(APPCONST.READ_STYLE_NIGHT[0]);
            mSetting.setReadBgColor(APPCONST.READ_STYLE_NIGHT[1]);
            mReadActivity.getDlReadActivity().setBackgroundColor(mReadActivity.getResources().getColor(APPCONST.READ_STYLE_NIGHT[1]));
        } else {
            mSetting.setReadWordColor(APPCONST.READ_STYLE_PROTECTED_EYE[0]);
            mSetting.setReadBgColor(APPCONST.READ_STYLE_PROTECTED_EYE[1]);
            mReadActivity.getDlReadActivity().setBackgroundColor(mReadActivity.getResources().getColor(APPCONST.READ_STYLE_PROTECTED_EYE[1]));

        }
        mSetting.setDayStyle(!isCurDayStyle);
        SysManager.saveSetting(mSetting);
        settingChange = true;
    }

    /**
     * 初始化主内容设置
     */
    private void initSetting() {
        if (mSetting.isDayStyle()) {
            mChapterContentAdapter.setmTextColor(APPCONST.READ_STYLE_PROTECTED_EYE[0]);
            mChapterContentAdapter.setmBgColor(APPCONST.READ_STYLE_PROTECTED_EYE[1]);
        } else {
            mChapterContentAdapter.setmTextColor(APPCONST.READ_STYLE_NIGHT[0]);
            mChapterContentAdapter.setmBgColor(APPCONST.READ_STYLE_NIGHT[1]);
        }
    }

    public void  saveHistory(){
        if (!StringHelper.isEmpty(mBook.getId())) {
            mBook.setHisttoryChapterNum(mReadActivity.getLvContent().getLastVisiblePosition());
            mBookService.updateEntity(mBook);
        }
    }

    private void setThemeColor(int colorPrimary, int colorPrimaryDark) {
//        mToolbar.setBackgroundResource(colorPrimary);
        mReadActivity.getSrlContent().setPrimaryColorsId(colorPrimary, android.R.color.white);
        if (Build.VERSION.SDK_INT >= 21) {
            mReadActivity.getWindow().setStatusBarColor(ContextCompat.getColor(mReadActivity, colorPrimaryDark));
        }
    }


}
