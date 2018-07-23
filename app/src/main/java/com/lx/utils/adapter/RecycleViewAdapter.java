package com.lx.utils.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lx.utils.R;
import com.lx.utils.activity.RecycleViewActivity;
import com.lx.utils.model.Student;
import com.lx.utils.util.ItemTouchHelperAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by lixiao2 on 2018/3/2.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> implements ItemTouchHelperAdapter{
    private Context mContext;
    private List<RecycleViewActivity.Student> mDatas;
    private RecycleViewAdapter.ViewHolder mHolder = null;
    private Toast mToast;
    public RecycleViewAdapter(Context context, List<RecycleViewActivity.Student> datas){
        mContext = context;
        mDatas = datas;
    }

    private void showToast(String str) {
        if(mToast == null){
            mToast = Toast.makeText(mContext,str,Toast.LENGTH_SHORT);
        }
        mToast.setText(str);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType == RecyclerView.INVALID_TYPE) {
            v = LayoutInflater.from(mContext).inflate(R.layout.listitem_main_item,parent,false);
        }else{
            v = LayoutInflater.from(mContext).inflate(R.layout.listitem_recycler,parent,false);
        }
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecycleViewAdapter.ViewHolder holder, int position) {
        if(mDatas.get(position).isTitle){
            if(position != 0) {
                mHolder = holder;
            }
            return;
        }
        holder.mTv.setText(mDatas.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mDatas == null?0:mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mDatas.get(position).isTitle) {
            return  RecyclerView.INVALID_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        Log.i("Lx", "onItemMove: fromPosition="+fromPosition +";toPosition="+toPosition);
        if(toPosition == 0) {
            return;
        }
        if(fromPosition - toPosition > 0){
//            Toast.makeText(mContext,"titlePosition="+mHolder.getAdapterPosition(),Toast.LENGTH_SHORT).show();
            // 向上拖动
            if(toPosition <= mHolder.getAdapterPosition() && mHolder.getAdapterPosition() <= fromPosition){
                if(mHolder.getAdapterPosition() >= 8){
                    showToast("最多不能超过七个");
                    return;
                }
            }
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mDatas, i, i - 1);
            }
        } else {
            // 向下拖动
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mDatas, i, i + 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDissmiss(RecyclerView.ViewHolder source) {
        int position = source.getAdapterPosition();
        mDatas.remove(position); //移除数据
        notifyItemRemoved(position);//刷新数据移除
    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder viewHolder) {
        //当拖拽选中时放大选中的view
        viewHolder.itemView.setScaleX(2.0f);
        viewHolder.itemView.setScaleY(2.0f);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder viewHolder) {
        //拖拽结束后恢复view的状态
        viewHolder.itemView.setScaleX(1.0f);
        viewHolder.itemView.setScaleY(1.0f);
    }

    @Override
    public boolean getIsTitle(RecyclerView.ViewHolder source) {
        return mDatas.get(source.getAdapterPosition()).isTitle;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTv;
        public ViewHolder(View itemView) {
            super(itemView);
            this.mTv = (TextView) itemView.findViewById(R.id.txt);
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
            if(getItemViewType(position) == RecyclerView.INVALID_TYPE){
                // 如果方向是纵向的，视图将充满整个宽度，方向为横向，视图将充满整个高度。
                params.setFullSpan(true);
            }
        }
    }
}
