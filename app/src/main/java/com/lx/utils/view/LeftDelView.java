package com.lx.utils.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.lx.utils.R;

/**
 * Created by lixiao2 on 2018/7/6.
 */

public class LeftDelView extends ViewGroup {

    // 按钮的宽度
    private final static int BTN_WIDTH = 200;

    //最大宽度
    private int maxWidth = 0;
    // 每次触摸每根手指都会拥有一个固定PointerId
    private int mPointerId;
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

    // 删除按钮是否显示
    private boolean isShowDeleteBtn = false;

    private Context mContext;

    private Button delBtn;

    public int mPosition = -1;

    public LeftDelView(Context context) {
        this(context, null);
    }

    public LeftDelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftDelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public interface LeftDeleteListener{
        void onClose();
        void onOpen();
        void onDelete();
    }
    private LeftDeleteListener ldl;
    public void setOnLeftDeleteListener (LeftDeleteListener l){
        this.ldl = l;
    }

    public LeftDelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        // 第一步，创建Scroller的实例
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        // 滚动速度计算器
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
//        addBtn(context);
    }

    private void addBtn(Context context) {
        delBtn = new Button(context);
        delBtn.setText("删除123");
        delBtn.setTextColor(Color.parseColor("#ffffff"));
        delBtn.setBackgroundColor(Color.parseColor("#ff0000"));
//        delBtn.setWidth(BTN_WIDTH);
        measureBtn();
        addView(delBtn);
        delBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ldl != null){
                    ldl.onDelete();
                }
            }
        });
    }

    private void measureBtn() {
        LayoutParams params = delBtn.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(BTN_WIDTH, LayoutParams.MATCH_PARENT);
            int widthSpec;
            if (params.width > 0) {
                widthSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.AT_MOST);
            } else {
                widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            }
            int heightSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.AT_MOST);
            delBtn.measure(widthSpec, heightSpec);
        }
    }

    private boolean isAddBtn = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isAddBtn) {
            addBtn(mContext);
            isAddBtn = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            measureChild(v, widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int w = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.layout(w, 0, w + v.getMeasuredWidth(), v.getMeasuredHeight());
            w += v.getMeasuredWidth();
//            Log.i("Lx", "w: " + w);
        }
        maxWidth = w;
//        Log.i("Lx", "maxWidth: " + maxWidth);
//        Log.i("Lx", "getWidth: " + getWidth());
    }


    int downX;
    int moveX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX() + getScrollX();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mPointerId = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) ev.getX();
                if (Math.abs(moveX - downX + getScrollX()) > mTouchSlop) {
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
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getX();
                if (moveX >= downX) {
                    scrollTo(0, 0);
                } else if (Math.abs(moveX - downX) >= (maxWidth - getWidth())) {
                    scrollTo(maxWidth - getWidth(), 0);
                } else {
                    scrollTo(downX - moveX, 0);
                }
                Log.i("Lx", "getScrollX(): " + getScrollX());
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                moveX = (int) event.getX();
                if(downX - moveX <= (maxWidth - getWidth()) / 2){
                    mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
                    isShowDeleteBtn = false;
                    if(ldl!=null) {
                        ldl.onClose();
                    }
                }else if(downX - moveX > (maxWidth - getWidth()) / 2) {
                    mScroller.startScroll(getScrollX(), 0, -getScrollX() + maxWidth - getWidth(), 0);
                    isShowDeleteBtn = true;
                    if(ldl!=null){
                        ldl.onOpen();
                    }
                }
                invalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    // 隐藏删除按钮
    public void closeBtn(){
        Log.i("Lx", "closeBtn: " +getScrollX() + ";" + mScroller.getCurrX() + ";" +mPosition);
        if(isShowDeleteBtn){
            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0,0);
            isShowDeleteBtn = false;
            postInvalidate();
        }
    }
    // 显示删除按钮
    public void showBtn(){
        mScroller.startScroll(getScrollX(), 0, -getScrollX() + maxWidth - getWidth(), 0,0);
        isShowDeleteBtn = true;
        postInvalidate();
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
