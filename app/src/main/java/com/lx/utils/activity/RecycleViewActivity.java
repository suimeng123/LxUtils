package com.lx.utils.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.lx.utils.R;
import com.lx.utils.adapter.MyLoadRefreshAdapter;
import com.lx.utils.adapter.MyLoadRefreshAdapter2;
import com.lx.utils.adapter.RecycleViewAdapter;
import com.lx.utils.util.ItemTouchHelperAdapter;
import com.lx.utils.util.SimpleItemTouchHelperCallback;
import com.lx.utils.view.ReflushRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixiao2 on 2018/3/2.
 */
public class RecycleViewActivity  extends Activity{
    private RecyclerView mRecyclerView;
    private List<Student> mDatas = new ArrayList<>();
//    private MyLoadRefreshAdapter2 mAdapter;
    private RecycleViewAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;

    public class Student {
        public int age;
        public String name;
        public boolean isTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);

//        // 通过h5 a标签打开并传递的参数
//        Intent intent = getIntent();
//        String action = intent.getAction();
//        if(Intent.ACTION_VIEW.equals(action)){
//            Uri uri =intent.getData();
//            if(uri != null){
//                //根据属性值获取数据
//                String gameid= uri.getQueryParameter("gameid");
//                String pkname= uri.getQueryParameter("pk_name");
//                Toast.makeText(this,"gameid="+gameid+",pk_name="+pkname,Toast.LENGTH_SHORT).show();
//            }
//        }

        mRecyclerView = (RecyclerView)findViewById(R.id.recycleView);
        initData(20);
        adapter = new RecycleViewAdapter(this,mDatas);
//        mAdapter = new MyLoadRefreshAdapter2(this,adapter);

        layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);//定义瀑布流管理器，第一个参数是列数，第二个是方向。
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);//不设置的话，图片闪烁错位，有可能有整列错位的情况。
        mRecyclerView.setLayoutManager(layoutManager);//设置瀑布流管理器
        // 设置adapter
        mRecyclerView.setAdapter(adapter);
        // 设置Item添加和移除的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置Item之间间隔样式
//        mRecyclerView.addItemDecoration(null);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    private Handler mHander = new Handler();

    private void initData(int a) {
        Student student = null;
        for(int i = 0;i < a;i++) {
            student = new Student();
            student.name = "学生"+i;
            if(i == 0 || i == 4) {
                student.isTitle = true;
            } else {
                student.isTitle = false;
            }
            mDatas.add(student);
        }
    }
}
