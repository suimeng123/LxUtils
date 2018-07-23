package com.lx.utils.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lx.utils.R;
import com.lx.utils.view.ABaseAdapter;
import com.lx.utils.view.LeftDelView;
import com.lx.utils.view.MViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiao2 on 2018/7/6.
 */

public class LeftDeleteListViewAdapter extends ABaseAdapter {

//    private Map<Object,MViewHolder> mViewHolders = new HashMap<>();

    public LeftDeleteListViewAdapter(Context context, List lists, int layoutId) {
        super(context, lists, layoutId);
    }


    @Override
    public void obtainView(final MViewHolder mViewHolder, Object item) {
//        if(mViewHolders.get(mViewHolder.getPosition()) == null) {
//            mViewHolders.put(mViewHolder.getPosition(),mViewHolder);
//        }
        final LeftDelView ldv = mViewHolder.getView(R.id.left_dv);
        TextView tv = mViewHolder.getView(R.id.item_text);
        tv.setText(item.toString());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ldv.closeBtn();
                Toast.makeText(mContext,"点击了第"+(mViewHolder.getPosition()) + "项",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLeftDelete(Object item,int position) {
        Toast.makeText(mContext,"删除了("+(item.toString()) + ")项",Toast.LENGTH_SHORT).show();
        mDatas.remove(position);
        notifyDataSetChanged();
    }
}
