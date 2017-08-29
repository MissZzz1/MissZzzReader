package com.zhao.myreader.custom;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ScrollView;


import com.zhao.myreader.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2008-2015, Huawei Tech. Co., Ltd.
 * <p/>
 * Description : 拖动排序布局
 *
 * @version V100R001
 * @since V100R001
 */
@SuppressLint({"NewApi", "Override"})
public class DragSortGridView extends FrameLayout {
    protected NoScrollGridView mGridView;
    private ScrollView mScrollView;
    private int headDragPosition = 0;
    private int footDragPosition = 0;
    private FrameLayout mDragFrame;
    private View mCopyView, hideView;
    private GestureDetector detector;

    private ViewGroup touchClashparent;
    private boolean isLongOnClick;

    /**
     * 动画时间
     */
    private static final long ANIM_DURING = 250;
    protected int mNumColumns = 3, mColHeight = 0, mColWidth = 0, mChildCount = 0, mMaxHeight = 0;
    private int currentDragPosition = -1;
    private DragAdapter adapter;
    /**
     * 持有子view
     */
    private List<View> mChilds = new ArrayList<View>();
    private static final int TAG_KEY = R.id.first;
    // private static final int TAG_KEY = R.id.tag_key;
    private int mCurrentY = 0;
    /**
     * 触摸区域,0不滚动区域,1可向上滚动的区域,-1可向下滚动的区域
     */
    private int mTouchArea = 0;
    /**
     * gridview能否滚动,是否内容太多
     */
    private boolean canScroll = true;
    /**
     * 是否可以拖动,点击拖动策略下直接开启,长按拖动需要长按以后开启
     */
    private boolean isDragable = true;
    /**
     * 自动滚屏的动画
     */
    private ValueAnimator animator;
    /**
     * view是否加载完成,如果未加载完成,没有宽高,无法接受事件
     */
    private boolean isViewInitDone = false;

    /**
     * 是否有位置发生改变,否则不用重绘
     */
    private boolean hasPositionChange = false;

