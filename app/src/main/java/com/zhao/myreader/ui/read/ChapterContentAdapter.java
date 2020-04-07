package com.zhao.myreader.ui.read;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.spreada.utils.chinese.ZHConverter;
import com.zhao.myreader.R;
import com.zhao.myreader.application.SysManager;
import com.zhao.myreader.callback.ResultCallback;
import com.zhao.myreader.custom.MyTextView;
import com.zhao.myreader.entity.Setting;
import com.zhao.myreader.enums.Font;
import com.zhao.myreader.enums.Language;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.greendao.entity.Chapter;
import com.zhao.myreader.greendao.service.BookService;
import com.zhao.myreader.greendao.service.ChapterService;
import com.zhao.myreader.util.StringHelper;
import com.zhao.myreader.webapi.CommonApi;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * （已废弃）
 * Created by zhao on 2017/7/26.
 */

public class ChapterContentAdapter extends ArrayAdapter<Chapter> {

    private int mResourceId;

    private ListView mListView;
    private ChapterService mChapterService;
    private BookService mBookService;
    private Setting mSetting;
    private Book mBook;
    private Typeface mTypeFace;
    private TextView curTextView;

    public ChapterContentAdapter(Context context, int resourceId, ArrayList<Chapter> datas, Book book) {
        super(context, resourceId, datas);
        mResourceId = resourceId;
        mChapterService = new ChapterService();
        mBookService = new BookService();
        mSetting = SysManager.getSetting();
        mBook = book;
        initFont();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ViewHolder viewHolder = (ViewHolder) msg.obj;
                    viewHolder.tvErrorTips.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };


    @Override
    public void notifyDataSetChanged() {
        mSetting = SysManager.getSetting();
        initFont();
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(mResourceId, null);
            viewHolder.tvTitle = (MyTextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvContent = (MyTextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tvErrorTips = (TextView) convertView.findViewById(R.id.tv_loading_error_tips);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        mListView = (ListView) parent;
        initView(position, viewHolder, convertView);
        return convertView;
    }


    private void initView(final int postion, final ViewHolder viewHolder, View view) {
        final Chapter chapter = getItem(postion);
        viewHolder.tvContent.setTypeface(mTypeFace);
        viewHolder.tvTitle.setTypeface(mTypeFace);
        viewHolder.tvErrorTips.setVisibility(View.GONE);

       /* hiddenSoftInput(viewHolder.tvContent);
        hiddenSoftInput(viewHolder.tvTitle);*/

        viewHolder.tvTitle.setText("【" + getLanguageContext(chapter.getTitle()) + "】");


        if (mSetting.isDayStyle()) {
            viewHolder.tvTitle.setTextColor(getContext().getResources().getColor(mSetting.getReadWordColor()));
            viewHolder.tvContent.setTextColor(getContext().getResources().getColor(mSetting.getReadWordColor()));
        } else {
            viewHolder.tvTitle.setTextColor(getContext().getResources().getColor(R.color.sys_night_word));
            viewHolder.tvContent.setTextColor(getContext().getResources().getColor(R.color.sys_night_word));
        }


        viewHolder.tvTitle.setTextSize(mSetting.getReadWordSize() + 2);
        viewHolder.tvContent.setTextSize(mSetting.getReadWordSize());
        viewHolder.tvErrorTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChapterContent(chapter, viewHolder);
            }
        });
        if (StringHelper.isEmpty(chapter.getContent())) {
            getChapterContent(chapter, viewHolder);
        } else {
            viewHolder.tvContent.setText(getLanguageContext(chapter.getContent()));
        }


        curTextView = viewHolder.tvContent;
        preLoading(postion);

        lastLoading(postion);

        saveHistory(postion);

    }

    public TextView getCurTextView() {
        return curTextView;
    }

    private String getLanguageContext(String content) {
        if (mSetting.getLanguage() == Language.traditional && mSetting.getFont() == Font.默认字体) {
            return ZHConverter.convert(content, ZHConverter.TRADITIONAL);
        }
        return content;

    }


    /**
     * 加载章节内容
     *
     * @param chapter
     * @param viewHolder
     */
    private void getChapterContent(final Chapter chapter, final ViewHolder viewHolder) {
        if (viewHolder != null) {
            viewHolder.tvErrorTips.setVisibility(View.GONE);
        }
        Chapter cacheChapter = mChapterService.findChapterByBookIdAndTitle(chapter.getBookId(), chapter.getTitle());

        if (cacheChapter != null && !StringHelper.isEmpty(cacheChapter.getContent())) {
            chapter.setContent(cacheChapter.getContent());
            chapter.setId(cacheChapter.getId());
            if (viewHolder != null) {
                if (mSetting.getLanguage() == Language.traditional) {
                    viewHolder.tvContent.setText(ZHConverter.convert(chapter.getTitle(), ZHConverter.TRADITIONAL));
                } else {
                    viewHolder.tvContent.setText(chapter.getContent());
                }

                viewHolder.tvErrorTips.setVisibility(View.GONE);
            }
        } else {
            CommonApi.getChapterContent(chapter.getUrl(), new ResultCallback() {
                @Override
                public void onFinish(final Object o, int code) {
                    chapter.setContent((String) o);
                    mChapterService.saveOrUpdateChapter(chapter);
                    if (viewHolder != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.tvContent.setText(getLanguageContext((String) o));
                                viewHolder.tvErrorTips.setVisibility(View.GONE);
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception e) {
                    if (viewHolder != null) {
                        mHandler.sendMessage(mHandler.obtainMessage(1, viewHolder));
                    }
                }

            });
        }

    }


    /**
     * 预加载下一章
     */
    private void preLoading(int position) {
        if (position + 1 < getCount()) {
            Chapter chapter = getItem(position + 1);
            if (StringHelper.isEmpty(chapter.getContent())) {
                getChapterContent(chapter, null);
            }
        }
    }

    /**
     * 预加载上一张
     *
     * @param position
     */
    private void lastLoading(int position) {
        if (position > 0) {
            Chapter chapter = getItem(position - 1);
            if (StringHelper.isEmpty(chapter.getContent())) {
                getChapterContent(chapter, null);
            }
        }
    }

    public void saveHistory(int position) {
        if (!StringHelper.isEmpty(mBook.getId())) {
            mBook.setHisttoryChapterNum(position);
            mBookService.updateEntity(mBook);
        }
    }

    public void initFont() {
        if (mSetting.getFont() == Font.默认字体) {
            mTypeFace = null;
        } else {
            mTypeFace = Typeface.createFromAsset(getContext().getAssets(), mSetting.getFont().path);
        }
    }

    private void hiddenSoftInput(EditText editText){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        try {
            Class<EditText> cls = EditText.class;
            Method setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setSoftInputShownOnFocus.setAccessible(true);
            setSoftInputShownOnFocus.invoke(editText, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class ViewHolder {

        MyTextView tvTitle;
        MyTextView tvContent;
        TextView tvErrorTips;
    }

}
