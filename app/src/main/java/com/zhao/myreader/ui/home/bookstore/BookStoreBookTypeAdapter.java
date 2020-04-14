package com.zhao.myreader.ui.home.bookstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.zhao.myreader.R;
import com.zhao.myreader.entity.bookstore.BookType;


import java.util.List;

public class BookStoreBookTypeAdapter extends RecyclerView.Adapter<BookStoreBookTypeAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<BookType> mDatas;
    private int mResourceId;
    private Context mContext;
    private RecyclerView rvContent;

    private OnItemClickListener onItemClickListener;

    private int selectPos = 0;


   BookStoreBookTypeAdapter(Context context, int resourceId, List<BookType> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mResourceId = resourceId;
        mContext = context;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View arg0) {
            super(arg0);
        }

        TextView tvTypeName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (rvContent == null) rvContent = (RecyclerView) parent;
        View view = mInflater.inflate(mResourceId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvTypeName = view.findViewById(R.id.tv_type_name);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        initView(position, holder);
        if (position == selectPos){
            holder.itemView.setBackgroundResource(R.color.white);
        }else{
            holder.itemView.setBackgroundResource(R.color.sys_book_type_bg);
        }

        if (onItemClickListener != null){

            holder.itemView.setOnClickListener(view -> {

                onItemClickListener.onClick(position,view);
                selectPos = position;
                notifyDataSetChanged();

            });

        }


    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private void initView(final int position, final ViewHolder holder) {
       BookType bookType = mDatas.get(position);
       holder.tvTypeName.setText(bookType.getTypeName());


    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{

        void onClick(int pos,View view);

    }






}
