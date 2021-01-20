package com.zhao.myreader.ui.search;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.zhao.myreader.R;
import com.zhao.myreader.enums.BookSource;
import com.zhao.myreader.greendao.entity.Book;
import com.zhao.myreader.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao on 2017/7/26.
 */

public class SearchBookAdapter extends ArrayAdapter<Book> {

    private int mResourceId;

    public SearchBookAdapter(Context context, int resourceId, List<Book> datas){
        super(context,resourceId,datas);
        mResourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(mResourceId,null);
            viewHolder.ivBookImg =  convertView.findViewById(R.id.iv_book_img);
            viewHolder.tvBookName = convertView.findViewById(R.id.tv_book_name);
            viewHolder.tvAuthor =  convertView.findViewById(R.id.tv_book_author);
            viewHolder.tvDesc =  convertView.findViewById(R.id.tv_book_desc);
            viewHolder.tvType =  convertView.findViewById(R.id.tv_book_type);
            viewHolder.tvBookSource = convertView.findViewById(R.id.tv_book_source);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initView(position,viewHolder);
        return convertView;
    }


    private void initView(int postion, ViewHolder viewHolder){
        Book book = getItem(postion);
        if (StringHelper.isEmpty(book.getImgUrl())){
            book.setImgUrl("");
        }
        Glide.with(getContext())
                .load(book.getImgUrl())
//                .override(DipPxUtil.dip2px(getContext(), 80), DipPxUtil.dip2px(getContext(), 150))
                .error(R.mipmap.no_image)
                .placeholder(R.mipmap.no_image)
                .into(viewHolder.ivBookImg);
        viewHolder.tvBookName.setText(book.getName());
        viewHolder.tvBookSource.setText(BookSource.fromString(book.getSource()).text);
        viewHolder.tvDesc.setText(book.getDesc());
        viewHolder.tvAuthor.setText(book.getAuthor());
        viewHolder.tvType.setText(book.getType());

    }

    class ViewHolder{
        ImageView ivBookImg;
        TextView tvBookName;
        TextView tvDesc;
        TextView tvAuthor;
        TextView tvType;
        TextView tvBookSource;

    }

}
