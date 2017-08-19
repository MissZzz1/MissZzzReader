package com.zhao.myreader.custom;

import android.content.Context;
import android.support.annotation.Nullable;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.TintContextWrapper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by zhao on 2017/8/19.
 */

public class MyTextView extends AppCompatTextView{

    private OnTouchListener mOnTouchListener;

    public MyTextView(Context context){
        super(context);
    }
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mOnTouchListener != null){
            mOnTouchListener.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }



    public void setmOnTouchListener(OnTouchListener mOnTouchListener) {
        this.mOnTouchListener = mOnTouchListener;
    }

   public interface OnTouchListener{
        void onTouchEvent(MotionEvent event);
    }
}
