package com.lx.utils.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lx.utils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixiao2 on 2018/7/6.
 */

public abstract class ABaseAdapter<T> extends BaseAdapter {
    public List<T> mDatas;
    public Context mContext;
    private int mLayoutId;
    private LeftDelView openLDV;
    public int mPosition = -1;
    public ABaseAdapter(Context context, List<T> lists, int layoutId) {
        mContext = context;
        if(lists == null) {
            mDatas = new ArrayList<>();
        }else{
            mDatas = lists;
        }
        mLayoutId = layoutId;
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MViewHolder mViewHolder = MViewHolder.getInstance(mContext,convertView,mLayoutId,position);
        final LeftDelView ldv = mViewHolder.getView(R.id.left_dv);
        ldv.mPosition = position;
        obtainView(mViewHolder,mDatas.get(position));
        if(mPosition == position){
            ldv.showBtn();
            openLDV = ldv;
        }else{
            ldv.closeBtn();
        }
        ldv.setOnLeftDeleteListener(new LeftDelView.LeftDeleteListener() {
            @Override
            public void onClose() {
            }

            @Override
            public void onOpen() {
                if(openLDV != null && openLDV != ldv) {
                    openLDV.closeBtn();
                }
                mPosition = position;
                openLDV = ldv;
            }

            @Override
            public void onDelete() {
                mPosition = -1;
                onLeftDelete((T)mDatas.get(position),position);
            }
        });
        return mViewHolder.getParentView();
    }

    public abstract void obtainView(MViewHolder mViewHolder, T item);

    public abstract void onLeftDelete(T item, int position);
}
