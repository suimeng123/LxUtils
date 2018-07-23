package com.lx.utils.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lx.utils.R;
import com.lx.utils.adapter.DatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixiao2 on 2018/3/15.
 */
public class DateView extends ViewGroup {

    private Context mContext;
    private View dateView;
    private ViewPager mViewPager;
    private DatePagerAdapter mAdapter;
    private List<View> mViews = new ArrayList<>();
    private DateMonthView mView1,mView2,mView3;

    private int startPosition = Integer.MAX_VALUE/2;
    private int nowPosition = startPosition,nowMdm = 0;//nowMdm偏差值即点击前后一月或一年按钮产生的

    public DateView(Context context) {
        this(context,null);
    }

    public DateView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DateView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public DateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        dateView = LayoutInflater.from(context).inflate(R.layout.date_view,null);
        addView(dateView);

        // viewpager 匀速滚动
//        try {
//            Field field = ViewPager.class.getDeclaredField("mScroller");
//            field.setAccessible(true);
//            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(),
//                    new AccelerateInterpolator());
//            field.set(mViewPager, scroller);
//            scroller.setmDuration(2000);
//        } catch (Exception e) {
//
//        }

        mViewPager = (ViewPager) dateView.findViewById(R.id.dv_vp);
        mView1 = new DateMonthView(context,attrs,defStyleAttr,defStyleRes,0,0,this);
        mView2 = new DateMonthView(context,attrs,defStyleAttr,defStyleRes,0,1,this);
        mView3 = new DateMonthView(context,attrs,defStyleAttr,defStyleRes,0,2,this);
        mViews.add(mView1);
        mViews.add(mView2);
        mViews.add(mView3);

        initViewPager();
    }


    private void initViewPager() {
        mAdapter = new DatePagerAdapter(mViews);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(Integer.MAX_VALUE/2);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("Lx", "onPageSelected: position="+position);
                nowPosition = position;
                initViewData();
//                mViewPager.setCurrentItem(nowPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initViewData() {
        if(nowPosition % 3 == 0){
            mView1.setNowDate(0,nowPosition-startPosition+nowMdm);
            mView2.setNowDate(0,nowPosition-startPosition+1+nowMdm);
            mView3.setNowDate(0,nowPosition-startPosition-1+nowMdm);
        }else if(nowPosition % 3 == 1){
            mView1.setNowDate(0,nowPosition-startPosition-1+nowMdm);
            mView2.setNowDate(0,nowPosition-startPosition+nowMdm);
            mView3.setNowDate(0,nowPosition-startPosition+1+nowMdm);
        }else if(nowPosition % 3 == 2){
            mView1.setNowDate(0,nowPosition-startPosition+1+nowMdm);
            mView2.setNowDate(0,nowPosition-startPosition-1+nowMdm);
            mView3.setNowDate(0,nowPosition-startPosition+nowMdm);
        }
    }


    public void onLastMonth(){
        nowMdm = nowMdm - 1;
        initViewData();
    }
    public void onLastYear(){
        nowMdm = nowMdm - 12;
        initViewData();
    }
    public void onNextMonth(){
        nowMdm = nowMdm + 1;
        initViewData();
    }
    public void onNextYear(){
        nowMdm = nowMdm + 12;
        initViewData();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for(int i=0;i<getChildCount();i++){
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int height = 0;
        View child;
        for(int k=0;k<getChildCount();k++){
            child = getChildAt(k);
            child.layout(0,height,child.getMeasuredWidth(),height+child.getMeasuredHeight());
            height += child.getMeasuredHeight();
        }
    }
}
