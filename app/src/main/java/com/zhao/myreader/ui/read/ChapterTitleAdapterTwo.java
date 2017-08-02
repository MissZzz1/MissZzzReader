package com.zhao.myreader.ui.read;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhao.myreader.R;
import com.zhao.myreader.application.SysManager;
import com.zhao.myreader.entity.Setting;
import com.zhao.myreader.greendao.entity.Chapter;

import java.util.List;


/**
 * Created by zhao on 2017/5/5.
 */

public class ChapterTitleAdapterTwo extends RecyclerView.Adapter<ChapterTitleAdapterTwo.ViewHolder> {


    private LayoutInflater mInflater;
    private List<Chapter> mDatas;
    private OnClickItemListener mOnClickItemListener;
    private Context mContext;
    private Setting setting;

    public ChapterTitleAdapterTwo(Context context, List<Chapter> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        mContext = context;
        setting = SysManager.getSetting();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView tvTitle;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.listview_chapter_title_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_chapter_title);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final  Chapter chapter = mDatas.get(i);
        viewHolder.tvTitle.setText("【" + chapter.getTitle() + "】");
        viewHolder.tvTitle.setTextColor(mContext.getResources().getColor(setting.getReadWordColor()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickItemListener != null){
                    mOnClickItemListener.onClick(viewHolder.itemView,i);
                }
            }
        });
    }

    public void setmOnClickItemListener(OnClickItemListener mOnClickItemListener) {
        this.mOnClickItemListener = mOnClickItemListener;
    }

    public interface OnClickItemListener{
        void onClick(View view, int positon);
    }

}

