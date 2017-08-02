package com.zhao.myreader.ui.read;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhao.myreader.R;
import com.zhao.myreader.callback.ResultCallback;
import com.zhao.myreader.greendao.entity.Chapter;
import com.zhao.myreader.greendao.service.ChapterService;
import com.zhao.myreader.util.StringHelper;
import com.zhao.myreader.webapi.CommonApi;

import java.util.ArrayList;

/**
 * Created by zhao on 2017/7/26.
 */

public class ChapterContentAdapter extends ArrayAdapter<Chapter> {

    private int mResourceId;
    private float mTextSize = 0;
    private int mTextColor = 0;
    private int mBgColor = 0;
    private ListView mListView;
    private ChapterService mChapterService;

    public ChapterContentAdapter(Context context, int resourceId, ArrayList<Chapter> datas) {
        super(context, resourceId, datas);
        mResourceId = resourceId;
        mChapterService = new ChapterService();
    }

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

    public void setmBgColor(int mBgColor) {
        this.mBgColor = mBgColor;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public void setmTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(mResourceId, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
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
        viewHolder.tvErrorTips.setVisibility(View.GONE);
        viewHolder.tvTitle.setText("【" + chapter.getTitle() + "】");
        if (mTextColor != 0) {
            viewHolder.tvTitle.setTextColor(getContext().getResources().getColor(mTextColor));
            viewHolder.tvContent.setTextColor(getContext().getResources().getColor(mTextColor));
        }
        if (mTextSize != 0) {
            viewHolder.tvTitle.setTextSize(mTextSize);
            viewHolder.tvContent.setTextSize(mTextSize);
        }
        if (mBgColor != 0) {
            view.setBackgroundColor(getContext().getResources().getColor(mBgColor));
        }
        viewHolder.tvErrorTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChapterContent(chapter, viewHolder);
            }
        });
        if (StringHelper.isEmpty(chapter.getContent())) {
            getChapterContent(chapter, viewHolder);
        } else {
            viewHolder.tvContent.setText(chapter.getContent());
        }

        preLoading(postion);

        lastLoading(postion);

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
        Chapter cacheChapter = mChapterService.findChapterByBookIdAndTitle(chapter.getBookId(),chapter.getTitle());

        if (cacheChapter != null && !StringHelper.isEmpty(cacheChapter.getContent())){
            chapter.setContent(cacheChapter.getContent());
            chapter.setId(cacheChapter.getId());
            if (viewHolder != null) {
                viewHolder.tvContent.setText(chapter.getContent());
                viewHolder.tvErrorTips.setVisibility(View.GONE);
            }
        }else {
            CommonApi.getChapterContent(chapter.getUrl(), new ResultCallback() {
                @Override
                public void onFinish(final Object o, int code) {
                    chapter.setContent((String) o);
                    mChapterService.saveOrUpdateChapter(chapter);
                    if (viewHolder != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.tvContent.setText((String) o);
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


    class ViewHolder {

        TextView tvTitle;
        TextView tvContent;
        TextView tvErrorTips;
    }

}
