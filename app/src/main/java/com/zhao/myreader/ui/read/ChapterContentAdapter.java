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

import com.spreada.utils.chinese.ZHConverter;
import com.zhao.myreader.R;
import com.zhao.myreader.application.SysManager;
import com.zhao.myreader.callback.ResultCallback;
import com.zhao.myreader.entity.Setting;
import com.zhao.myreader.enums.Language;
import com.zhao.myreader.greendao.entity.Chapter;
import com.zhao.myreader.greendao.service.ChapterService;
import com.zhao.myreader.util.ChschtUtil;
import com.zhao.myreader.util.StringHelper;
import com.zhao.myreader.webapi.CommonApi;

import java.util.ArrayList;

/**
 * Created by zhao on 2017/7/26.
 */

public class ChapterContentAdapter extends ArrayAdapter<Chapter> {

    private int mResourceId;

    private ListView mListView;
    private ChapterService mChapterService;
    private Setting mSetting;

    public ChapterContentAdapter(Context context, int resourceId, ArrayList<Chapter> datas) {
        super(context, resourceId, datas);
        mResourceId = resourceId;
        mChapterService = new ChapterService();
        mSetting = SysManager.getSetting();
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



    @Override
    public void notifyDataSetChanged() {
        mSetting = SysManager.getSetting();
        super.notifyDataSetChanged();
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
        if (mSetting.getLanguage() == Language.traditional){
            viewHolder.tvTitle.setText("【" + ZHConverter.convert(chapter.getTitle(),ZHConverter.TRADITIONAL) + "】");
        }else {
            viewHolder.tvTitle.setText("【" + chapter.getTitle() + "】");
        }


        if (mSetting.isDayStyle()) {
            viewHolder.tvTitle.setTextColor(getContext().getResources().getColor(mSetting.getReadWordColor()));
            viewHolder.tvContent.setTextColor(getContext().getResources().getColor(mSetting.getReadWordColor()));
        }else {
            viewHolder.tvTitle.setTextColor(getContext().getResources().getColor(R.color.sys_night_word));
            viewHolder.tvContent.setTextColor(getContext().getResources().getColor(R.color.sys_night_word));
        }


        viewHolder.tvTitle.setTextSize(mSetting.getReadWordSize());
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
            if (mSetting.getLanguage() == Language.traditional){
                viewHolder.tvContent.setText(ZHConverter.convert(chapter.getContent(),ZHConverter.TRADITIONAL));
            }else {
                viewHolder.tvContent.setText(chapter.getContent());
            }

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
        Chapter cacheChapter = mChapterService.findChapterByBookIdAndTitle(chapter.getBookId(), chapter.getTitle());

        if (cacheChapter != null && !StringHelper.isEmpty(cacheChapter.getContent())) {
            chapter.setContent(cacheChapter.getContent());
            chapter.setId(cacheChapter.getId());
            if (viewHolder != null) {
                if (mSetting.getLanguage() == Language.traditional){
                    viewHolder.tvContent.setText(ZHConverter.convert(chapter.getTitle(),ZHConverter.TRADITIONAL));
                }else {
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
                                if (mSetting.getLanguage() == Language.traditional){
                                    viewHolder.tvContent.setText(ZHConverter.convert((String) o,ZHConverter.TRADITIONAL));
                                }else {
                                    viewHolder.tvContent.setText((String) o);
                                }
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
