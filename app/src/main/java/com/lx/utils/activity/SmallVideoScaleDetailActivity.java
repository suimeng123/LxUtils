package com.lx.utils.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lx.utils.R;
import com.lx.utils.adapter.SmallVideoScaleRecycleViewAdapter;

/**
 * Created by lixiao2 on 2018/7/18.
 */

public class SmallVideoScaleDetailActivity extends Activity implements GestureDetector.OnGestureListener {

    private ImageView iv;
    private GestureDetector mGesture = null;
    private LinearLayout ll;
    private int listWidth;
    private int listHeight;
    private int listLeft;
    private int listTop;

    private int oldHeight;
    private int oldWidth;

    float scaleX ;
    float scaleY ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_video_scale_detail);
        int id = getIntent().getIntExtra("id", 0);
        listHeight = getIntent().getIntExtra("height", 0);
        listWidth = getIntent().getIntExtra("width", 0);
        listLeft = getIntent().getIntExtra("left", 0);
        listTop = getIntent().getIntExtra("top", 0);
        Log.i("Lx", "left: " + listLeft + ";top: " + listTop);

        WindowManager wm = this.getWindowManager();
        Point p = new Point();
        wm.getDefaultDisplay().getSize(p);
        scaleX = listWidth / (float)p.x;
        scaleY = listHeight / (float)p.y;
//        scaleY = scaleX;

        iv = findViewById(R.id.iv);
        ll = findViewById(R.id.ll);
        oldHeight = ll.getMeasuredHeight();
        oldWidth = ll.getMeasuredWidth();
        iv.setImageResource(id);
        setToOldSize();
        setToFullScreen();
        mGesture = new GestureDetector(this, this);
    }

    // 设置为列表页的尺寸
    private void setToOldSize() {
//        ViewGroup.LayoutParams params = ll.getLayoutParams();
//        params.height = listHeight;
//        params.width = listWidth;
//        ll.setLayoutParams(params);
//        ll.setTranslationX(listLeft);
//        ll.setTranslationY(listTop);
        ll.setPivotY(0);
        ll.setPivotX(0);
        ll.setScaleX(scaleX);
        ll.setScaleY(scaleY);
        ll.setTranslationX(listLeft);
        ll.setTranslationY(listTop);
    }

    // 全屏显示
    private void setToFullScreen() {
//        ViewGroup.LayoutParams params = ll.getLayoutParams();
//        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        ll.setLayoutParams(params);
//        ll.setTranslationX(0);
//        ll.setTranslationY(0);
        ValueAnimator animator = ValueAnimator.ofInt(listLeft,0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (int) animation.getAnimatedValue();
                ll.setTranslationX(x);
            }
        });
        ValueAnimator animator2 = ValueAnimator.ofInt(listTop,0);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int y = (int) animation.getAnimatedValue();
                ll.setTranslationY(y);
            }
        });
        ValueAnimator animator3 = ValueAnimator.ofFloat(scaleX,1);
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (float) animation.getAnimatedValue();
                // 以顶点为中心缩放
                ll.setPivotX(0);
                ll.setScaleX(x);
            }
        });
        ValueAnimator animator4 = ValueAnimator.ofFloat(scaleY,1);
        animator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float y = (float) animation.getAnimatedValue();
                // 以顶点为中心缩放
                ll.setPivotY(0);
                ll.setScaleY(y);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator).with(animator2).with(animator3).with(animator4);
        animatorSet.setDuration(3000);
        animatorSet.start();
    }

    // 回退到列表图
    private void backToList() {
        int[] location = new int[2];
        ll.getLocationInWindow(location);
        int bLeft = location[0];
        int bTop = location[1] - SmallVideoScaleRecycleViewAdapter.getStateBarHeight(this);
        float bScaleX = ll.getScaleX();
        float bScaleY = ll.getScaleY();
        ValueAnimator animator = ValueAnimator.ofInt(bLeft,listLeft);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (int) animation.getAnimatedValue();
                ll.setTranslationX(x);
            }
        });
        ValueAnimator animator2 = ValueAnimator.ofInt(bTop,listTop);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int y = (int) animation.getAnimatedValue();
                ll.setTranslationY(y);
            }
        });
        ValueAnimator animator3 = ValueAnimator.ofFloat(bScaleX,scaleX);
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (float) animation.getAnimatedValue();
                // 以顶点为中心缩放
                ll.setPivotX(0);
                ll.setScaleX(x);
            }
        });
        ValueAnimator animator4 = ValueAnimator.ofFloat(bScaleY,scaleY);
        animator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float y = (float) animation.getAnimatedValue();
                // 以顶点为中心缩放
                ll.setPivotY(0);
                ll.setScaleY(y);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator).with(animator2).with(animator3).with(animator4);
        animatorSet.setDuration(3000);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 去掉页面默认跳转动画
                SmallVideoScaleDetailActivity.this.finish();
                SmallVideoScaleDetailActivity.this.overridePendingTransition(0, 0);
            }
        });
        animatorSet.start();
    }


    float downX;
    float downY;
    float moveX;
    float moveY;
    float dx;
    float dy;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                moveY = event.getY();
                dx = moveX - downX;
                dy = moveY - downY;
                ll.setTranslationX(dx);
                ll.setTranslationY(dy);
//                Log.i("Lx", "scaleX: " + Math.abs(dx / ll.getMeasuredWidth()) +";scaleY:" + Math.abs(dy / ll .getMeasuredHeight()));
                // 等比缩放
                ll.setScaleX(1 - Math.abs(dy / ll.getMeasuredHeight()));
                ll.setScaleY(1 - Math.abs(dy / ll.getMeasuredHeight()));
                break;
            case MotionEvent.ACTION_UP:
                moveX = event.getX();
                moveY = event.getY();
                dx = moveX - downX;
                dy = moveY - downY;
                if (Math.abs(dy / ll.getMeasuredHeight()) > 0.2f || Math.abs(dx / ll.getMeasuredHeight()) > 0.2f) {
                    // 超过了一半
                    backToList();
                } else {
                    ll.setTranslationX(0);
                    ll.setTranslationY(0);
                    ll.setScaleX(1);
                    ll.setScaleY(1 );
                }
                break;
            default:
                break;
        }
        return true;
//        return mGesture.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
