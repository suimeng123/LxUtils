package com.lx.utils.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.lx.utils.R;

/**
 * Created by lixiao2 on 2018/6/8.
 */

public class MyStatusActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){ // 5.0以上
            setStatusBarUpperAPI21();
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){ // 4.4到5.0之间
            setStatusBarUpperAPI19();
        }
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_status);
        setContentView(R.layout.activity_my_floatview
        );

    }
    /**
     * 思路:直接设置状态栏的颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarUpperAPI21() {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.argb(0, 0, 0, 0));
    }

    /**
     * 思路:设置状态栏悬浮透明，然后制造一个和状态栏等尺寸的View设置好颜色填进去，就好像是状态栏着色了一样
     */
    private void setStatusBarUpperAPI19() {
        Window window = getWindow();
        //设置悬浮透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
//        int statusBarHeight = getStatusBarHeight(getApplicationContext());
//        int statusColor = getResources().getColor(R.color.bg_green);

//        View mTopView = mContentView.getChildAt(0);
//        if (mTopView != null && mTopView.getLayoutParams() != null &&
//                mTopView.getLayoutParams().height == statusBarHeight) {
////            mTopView.setBackgroundColor(statusColor);
//            return;
//        }
//
//        //制造一个和状态栏等尺寸的 View
//        mTopView = new View(this);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
////        mTopView.setBackgroundColor(statusColor);
//        //将view添加到第一个位置
//        mContentView.addView(mTopView, 0, lp);
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
