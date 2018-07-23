package com.lx.utils.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lx.utils.R;
import com.lx.utils.util.ItemTouchHelperAdapter;

import java.util.List;

/**
 * Created by lixiao2 on 2018/7/10.
 */

public class LeftDeleteRecycleViewAdapter extends RecyclerView.Adapter<LeftDeleteRecycleViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private List mDatas;
    public LeftDeleteRecycleViewAdapter(Context context, List lists) {
        this.mContext = context;
        this.mDatas = lists;
    }
    @Override
    public LeftDeleteRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
//        if(viewType == 1) {
            view = LayoutInflater.from(mContext).inflate(R.layout.recyclerviewitem_left_delete, parent, false);
//        } else {
//            view = LayoutInflater.from(mContext).inflate(R.layout.recyclerviewitem_left_delete2,parent,false);
//        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeftDeleteRecycleViewAdapter.ViewHolder holder, int position) {
        holder.name.setText(mDatas.get(position).toString());
    }

    @Override
    public int getItemViewType(int position) {
        if(position % 3 == 1) {
            return 1;
        } else {
            return 2;
        }
//        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mDatas!=null?mDatas.size():0;
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {

    }

    @Override
    public void onItemDissmiss(RecyclerView.ViewHolder source) {

    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder source) {

    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder source) {

    }

    @Override
    public boolean getIsTitle(RecyclerView.ViewHolder source) {
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        public Button btn;
        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_name);
            btn = view.findViewById(R.id.item_btn);
        }
    }
}
