package com.lx.utils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.lx.utils.R;
import com.lx.utils.adapter.LeftDeleteRecycleViewAdapter;
import com.lx.utils.util.LeftDeleteItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixiao2 on 2018/7/10.
 */

public class RecycleViewLeftScrollDelteActivity extends Activity{

    private LeftDeleteRecycleViewAdapter adapter;
    private List<String> lists = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_left_delete);
        mRecyclerView = findViewById(R.id.recycleView);
        initData();

        //设置布局管理器
        //1、第一种LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //2、第二种 GridLayoutManager
//        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        //3、第三种
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        //设置布局的排版方向
//        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //绑定适配器
        adapter = new LeftDeleteRecycleViewAdapter(this,lists);
        mRecyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new LeftDeleteItemTouchHelperCallback(adapter);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void initData() {
        for(int i = 0; i < 30; i++) {
            lists.add("测试数据"+(i+1)*1000);
        }
    }
}
