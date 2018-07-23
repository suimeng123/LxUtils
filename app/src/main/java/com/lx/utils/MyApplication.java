package com.lx.utils;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.*;
import android.support.v4.BuildConfig;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by lixiao2 on 2018/5/19.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
    }
}
