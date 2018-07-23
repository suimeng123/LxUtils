package com.lx.utils.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lixiao2 on 2018/7/6.
 */

public class MViewHolder {
    private Context mContext;
    private View mParentView;

    private SparseArray<View> mViews;

    private int mPosition;

    public MViewHolder(Context context,View parentView, int layoutId) {
        this.mContext = context;
        mParentView = LayoutInflater.from(context).inflate(layoutId,null);
        mViews = new SparseArray<>();
        mParentView.setTag(this);
    }

    public static MViewHolder getInstance(Context context,View parentView, int layoutId,int position) {
        MViewHolder mViewHolder;
        if (parentView == null) {
            mViewHolder = new MViewHolder(context,parentView,layoutId);
        } else {
            mViewHolder = (MViewHolder) parentView.getTag();
        }
        mViewHolder.mPosition = position;
        return mViewHolder;
    }

    public View getParentView() {
        return mParentView;
    }

    public <T extends View> T getView(int id) {
        View view = mViews.get(id);
        if(view == null) {
            view = mParentView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }
    public MViewHolder setText(int id, String text){
        TextView t = getView(id);
        t.setText(text);
        return this;
    }

    public MViewHolder setImageResource(int id,int sid){
        ImageView iv = getView(id);
        iv.setImageResource(sid);
        return this;
    }

    public SparseArray<View> getViews(){
        return mViews;
    }

    public int getPosition() {
        return  mPosition;
    }
}
