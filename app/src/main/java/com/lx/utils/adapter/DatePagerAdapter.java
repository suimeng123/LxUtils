package com.lx.utils.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lixiao2 on 2018/3/16.
 */
public class DatePagerAdapter extends PagerAdapter {

    private List<View> mViews;
    public DatePagerAdapter(List<View> lists){
        mViews = lists;
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //对超出范围的资源进行销毁
    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        // TODO Auto-generated method stub
        //super.destroyItem(container, position, object);
//        container.removeView(mViews.get(position % mViews.size()));
    }
    //对显示的资源进行初始化
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        //return super.instantiateItem(container, position);
        if(mViews.get(position % mViews.size()).getParent() == container){
            container.removeView(mViews.get(position % mViews.size()));
        }
        container.addView(mViews.get(position % mViews.size()));
        return mViews.get(position % mViews.size());
    }
}
