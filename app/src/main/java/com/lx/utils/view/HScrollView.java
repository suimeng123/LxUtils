package com.lx.utils.view;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by lx on 2018/6/13.
 */

public class HScrollView extends ViewGroup {

    //最大高度
    private int maxHeight = 0;

    /**
     * 用于完成滚动操作的实例
     */
    private Scroller mScroller;

    /**
     * 判定为拖动的最小移动像素数
     */
    private int mTouchSlop;

    private int mMaxVelocity;//最大速度
    // 速度计算器
    private VelocityTracker mVelocityTracker;

    // 每次触摸每根手指都会拥有一个固定PointerId
    private int mPointerId;

    public HScrollView(Context context) {
        this(context, null);
    }

    public HScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        // 第一步，创建Scroller的实例
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        // 滚动速度计算器
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量所有可见的child布局
        int childLenght = getChildCount();
        for (int i = 0; i < childLenght; i++) {
            View view = getChildAt(i);
            if(view.getVisibility() == View.VISIBLE){
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int start = 0;
        // 将所有子布局在父布局中排序好
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if(view.getVisibility() == View.VISIBLE) {
                view.layout(0, start, getMeasuredWidth(), start + view.getMeasuredHeight());
                start += view.getMeasuredHeight();
            }
        }
        maxHeight = start;
    }


    int downY;
    int moveY;
    // Y方向移动的距离
    int lastMoveY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 获取当前已经移动的距离
                lastMoveY = mScroller.getCurrY();
                downY = (int) event.getRawY() + lastMoveY;
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) event.getRawY();
                int dy = Math.abs(moveY - downY);
                // 大于最小滑动距离
                if(dy >= mTouchSlop){
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        obtainVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mPointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) event.getRawY();
                if(downY - moveY <= 0) {
                    // 向下滑动超过最小值时 增加阻力
                    moveY = downY - (downY - moveY)/3 ;
                }else if(downY - moveY >= maxHeight-getHeight()){
                    // 向上滑动超过最大值时 增加阻力
                    moveY = moveY + (downY - moveY - maxHeight + getHeight())*2/3;
                }
                scrollTo(0,downY - moveY);
                break;
            case MotionEvent.ACTION_UP:
                moveY = (int) event.getRawY();
                lastMoveY = downY - moveY;
                if(lastMoveY <= 0 ) {
                    //向下滑动超过最小值时回到原点
                    lastMoveY = 0;
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                    postInvalidate();
                }else if(lastMoveY >= maxHeight-getHeight()) {
                    //向上滑动超过最大值回到最大值位置
                    lastMoveY = maxHeight - getHeight();
                    mScroller.startScroll(0, getScrollY(), 0, maxHeight - getHeight() - getScrollY());
                    postInvalidate();
                }else {
                    // 在最小值和最大高度之间的惯性滑动
                    //计算1000ms的速度
                    mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                    //获取y在mPointerId上的的速度
                    final float velocityY = mVelocityTracker.getYVelocity(mPointerId);
                    // 滚动一段距离
                    mScroller.fling(0,getScrollY(),0,(int)-velocityY,0,0,0,maxHeight-getHeight()-100);
                    recycleVelocityTracker();
                    postInvalidate();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 创建新的速度监视对象
     *
     * @param event 滑动事件
     */
    private void obtainVelocityTracker(MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 释放资源
     */
    private void recycleVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

}