    /**
     * 适配器的观察者,观察适配器的数据改变
     */
    private DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            mChildCount = adapter.getCount();
            // 下列属性状态清除,才会在被调用notifyDataSetChange时,在gridview测量布局完成后重新获取
            mChilds.clear();
            mColHeight = mColWidth = mMaxHeight = 0;
            isViewInitDone = false;
        }

        @Override
        public void onInvalidated() {
            mChildCount = adapter.getCount();
        }
    };
    private float[] lastLocation = null;
    /**
     * 手势监听器,滚动和单击
     */
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (hasSendDragMsg) {
                hasSendDragMsg = false;
                handler.removeMessages(0x123);
            }
            if (isDragable && mCopyView != null) {// 可以拖动,实现跟随手指的拖动效果

                // /// 2015/11/27补充修正跟随手指移动方法,适用于当本控件在drag时同时滚动的情况
                if (lastLocation == null) {
                    lastLocation = new float[]{e1.getRawX(), e1.getRawY()};
                }
                distanceX = lastLocation[0] - e2.getRawX();
                distanceY = lastLocation[1] - e2.getRawY();
                lastLocation[0] = e2.getRawX();
                lastLocation[1] = e2.getRawY();
                // ////////

                mCopyView.setX(mCopyView.getX() - distanceX);
                mCopyView.setY(mCopyView.getY() - distanceY);
                mCopyView.invalidate();
                int to = eventToPosition(e2);
                if (to != currentDragPosition && to >= headDragPosition && to < mChildCount - footDragPosition) {
                    onDragPositionChange(currentDragPosition, to);
                }
            }
            return true;
        }



        @Override
        public void onShowPress(final MotionEvent e) {
            /** 响应长按拖拽 */

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isLongOnClick) {
                        if (itemLongClickListener != null) {
                            itemLongClickListener.onItemLongClick(mGridView, childAt(currentDragPosition), currentDragPosition, 0);
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mDragMode == DRAG_BY_LONG_CLICK) {

                                    // 启动拖拽模式
                                    // isDragable = true;
                                    // 通知父控件不拦截我的事件
                                    getParent().requestDisallowInterceptTouchEvent(true);
                                    // 根据点击的位置生成该位置上的view镜像
                                    int position = eventToPosition(e);
                                    if (position >= headDragPosition && position < mChildCount - footDragPosition) {
                                        // copyView(currentDragPosition = position);
                                        Message msg = handler.obtainMessage(0x123, position, 0);
                                        // showpress本身大概需要170毫秒
//                            handler.sendMessageDelayed(msg, dragLongPressTime - 170);
                                        handler.sendMessage(msg);
                                        hasSendDragMsg = true;
                                    }
                                }
                            }
                        });
                    }
                }
            },dragLongPressTime - 170);
        };
    };
    private boolean hasSendDragMsg = false;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0x123:
                    // 启动拖拽模式
                    isDragable = true;
                    // 根据点击的位置生成该位置上的view镜像
                    copyView(currentDragPosition = msg.arg1);
                    hasSendDragMsg = false;
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    public DragSortGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragSortGridView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Context context = getContext();
        mGridView = new NoScrollGridView(context);
        mGridView.setVerticalScrollBarEnabled(false);
        mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        mGridView.setSelector(new ColorDrawable());
        // View的宽高之类必须在测量,布局,绘制一系列过程之后才能获取到
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (mChilds.isEmpty()) {
                    for (int i = 0; i < mGridView.getChildCount(); i++) {
                        View view = mGridView.getChildAt(i);
                        view.setTag(TAG_KEY, new int[]{0, 0});
                        view.clearAnimation();
                        mChilds.add(view);
                    }
                }
                if (!mChilds.isEmpty()) {
                    mColHeight = mChilds.get(0).getHeight();
                }
                mColWidth = mGridView.getColumnWidth();
                if (mChildCount % mNumColumns == 0) {
                    mMaxHeight = mColHeight * mChildCount / mNumColumns;
                } else {
                    mMaxHeight = mColHeight * (mChildCount / mNumColumns + 1);
                }
                canScroll = mMaxHeight - getHeight() > 0;
                // 告知事件处理,完成View加载,许多属性也已经初始化了
                isViewInitDone = true;
            }
        });
        mScrollView = new ListenScrollView(context);

        mDragFrame = new FrameLayout(context);
        addView(mScrollView, -1, -1);
        mScrollView.addView(mGridView, -1, -1);

        addView(mDragFrame, new LayoutParams(-1, -1));
        detector = new GestureDetector(context, simpleOnGestureListener);
        detector.setIsLongpressEnabled(false);
        mGridView.setNumColumns(mNumColumns);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isLongOnClick = true;
                    break;

                case MotionEvent.ACTION_UP:
                    isLongOnClick = false;
                    break;
                default:
                    break;
            }
            if (l != null) {
                l.onTouch(this, ev);
            }
            if (!isViewInitDone) {
                return false;
            }

            if (isDragable) {
                handleScrollAndCreMirror(ev);
            } else {
                // 交给子控件自己处理
                if (canScroll) {
                    mScrollView.dispatchTouchEvent(ev);
                } else {
                    mGridView.dispatchTouchEvent(ev);
                }
            }
            // 处理拖动
            detector.onTouchEvent(ev);
            if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
                lastLocation = null;
                if (hasSendDragMsg) {
                    hasSendDragMsg = false;
                    handler.removeMessages(0x123);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Author :[pWX273343] 2015年7月22日
     * <p>
     * Description :拦截所有事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 处理自动滚屏,和单击生成镜像
     */
    private void handleScrollAndCreMirror(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 通知父控件不拦截我的事件
                getParent().requestDisallowInterceptTouchEvent(true);
                if (touchClashparent != null) {
                    touchClashparent.requestDisallowInterceptTouchEvent(true);
                }
                // 根据点击的位置生成该位置上的view镜像
                int position = eventToPosition(ev);
                if (position >= headDragPosition && position < mChildCount - footDragPosition) {
                    copyView(currentDragPosition = position);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // 通知父控件不拦截我的事件
                getParent().requestDisallowInterceptTouchEvent(true);
                if (touchClashparent != null) {
                    touchClashparent.requestDisallowInterceptTouchEvent(true);
                }
                // 内容太多时,移动到边缘会自动滚动
                if (canScroll) {

                    int touchArea = decodeTouchArea(ev);
                    if (touchArea != mTouchArea) {
                        onTouchAreaChange(touchArea);
                        mTouchArea = touchArea;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (hideView != null) {
                    hideView.setVisibility(View.VISIBLE);
                    if (onDragSelectListener != null) {
                        onDragSelectListener.onPutDown(hideView);
                    }
                }
                mDragFrame.removeAllViews();
                // mDragFrame.scrollTo(0, 0);
                // isNotifyByDragSort = true;
                if (hasPositionChange) {
                    hasPositionChange = false;
                    adapter.notifyDataSetChanged();
                } else if (mDragMode == DRAG_BY_LONG_CLICK && itemLongClickListener != null) {
                    itemLongClickListener.onItemLongClick(mGridView, childAt(currentDragPosition), currentDragPosition, 0);
                }
                // 停止滚动
                if (canScroll) {
                    int scrollStates2 = decodeTouchArea(ev);
                    if (scrollStates2 != 0) {
                        onTouchAreaChange(0);
                        mTouchArea = 0;
                    }
                }
                // 放手时取消拖动排序模式
                if (mDragMode == DRAG_BY_LONG_CLICK) {
                    isDragable = false;
                }
                break;
            default:
                break;
        }
    }

    /**
     * @param ev 事件
     * @return 0中间区域, 1底部,-1顶部
     * @描述: 检查当前触摸事件位于哪个区域, 顶部1/5可能触发下滚,底部1/5可能触发上滚
     * @作者 [pWX273343] 2015年6月30日
     */
    private int decodeTouchArea(MotionEvent ev) {
        if (ev.getY() > getHeight() * 4 / (double) 5) {
            return 1;
        } else if (ev.getY() < getHeight() / (double) 5) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * @param ev
     * @return
     * @描述 得到事件触发点, 摸到的是哪一个item
     * @作者 [pWX273343] 2015年7月6日
     */
    public int eventToPosition(MotionEvent ev) {

        if (ev != null) {
            int m = (int) ev.getX() / mColWidth;
            int n = (int) (ev.getY() + mCurrentY) / mColHeight;
            int position = n * mNumColumns + m;
            if (position >= mChildCount) {
                return mChildCount - 1;
            } else {
                return position;
            }
        }
        return 0;
    }

    // 这里把控件作为假的横向ListView,所以返回position跟高度无关,暂时这样
    // public int eventToPosition(MotionEvent ev) {
    //
    // if (ev != null) {
    // int m = (int) ev.getX() / mColWidth;
    // if (m >= mChildCount) {
    // return mChildCount - 1;
    // } else {
    // return m;
    // }
    // }
    // return 0;
    // }

    /**
     * @param dragPosition
     * @描述:复制一个镜像,并添加到透明层
     * @作者 [pWX273343] 2015年7月6日
     */
    private void copyView(int dragPosition) {
        hideView = mChilds.get(dragPosition);
        int realPosition = mGridView.indexOfChild(hideView);
        if (!adapter.isUseCopyView()) {
            mCopyView = adapter.getView(realPosition, mCopyView, mDragFrame);
        } else {
            mCopyView = adapter.copyView(realPosition, mCopyView, mDragFrame);
        }
        hideView.setVisibility(View.INVISIBLE);
        mDragFrame.addView(mCopyView, mColWidth, mColHeight);

        int[] l1 = new int[2];
        int[] l2 = new int[2];
        hideView.getLocationOnScreen(l1);
        mDragFrame.getLocationOnScreen(l2);

        // mCopyView.setX(hideView.getLeft());
        // mCopyView.setY(hideView.getTop() - mCurrentY);
        mCopyView.setX(l1[0] - l2[0]);
        mCopyView.setY(l1[1] - l2[1]);
        if (onDragSelectListener == null) {
            mCopyView.setScaleX(1.2f);
            mCopyView.setScaleY(1.2f);
        } else {
            onDragSelectListener.onDragSelect(mCopyView);
        }
    }

    /**
     * @param from
     * @param to
     * @描述:动画效果移动View
     * @作者 [pWX273343] 2015年6月24日
     */
    private void translateView(int from, int to) {
        View view = mChilds.get(from);
        int fromXValue = ((int[]) view.getTag(TAG_KEY))[0];
        int fromYValue = ((int[]) view.getTag(TAG_KEY))[1];
        int toXValue = to % mNumColumns - from % mNumColumns + fromXValue;
        int toYValue = to / mNumColumns - from / mNumColumns + fromYValue;
        Animation animation = new TranslateAnimation(1, fromXValue, 1, toXValue, 1, fromYValue, 1, toYValue);
        animation.setDuration(ANIM_DURING);
        animation.setFillAfter(true);
        view.setTag(TAG_KEY, new int[]{toXValue, toYValue});
        view.startAnimation(animation);
    }

    /**
     * @param from
     * @param to
     * @描述:拖动View使位置发生改变时
     * @作者 [pWX273343] 2015年7月6日
     */
    private void onDragPositionChange(int from, int to) {
        if (from > to) {
            for (int i = to; i < from; i++) {
                translateView(i, i + 1);
            }
        } else {
            for (int i = to; i > from; i--) {
                translateView(i, i - 1);
            }
        }
        if (!hasPositionChange) {
            hasPositionChange = true;
        }
        adapter.onDataModelMove(from, to);
        View view = mChilds.remove(from);
        mChilds.add(to, view);
        currentDragPosition = to;
    }

    /**
     * Function :setAdapter
     * <p/>
     * Author :[pWX273343] 2015年6月24日
     * <p/>
     * Description :设置适配器.该适配器必须实现一个方法,当view的位置发生变动时,对实际数据的改动
     *
     * @param adapter
     * @see GridView#setAdapter(android.widget.ListAdapter)
     */
    public void setAdapter(DragAdapter adapter) {
        if (this.adapter != null && observer != null) {
            this.adapter.unregisterDataSetObserver(observer);
        }
        this.adapter = adapter;
        mGridView.setAdapter(adapter);
        adapter.registerDataSetObserver(observer);
        mChildCount = adapter.getCount();
    }

    public int getNumColumns() {
        return mNumColumns;
    }

    /**
     * 每行几个
     */
    public void setNumColumns(int numColumns) {
        this.mNumColumns = numColumns;
        mGridView.setNumColumns(numColumns);
    }

    /**
     * 设置前几个item不可以改变位置
     */
    public void setNoPositionChangeItemCount(int count) {
        headDragPosition = count;
    }

    /**
     * 设置后几个item不可以改变位置
     */
    public void setFootNoPositionChangeItemCount(int count) {
        footDragPosition = count;
    }

    /**
     * 控制自动滚屏的动画监听器.
     */
    private ValueAnimator.AnimatorUpdateListener animUpdateListener = new ValueAnimator.AnimatorUpdateListener() {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int targetY = Math.round((Float) animation.getAnimatedValue());
            if (targetY < 0) {
                targetY = 0;
            } else if (targetY > mMaxHeight - getHeight()) {
                targetY = mMaxHeight - getHeight();
            }
            // mGridView.scrollTo(0, targetY);
            mScrollView.smoothScrollTo(0, targetY);
            // mCurrentY = targetY;
        }

    };

    /**
     * @param scrollStates
     * @描述:触摸区域改变,做相应处理,开始滚动或停止滚动
     * @作者 [pWX273343] 2015年6月29日
     */
    protected void onTouchAreaChange(int scrollStates) {
        if (!canScroll) {
            return;
        }
        if (animator != null) {
            animator.removeUpdateListener(animUpdateListener);
        }
        if (scrollStates == 1) {// 从普通区域进入触发向上滚动的区域
            int instance = mMaxHeight - getHeight() - mCurrentY;
            animator = ValueAnimator.ofFloat(mCurrentY, mMaxHeight - getHeight());
            animator.setDuration((long) (instance / 0.5f));
            animator.setTarget(mGridView);
            animator.addUpdateListener(animUpdateListener);
            animator.start();
        } else if (scrollStates == -1) {// 进入触发向下滚动的区域
            animator = ValueAnimator.ofFloat(mCurrentY, 0);
            animator.setDuration((long) (mCurrentY / 0.5f));
            animator.setTarget(mGridView);
            animator.addUpdateListener(animUpdateListener);
            animator.start();
        }
    }

    private OnDragSelectListener onDragSelectListener;

    /**
     * @描述:一个item view刚被拖拽和放下时起来生成镜像时调用.
     * @作者 [pWX273343] 2015年6月30日
     */
    public void setOnDragSelectListener(OnDragSelectListener onDragSelectListener) {
        this.onDragSelectListener = onDragSelectListener;
    }

    public interface OnDragSelectListener {
        /**
         * @param mirror 所拖拽起来的view生成的镜像 ,并不是实际的view.可对这个镜像实施变换效果,但是并不改变放下后的效果
         * @描述:拖拽起一个view时调用
         * @作者 [pWX273343] 2015年6月30日
         */
        void onDragSelect(View mirror);

        /**
         * @param itemView
         * @描述:拖拽的View放下时调用
         * @作者 [pWX273343] 2015年7月3日
         */
        void onPutDown(View itemView);
    }

    class NoScrollGridView extends GridView {

        public NoScrollGridView(Context context) {
            super(context);
        }

        /**
         * @return
         * @描述:兼容老版本的getColumWidth
         * @作者 [pWX273343] 2015年7月1日
         */
        public int getColumnWidth() {
            return getWidth() / getNumColumns();
        }

        public NoScrollGridView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, mExpandSpec);
        }
    }

    /**
     * Copyright (C), 2008-2015, Huawei Tech. Co., Ltd.
     * <p>
     * Description : 监听滚动的scrollview,我们需要实时知道他已滚动的距离
     *
     * @author [pWX273343] 2015年7月22日
     * @version V100R001
     * @since V100R001
     */
    class ListenScrollView extends ScrollView {
        public ListenScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            mCurrentY = getScrollY();
        }
    }

    public View getChildViewAtIndex(int index) {
        if (index < mChilds.size()) {
            return mChilds.get(index);
        }
        return null;
    }

    // 转交给gridview一些常用监听器
    private AdapterView.OnItemLongClickListener itemLongClickListener;

    /**
     * @param itemClickListener
     * @描述:item 转交给gridview一些常用监听器
     * @作者 [pWX273343] 2015年7月27日
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        mGridView.setOnItemClickListener(itemClickListener);
    }

    /**
     * 长按监听器自己触发,点击拖动模式不存在长按
     *
     * @param
     */
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;

    }

    /**
     * 点击拖动
     */
    public static final int DRAG_WHEN_TOUCH = 0;
    /**
     * 长按拖动
     */
    public static final int DRAG_BY_LONG_CLICK = 1;

    private int mDragMode = DRAG_WHEN_TOUCH;

    /**
     * @param mode int类型
     * @描述:设置拖动的策略是点击还是长按
     * @作者 [pWX273343] 2015年7月20日 参考 DRAG_WHEN_TOUCH,DRAG_BY_LONG_CLICK
     */
    public void setDragModel(int mode) {
        this.mDragMode = mode;
        isDragable = mode == DRAG_WHEN_TOUCH;
    }

    public View childAt(int index) {
        return mGridView.getChildAt(index);
    }

    public int childCount() {
        return mGridView.getChildCount();
    }

    public void setAnimFrame(FrameLayout mDragFrame) {
        this.mDragFrame = mDragFrame;
    }

    private OnTouchListener l;

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    private long dragLongPressTime = 600;

    /**
     * 设置长按需要用时
     *
     * @param time
     */
    public void setDragLongPressTime(long time) {
        dragLongPressTime = time;
    }

    /**
     * 设置触摸事件冲突父控件
     *
     * @param touchClashparent
     */
    public void setTouchClashparent(ViewGroup touchClashparent) {
        this.touchClashparent = touchClashparent;
    }
}
