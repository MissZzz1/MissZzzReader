package com.zhao.myreader.ui.home.bookstore;

import android.content.Context;
import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zhao.myreader.R;

import com.zhao.myreader.callback.ResultCallback;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.util.StringHelper;
import com.zhao.myreader.webapi.BookStoreApi;


import java.util.List;



public class BookStoreBookAdapter extends RecyclerView.Adapter<BookStoreBookAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<Book> mDatas;
    private int mResourceId;
    private Context mContext;
    private RecyclerView rvContent;


    private OnItemClickListener onItemClickListener;

    private Handler mHandle = new Handler(message -> {

        switch (message.what){
            case  1:
                ViewHolder holder = (ViewHolder) message.obj;
                int pos = message.arg1;
                initImgAndDec(pos,holder);
                break;
        }

        return false;

    });


   BookStoreBookAdapter(Context context, int resourceId, List<Book> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mResourceId = resourceId;
        mContext = context;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView ivBookImg;
        TextView tvBookName;
        TextView tvDesc;
        TextView tvAuthor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (rvContent == null) rvContent = (RecyclerView) parent;
        View view = mInflater.inflate(mResourceId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ivBookImg =  view.findViewById(R.id.iv_book_img);
        viewHolder.tvBookName = view.findViewById(R.id.tv_book_name);
        viewHolder.tvAuthor = view.findViewById(R.id.tv_book_author);
        viewHolder.tvDesc = view.findViewById(R.id.tv_book_desc);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        initView(position, holder);
        if (onItemClickListener != null){

            holder.itemView.setOnClickListener(view -> {

                onItemClickListener.onClick(position,view);

            });

        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private void initView(final int position, final ViewHolder holder) {
       Book book = mDatas.get(position);
       holder.tvBookName.setText(book.getName());
       holder.tvAuthor.setText(book.getAuthor());
       if (StringHelper.isEmpty(book.getImgUrl())){
           //获取小说详情
           BookStoreApi.getBookInfo(book, new ResultCallback() {
               @Override
               public void onFinish(Object o, int code) {
                   mDatas.set(position,(Book) o);
                   mHandle.sendMessage(mHandle.obtainMessage(1,position,0,holder));
               }

               @Override
               public void onError(Exception e) {


               }
           });
       }else{
           initImgAndDec(position,holder);
       }



    }

    private void initImgAndDec(final int position, final ViewHolder holder){
        Book book = mDatas.get(position);

        //图片
        Glide.with(mContext)
                .load(book.getImgUrl())
//                .override(DipPxUtil.dip2px(getContext(), 80), DipPxUtil.dip2px(getContext(), 150))
                .error(R.mipmap.no_image)
                .placeholder(R.mipmap.no_image)
                .into(holder.ivBookImg);
        //简介
        holder.tvDesc.setText(book.getDesc());

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{

        void onClick(int pos,View view);

    }




}
