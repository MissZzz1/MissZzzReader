package com.zhao.myreader.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zhao.myreader.base.BaseActivity;

import java.lang.reflect.Field;

/**
 * Created by zhao on 2016/10/28.
 */

public class ListViewHeight {
    private static HandlerThread thread = new HandlerThread("listview");
    private static int totalHeight = 0;
    static {
        thread.start();
    }
    private static Handler handler = new Handler(thread.getLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:

                    break;
            }
        }
    };

    /**
     * 计算listview的高度
     * @param listView
     * @return
     */
    public static int setListViewHeightBasedOnChildren(final ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                             View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        final ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        MyApplication.getApplication().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
        listView.setLayoutParams(params);
//            }
//        });

        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }



    /**
     * 日期gridview的高度
     * @param gridView
     * @return
     */
    public static int setDateGridViewHeightBasedOnChildren(GridView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < (listAdapter.getCount()/7); i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (1 * (listAdapter.getCount()/7 - 1));
        gridView.setLayoutParams(params);
        return totalHeight + (1 * (listAdapter.getCount()/7 - 1));
    }

    /**
     * 计算gridview的高度
     * @param gridView
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columnWidths;
        int columns=0;
        int horizontalBorderHeight = 0;
        int verticalBorderHeight = 0;

        Class<?> clazz=gridView.getClass();
        try {
            //利用反射，取得横向分割线高度
            Field vertical=clazz.getDeclaredField("mVerticalSpacing");
            vertical.setAccessible(true);
            verticalBorderHeight =(Integer)vertical.get(gridView);

            //利用反射，取得纵向分割线高度
            Field horizontalSpacing=clazz.getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight=(Integer)horizontalSpacing.get(gridView);

            //利用反射，取得每行显示的个数
            Field column=clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns=(Integer)column.get(gridView);

            if(columns == -1 || columns == 0){
                Field columnWidth = clazz.getDeclaredField("mRequestedColumnWidth");
                columnWidth.setAccessible(true);
                columnWidths = (Integer)columnWidth.get(gridView);
                int pad = gridView.getPaddingLeft() * 2;
                int width = BaseActivity.width - pad * 2;
                columns = width  / (columnWidths /*+ horizontalBorderHeight*/);
                if(horizontalBorderHeight * (columns - 1) + columnWidths * columns > width){
                    columns --;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        //判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if(listAdapter.getCount()%columns > 0){
            rows=listAdapter.getCount()/columns+1;
        }else {
            rows=listAdapter.getCount()/columns;
        }
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { //只计算每项高度*行数
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight  + gridView.getPaddingTop() + + gridView.getPaddingBottom() + verticalBorderHeight * (rows-1);//最后加上分割线总高度
        gridView.setLayoutParams(params);
    }

}
