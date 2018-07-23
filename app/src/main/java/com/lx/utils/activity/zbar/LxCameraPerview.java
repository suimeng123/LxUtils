package com.lx.utils.activity.zbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

/**
 * Created by lixiao2 on 2018/6/29.
 */

public class LxCameraPerview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;

    public LxCameraPerview(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
