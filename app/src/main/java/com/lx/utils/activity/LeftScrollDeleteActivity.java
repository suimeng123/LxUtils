package com.lx.utils.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lx.utils.R;
import com.lx.utils.view.LeftDelView;

/**
 * Created by lixiao2 on 2018/7/6.
 * 左滑删除
 */

public class LeftScrollDeleteActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_delete);

        findViewById(R.id.list_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LeftScrollDeleteActivity.this,ListViewLeftScrollDeleteActivity.class));
            }
        });

        findViewById(R.id.recycle_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LeftScrollDeleteActivity.this,RecycleViewLeftScrollDelteActivity.class));
            }
        });
    }
}
