package com.lx.utils.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lx.utils.R;
import com.lx.utils.model.DateInfo;

import java.util.List;

/**
 * Created by lixiao2 on 2018/3/15.
 */
public class DateAdapter extends BaseAdapter {
    private List mData;
    private Context mContext;

    public DateAdapter(Context context,List list){
        mData = list;
        mContext = context;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.griditem_date_view,null);
            viewHolder.text = (TextView) view.findViewById(R.id.text);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final DateInfo item = (DateInfo) mData.get(i);
        viewHolder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Lx", "date: "+item.getYear()+'-'+item.getMonth()+'-'+item.getDay());
//                Toast.makeText(mContext,item.getYear()+'-'+item.getMonth()+'-'+item.getDay(),Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.text.setText(item.getDay()+"");
        if(item.isThisMonth()){
            viewHolder.text.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            viewHolder.text.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
        if(item.isToday()){
            viewHolder.text.setTextColor(Color.parseColor("#0000ff"));
        }else{
            viewHolder.text.setTextColor(Color.parseColor("#666666"));
        }

        return view;
    }

    class ViewHolder{
        TextView text;
    }
}
