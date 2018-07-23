package com.lx.utils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.lx.utils.R;
import com.lx.utils.activity.zbar.CameraManagerUtils;

/**
 * Created by lixiao2 on 2018/7/4.
 * 拍摄照片
 */

public class PictureActivity extends Activity {
    CameraManagerUtils utils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);


        SurfaceView surfaceView = new SurfaceView(this);
        FrameLayout preview = (FrameLayout)findViewById(R.id.fl);
        preview.addView(surfaceView);
        utils = new CameraManagerUtils(this,surfaceView,new Handler());

        findViewById(R.id.take_pic_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.captureStillPicture();
            }
        });
    }
}
