package com.lx.utils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.lx.utils.R;
import com.lx.utils.adapter.LeftDeleteRecycleViewAdapter;
import com.lx.utils.adapter.SmallVideoScaleRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixiao2 on 2018/7/18.
 */

public class SmallVideoScaleActivity extends Activity{

    private RecyclerView mRecyclerView;
    private SmallVideoScaleRecycleViewAdapter adapter;
    private List lists = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_video_scale);

        mRecyclerView = findViewById(R.id.recycleView);
        initData();

        //设置布局管理器
        //1、第一种LinearLayoutManager
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //2、第二种 GridLayoutManager
//        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        //3、第三种
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        //设置布局的排版方向
//        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //绑定适配器
        adapter = new SmallVideoScaleRecycleViewAdapter(this,lists);
        mRecyclerView.setAdapter(adapter);
    }

    private void initData() {
        for(int i = 0; i < 30; i++) {
           if(i % 3 == 0) {
               lists.add(R.mipmap.p1);
           } else if (i % 3 == 1){
               lists.add(R.mipmap.p2);
           } else {
               lists.add(R.mipmap.p3);
           }
        }
    }
}
