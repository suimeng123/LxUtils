package com.wy.livetelecast.mylibrary;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by lixiao2 on 2018/5/19.
 */

public class AliArouterActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_aliarouter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(AliArouterActivity.this.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AliArouterActivity.this, Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        }).start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(AliArouterActivity.this,"成功"+msg.arg1,Toast.LENGTH_SHORT).show();
        }
    };

    public void OnClick(View v){
        if(v.getId() == R.id.arouter_btn1){
            ARouter.getInstance().build("/test/activity").navigation();
        }
    }
}
