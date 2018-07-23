package com.lx.utils.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.lx.utils.R;
import com.lx.utils.adapter.DateAdapter;
import com.lx.utils.adapter.DatePagerAdapter;
import com.lx.utils.model.DateInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lixiao2 on 2018/3/15.
 */
public class DateView2 extends ViewGroup {

    private Context mContext;
    private View dateView;
    private ViewPager mViewPager;
    private DatePagerAdapter mAdapter;
    private List<View> mViews = new ArrayList<>();
    private View mView1,mView2,mView3;

    private GridView mGv ;
    private DateAdapter mDateAdapter;
    private TextView mTitleDate;
    private List mDateDatas = new ArrayList();

    private int mDy = 0;//选中的年默认为0 是今年 前一年-1 后一年加1 类推
    private int mDm = 0;//选中月默认为0 是当前月 前一月-1 后一月加1 类推
    private Date mToday = new Date();

    private Calendar mNowCalendar ;//当前选中的某月时间


    public DateView2(Context context) {
        this(context,null);
    }

    public DateView2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DateView2(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public DateView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        dateView = LayoutInflater.from(context).inflate(R.layout.date_view,null);
        addView(dateView);
        mViewPager = (ViewPager) dateView.findViewById(R.id.dv_vp);
        mView1 = LayoutInflater.from(context).inflate(R.layout.date_view_item,null);

        for(int x = 0;x<3;x++){
            mView1.setId(x+1000);
            mViews.add(mView1);
        }

        mGv = (GridView) mView1.findViewById(R.id.gv);
        mTitleDate = (TextView) mView1.findViewById(R.id.tv_date);
        // 上一月
        mView1.findViewById(R.id.tv_last_month).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mDm = mDm - 1;
                mDateDatas = getDateData();
                mDateAdapter = new DateAdapter(mContext,mDateDatas);
                mGv.setAdapter(mDateAdapter);
                mTitleDate.setText(mNowCalendar.get(Calendar.YEAR)+"年"+(mNowCalendar.get(Calendar.MONTH)+1)+"月");
            }
        });
        // 下一月
        mView1.findViewById(R.id.tv_next_month).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mDm = mDm + 1;
                mDateDatas = getDateData();

                mDateAdapter = new DateAdapter(mContext,mDateDatas);
                mGv.setAdapter(mDateAdapter);
                mTitleDate.setText(mNowCalendar.get(Calendar.YEAR)+"年"+(mNowCalendar.get(Calendar.MONTH)+1)+"月");
            }
        });
        //上一年
        mView1.findViewById(R.id.tv_last_year).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mDy = mDy - 1;
                mDateDatas = getDateData();

                mDateAdapter = new DateAdapter(mContext,mDateDatas);
                mGv.setAdapter(mDateAdapter);
                mTitleDate.setText(mNowCalendar.get(Calendar.YEAR)+"年"+(mNowCalendar.get(Calendar.MONTH)+1)+"月");
            }
        });
        //下一年
        mView1.findViewById(R.id.tv_next_year).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mDy = mDy + 1;
                mDateDatas = getDateData();

                mDateAdapter = new DateAdapter(mContext,mDateDatas);
                mGv.setAdapter(mDateAdapter);
                mTitleDate.setText(mNowCalendar.get(Calendar.YEAR)+"年"+(mNowCalendar.get(Calendar.MONTH)+1)+"月");
            }
        });
        mDateDatas = getDateData();
        mDateAdapter = new DateAdapter(context,mDateDatas);
        mGv.setAdapter(mDateAdapter);

        initViewPager();
    }

    private List getDateData() {
        List lists = new ArrayList();
        mNowCalendar = Calendar.getInstance();
        mNowCalendar.add(Calendar.YEAR,mDy);
        mNowCalendar.add(Calendar.MONTH,mDm);
        mNowCalendar.set(Calendar.DAY_OF_MONTH,1);
        int week = mNowCalendar.get(Calendar.DAY_OF_WEEK);
        mNowCalendar.set(Calendar.DAY_OF_MONTH,mNowCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int week2 = mNowCalendar.get(Calendar.DAY_OF_WEEK);


        if(week>1){
            Calendar calendar0 = Calendar.getInstance();
            calendar0.add(Calendar.YEAR,mDy);
            calendar0.add(Calendar.MONTH,mDm-1);
            for(int m=1;m<week;m++){
                calendar0.set(Calendar.DAY_OF_MONTH,calendar0.getActualMaximum(Calendar.DAY_OF_MONTH)-week+m+1);
                DateInfo d = new DateInfo();
                d.setSelected(false);
                d.setYear(calendar0.get(Calendar.YEAR));
                d.setMonth(calendar0.get(Calendar.MONTH)+1);
                d.setThisMonth(false);
                if(mDy==0 && (mDm-1)==0 && calendar0.get(Calendar.DATE) == mToday.getDate()){
                    d.setToday(true);
                }else {
                    d.setToday(false);
                }
                d.setDay(calendar0.get(Calendar.DATE));
                lists.add(d);
            }
        }

        for(int i=1;i<mNowCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)+1;i++){
            DateInfo d = new DateInfo();
            d.setSelected(false);
            d.setYear(mNowCalendar.get(Calendar.YEAR));
            d.setMonth(mNowCalendar.get(Calendar.MONTH)+1);
            d.setThisMonth(true);
            d.setDay(i);
            if(mToday.getDate() == i && mDy==0 && mDm==0){
                d.setToday(true);
            }else{
                d.setToday(false);
            }
            lists.add(d);
        }

        if(week2<7){
            Calendar calendar2 = Calendar.getInstance();
            calendar2.add(Calendar.YEAR,mDy);
            calendar2.add(Calendar.MONTH,mDm+1);
            for(int k=0;k<7-week2;k++){
                calendar2.set(Calendar.DAY_OF_MONTH,k+1);
                DateInfo d = new DateInfo();
                d.setSelected(false);
                d.setYear(calendar2.get(Calendar.YEAR));
                d.setMonth(calendar2.get(Calendar.MONTH)+1);
                d.setThisMonth(false);
                if(mDy==0 && (mDm+1)==0 && calendar2.get(Calendar.DATE) == mToday.getDate()){
                    d.setToday(true);
                }else {
                    d.setToday(false);
                }
                d.setDay(calendar2.get(Calendar.DATE));
                lists.add(d);
            }
        }

        return lists;
    }

    private Calendar getYearMonth(int dy,int dm){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,dy);
        calendar.add(Calendar.MONTH,dm);
        return calendar;
    }

    private void initViewPager() {
        mAdapter = new DatePagerAdapter(mViews);
        mViewPager.setAdapter(mAdapter);
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
