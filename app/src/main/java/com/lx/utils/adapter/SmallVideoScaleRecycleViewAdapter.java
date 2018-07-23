package com.lx.utils.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lx.utils.R;
import com.lx.utils.activity.SmallVideoScaleDetailActivity;
import com.lx.utils.util.ItemTouchHelperAdapter;

import java.util.List;

/**
 * Created by lixiao2 on 2018/7/10.
 */

public class SmallVideoScaleRecycleViewAdapter extends RecyclerView.Adapter<SmallVideoScaleRecycleViewAdapter.ViewHolder>{
    private Context mContext;
    private List mDatas;
    public SmallVideoScaleRecycleViewAdapter(Context context, List lists) {
        this.mContext = context;
        this.mDatas = lists;
    }
    @Override
    public SmallVideoScaleRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerviewitem_small_video_scale, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SmallVideoScaleRecycleViewAdapter.ViewHolder holder, final int position) {
        holder.iv.setImageResource((Integer) mDatas.get(position));
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int height = holder.iv.getMeasuredHeight();
                int width = holder.iv.getMeasuredWidth();
                int[] location = new int[2];
                v.getLocationInWindow(location);
                int left = location[0];
                int top = location[1] - getStateBarHeight(mContext);
                mContext.startActivity(new Intent(mContext, SmallVideoScaleDetailActivity.class)
                        .putExtra("id", (int)mDatas.get(position))
                        .putExtra("width",width)
                        .putExtra("height",height)
                        .putExtra("left",left)
                        .putExtra("top",top)
                );
                ((Activity)mContext).overridePendingTransition(0, 0);
            }
        });
    }

    // 获取状态栏高度
    public static int getStateBarHeight (Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public int getItemCount() {
        return mDatas!=null?mDatas.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        public ViewHolder(View view) {
            super(view);
            iv = view.findViewById(R.id.item_iv);
        }
    }
}
