package com.lx.utils.view;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by lixiao2 on 2018/3/12.
 */
public class TwoPageLayout extends ViewGroup {
    private Scroller mScroller;
    private int mTouchSlop;
    public TwoPageLayout(Context context) {
        this(context,null);
    }

    public TwoPageLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TwoPageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
         super(context, attrs, defStyleAttr);
         mScroller = new Scroller(context);
         ViewConfiguration configuration = ViewConfiguration.get(context);
         // 获取TouchSlop值 触发移动的最小距离
         mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0;i<getChildCount();i++){
            // 为ScrollerLayout中的每一个子控件测量大小
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        for(int k = 0;k<getChildCount();k++){
            getChildAt(k).layout(0,k * getChildAt(k).getMeasuredHeight(),getChildAt(k).getMeasuredWidth(),(k+1)*getChildAt(k).getMeasuredHeight());
        }
        topBorder = getChildAt(0).getTop();
        bottomBorder = getChildAt(getChildCount()-1).getBottom();
        Log.i("Lx", "onLayout: topBorder="+topBorder + ",bottomBorder="+bottomBorder);
    }


    //上下边界
    int topBorder = 0;
    int bottomBorder = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getRawY();
                lastY = downY;
                Log.i("Lx", "onInterceptTouchEvent: lastY="+lastY);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();
                Log.i("Lx", "onInterceptTouchEvent: moveY="+moveY);
                lastY = moveY;
                int dy = (int) Math.abs(moveY-downY);
                Log.i("Lx", "onInterceptTouchEvent: dy="+dy);
                lastY = moveY;
                if (dy > mTouchSlop ) {
                    return true;
                }
                break;
        }
        return false;
    }

    int downY = 0;
    int moveY = 0;
    int lastY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                if (!mScroller.isFinished()) {
//                    mScroller.abortAnimation();
//                }
//                lastY = y;
//                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) event.getRawY();
                int dy = lastY - moveY;
//                Log.i("Lx", "dy: " + dy);
                if(getScrollY() + dy < topBorder) {
                    scrollTo(0,topBorder);
                    return true;
                }else if (getScrollY() + getHeight() + dy > bottomBorder){
                    scrollTo(0,bottomBorder - getHeight());
                    return true;
                }
                scrollBy(0,(int)(dy));
                lastY = moveY;
//                Log.i("Lx", "onTouchEvent: "+lastY);
                break;
            case MotionEvent.ACTION_UP:
                int targetIndex = (getScrollY() + getHeight()/2) / getHeight();
                int dxy = targetIndex * getHeight() - getScrollY();
                mScroller.startScroll(0,getScrollY(), 0, dxy);
                invalidate();
                break;
        }
        return true;
    }

    //第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();//当没滚动到需要的位置时，不断的重绘，形成动画
        }
    }

    public void scrollTo(int position) {
        if (mScroller != null) {
            int height = position * getHeight();
            int dy = height - getScrollY();
            mScroller.startScroll(0,getScrollY(), 0, dy,2000);
            invalidate();
        }
    }
}
