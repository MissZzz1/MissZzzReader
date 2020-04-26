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
import com.zhao.myreader.databinding.ListviewBookStoreBookItemBinding;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.util.StringHelper;
import com.zhao.myreader.webapi.BookStoreApi;


import java.util.List;



public class BookStoreBookAdapter extends RecyclerView.Adapter<BookStoreBookAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<Book> mDatas;

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


   BookStoreBookAdapter(Context context,  List<Book> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;

        mContext = context;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ListviewBookStoreBookItemBinding binding;

        ViewHolder(ListviewBookStoreBookItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (rvContent == null) rvContent = (RecyclerView) parent;
        ListviewBookStoreBookItemBinding binding = ListviewBookStoreBookItemBinding.inflate(mInflater,parent,false);

        return new ViewHolder(binding);
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
       holder.binding.tvBookName.setText(book.getName());
       holder.binding.tvBookAuthor.setText(book.getAuthor());
       holder.binding.tvBookDesc.setText("");
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
                .into(holder.binding.ivBookImg);
        //简介
        holder.binding.tvBookDesc.setText(book.getDesc());

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{

        void onClick(int pos,View view);

    }




}
