package com.lx.utils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.lx.utils.R;
import com.lx.utils.adapter.LeftDeleteListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixiao2 on 2018/7/6.
 */

public class ListViewLeftScrollDeleteActivity extends Activity {
    private ListView listView;
    private LeftDeleteListViewAdapter adapter;
    private List<String> lists;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_left_delete);
        listView = findViewById(R.id.listview);
        initData();
        adapter = new LeftDeleteListViewAdapter(this,lists,R.layout.listitem_listview);
        listView.setAdapter(adapter);
    }

    private void initData() {
        lists = new ArrayList();
        for(int i = 0;i <30;i++){
            lists.add("测试数据"+i);
        }
    }
}
