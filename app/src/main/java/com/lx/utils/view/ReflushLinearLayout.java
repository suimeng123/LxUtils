package com.lx.utils.view;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import com.lx.utils.R;
import com.lx.utils.util.DisplayUtil;

/**
 * Created by lixiao2 on 2018/3/2.
 */
public class ReflushLinearLayout extends LinearLayout {
    private Context mContext;
    private View mHeaderView,mFooterView;
    private int mTouchSlop;

    private int downY,moveY,lastY;
    private int topBorder,bottomBorder;
    private int reflushHeight = 0;

    private ImageView mHeaderIv;
    private TextView mHeaderTv,mFooterTv;
    private ProgressBar mHeaderPb,mFooterPb;

    private boolean isReflush = false,isLoadMore = false;

    private Scroller mScroller;


    public ReflushLinearLayout(Context context) {
        this(context,null);
    }

    public ReflushLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReflushLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mHeaderView = LayoutInflater.from(context).inflate(R.layout.linearlayout_header,null);
        mFooterView = LayoutInflater.from(context).inflate(R.layout.linearlayout_footer,null);
        Log.i("Lx", "getChildCount: "+getChildCount());
        addView(mHeaderView,0);

        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值 触发移动的最小距离
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        reflushHeight = DisplayUtil.dip2px(mContext,30);
        scrollTo(0,reflushHeight );
    }

    private void initLayout() {
        mHeaderIv = (ImageView) mHeaderView.findViewById(R.id.header_iv);
        mHeaderTv = (TextView) mHeaderView.findViewById(R.id.header_tv);
        mHeaderPb = (ProgressBar) mHeaderView.findViewById(R.id.header_pb);
        mFooterTv = (TextView) mFooterView.findViewById(R.id.footer_tv);
        mFooterPb = (ProgressBar) mFooterView.findViewById(R.id.footer_pb);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if(!isStart) {
                addView(mFooterView, getChildCount());
                isStart = true;
            }
            for (int i = 0; i < getChildCount(); i++) {
                measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
            }

    }

    boolean isStart = false;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child = null;
        int newY = 0;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            child.layout(0, newY, child.getMeasuredWidth(), newY + child.getMeasuredHeight());
            newY += child.getMeasuredHeight();
        }
        topBorder = getChildAt(0).getTop();
        bottomBorder = getChildAt(getChildCount() - 1).getBottom();
//        Log.i("Lx", "bottomBorder: " + bottomBorder);
        initLayout();
    }

    private int dy = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                moveY = (int) event.getRawY();
                dy = lastY - moveY;

//                if (getScrollY()+getHeight() + dy >= bottomBorder){
//                    return true;
//                }

                if(!isReflush) {
                    if (getScrollY() + dy < -reflushHeight) {
                        mHeaderTv.setText("松开可刷新");
                        mHeaderIv.setVisibility(View.VISIBLE);
                        mHeaderPb.setVisibility(View.GONE);
                        mHeaderIv.setImageResource(R.mipmap.ic_pulltorefresh_arrow);
                    } else if (getScrollY() + dy >= -reflushHeight && getScrollY() + dy <= reflushHeight) {
                        mHeaderTv.setText("下拉去刷新");
                        mHeaderIv.setVisibility(View.VISIBLE);
                        mHeaderPb.setVisibility(View.GONE);
                        mHeaderIv.setImageResource(R.mipmap.ic_pulltorefresh_down);
                    }
                }

                scrollBy(0,dy);
                lastY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                moveY = (int) event.getRawY();
                int dif = getScrollY() + lastY-moveY;

                if (dif >= -reflushHeight && dif <= reflushHeight) {
//                        scrollTo(0, reflushHeight);
//                        mScroller.startScroll(0,getScrollY(), 0,reflushHeight -getScrollY());
//                        invalidate();
                    diffY = reflushHeight -getScrollY();
                } else if (dif < -reflushHeight) {
                    mHeaderIv.setVisibility(View.GONE);
                    mHeaderPb.setVisibility(View.VISIBLE);
                    mHeaderTv.setText("正在加载...");
//                        scrollTo(0, 0);
//                        mScroller.startScroll(0,getScrollY(), 0, -getScrollY());
//                        invalidate();
                    diffY = -getScrollY();
                    isReflush = true;
                }else if ((dif > bottomBorder - getHeight()-reflushHeight) && dif < bottomBorder - getHeight()) {//没满足上拉
//                        scrollTo(0, bottomBorder - getHeight() - reflushHeight);
//                        mScroller.startScroll(0,getScrollY(), 0, bottomBorder - getHeight()-reflushHeight-getScrollY());
//                        invalidate();
                    diffY = bottomBorder - getHeight()-reflushHeight-getScrollY();
                } else if (dif >= bottomBorder - getHeight()) {//满足上拉
//                        scrollTo(0, bottomBorder - getHeight());
//                        mScroller.startScroll(0,getScrollY(), 0, bottomBorder - getHeight()-getScrollY());
//                        invalidate();
                    diffY = bottomBorder - getHeight()-getScrollY();
                }

                mScroller.startScroll(0,getScrollY(), 0, diffY);
                invalidate();
                break;
        }
        return true;
    }

    int diffY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getRawY();
                lastY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) ev.getRawY();
                lastY = moveY;
                int diff = Math.abs(moveY - downY);
                if(diff > mTouchSlop){
                    return true;
                }
                break;
        }
        return false;
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
}

