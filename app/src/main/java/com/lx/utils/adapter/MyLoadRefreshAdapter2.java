package com.lx.utils.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lx.utils.R;
import com.lx.utils.util.ItemTouchHelperAdapter;

import java.util.ArrayList;

/**
 * 实现显示头部和尾部item的adapter,把头部尾部的事情交给这个adapter来做,其他的交给子adapter
 * Created by SwmIsMe on 2017/3/2.
 */
public class MyLoadRefreshAdapter2 extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    public ArrayList<View> headerViews = new ArrayList<>();
    public ArrayList<View> footViews = new ArrayList<>();
    public RecycleViewAdapter adapter;
    private Context mContext;

    public MyLoadRefreshAdapter2(Context context, RecycleViewAdapter adapter) {
        this.adapter = adapter;
        this.mContext = context;
        View header = LayoutInflater.from(context).inflate(R.layout.recycleview_header,null);
        View footer = LayoutInflater.from(context).inflate(R.layout.recycleview_footer,null);
        this.headerViews.add(header);
        this.footViews.add(footer);
    }

    MyLoadRefreshListener mLoadRefreshListener;

    public MyLoadRefreshListener getLoadRefreshListener() {
        return mLoadRefreshListener;
    }

    public void setLoadRefreshListener(MyLoadRefreshListener loadRefreshListener) {
        mLoadRefreshListener = loadRefreshListener;
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        adapter.onItemMove(source,target);
    }

    @Override
    public void onItemDissmiss(RecyclerView.ViewHolder source) {
        adapter.onItemDissmiss(source);
    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder source) {
        adapter.onItemSelect(source);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder source) {
        adapter.onItemClear(source);
    }

    @Override
    public boolean getIsTitle(RecyclerView.ViewHolder source) {
        return false;
    }

    public interface MyLoadRefreshListener {
        //        获取现在item的类型
        void getNowItemType(int itemPosition);
    }

    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerView.INVALID_TYPE) {
            //头部item
            return new RecycleViewAdapter.ViewHolder(headerViews.get(0)) {
            };
        } else if (viewType == (RecyclerView.INVALID_TYPE - 1)) {
            //尾部item
            return new RecycleViewAdapter.ViewHolder(footViews.get(0)) {
            };
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecycleViewAdapter.ViewHolder holder, int position) {
        if (position >= 0 && position < headerViews.size()) {
            return;
        }

//        交给 其他的适配器处理
        if (adapter != null) {
            int p = position - headerViews.size();
            if (p < adapter.getItemCount()) {
                adapter.onBindViewHolder(holder, p);
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position < headerViews.size()) {
            //如果是头部则返回一个不可用的标识,表示这是头部item
            return RecyclerView.INVALID_TYPE;
        }

        if (adapter != null) {
            int p = position - headerViews.size();
            if (p < adapter.getItemCount()) {
                return adapter.getItemViewType(p);
            }
        }

        return RecyclerView.INVALID_TYPE - 1;//默认返回表示是尾部的item
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    public int getCount() {
        int count = headerViews.size() + footViews.size();
        if (adapter != null) {
            count += adapter.getItemCount();
        }
        return count;
    }

    //用于GridLayoutManager header footer只显示一行
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager layout = ((GridLayoutManager) manager);
            GridLayoutManager.SpanSizeLookup mGridSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    Log.e("GridSpanSizeLookup", "GridSpanSizeLookup:" + position);
                    if (getItemViewType(position) == RecyclerView.INVALID_TYPE || getItemViewType(position) == RecyclerView.INVALID_TYPE -1) {
                        //The number of spans occupied by the item at the provided position，Default Each item occupies 1 span.
                        //在某个位置的item所占用的跨度的数量，默认情况下占用一个跨度。
                        return 1;
                    } else {
                        return layout.getSpanCount();
                    }
                }
            };
            layout.setSpanSizeLookup(mGridSpanSizeLookup);
        }
    }

    //用于StaggeredGridLayoutManager header footer只显示一行
    @Override
    public void onViewAttachedToWindow(RecycleViewAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if(layoutParams!=null&&layoutParams instanceof StaggeredGridLayoutManager.LayoutParams){
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            int position = holder.getLayoutPosition();
            if(getItemViewType(position) == RecyclerView.INVALID_TYPE || getItemViewType(position) == RecyclerView.INVALID_TYPE -1 || position == 4){
                // 如果方向是纵向的，视图将充满整个宽度，方向为横向，视图将充满整个高度。
                params.setFullSpan(true);
            }
        }
    }
}