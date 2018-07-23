package com.lx.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lx.utils.R;

/**
 * Created by lixiao2 on 2018/3/13.
 */
public class SwitchView extends View {

    // 背景图片
    private Drawable srcb;
    //开关图片
    private Drawable src;
    //背景图片高度
    private int bHeight;
    // 背景图片宽度
    private int bWidth;
    // 开关图片宽度
    private int sWidth;

    //背景图片Bitmap
    Bitmap bp ;
    // 开关图片Bitmap
    Bitmap bp2 ;
    // 当前开关停留的x位置
    private int moveX;
    // 用户按下时的位置
    private int downX;

    private int type = 1;//1是开关在左边 2是开关在右边

    public SwitchView(Context context) {
        this(context,null);
    }

    public SwitchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
         bp = ((BitmapDrawable)srcb).getBitmap();
         bp2 = ((BitmapDrawable)src).getBitmap();
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray array = null;
        try {
            //获得属性集合，并从属性集合中获得对应属性的资源
            array = getContext().obtainStyledAttributes(attrs,R.styleable.SwitchView);
            srcb = array.getDrawable(R.styleable.SwitchView_srcb);
            src = array.getDrawable(R.styleable.SwitchView_src);
            measureDrawble();
        }finally {
            if(array != null){
                array.recycle();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: //按下操作
                downX = (int) event.getX();
                // 按下的可触摸范围 只有最左边和最右边开关按钮范围可触摸
                if((downX>sWidth && downX<(bWidth-sWidth)) || downX>bWidth || event.getY()>bHeight){
                    return false;
                }
                if(downX< bWidth/2){ // 判断
                    type = 1;
                }else {
                    type = 2;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getX();
                if(type == 1){
                    if(moveX - downX <= 0) {
                        moveX = 0;
                    } else if (moveX - downX >= bWidth-sWidth){
                        moveX = bWidth-sWidth;
                    }else {
                        moveX = moveX - downX;
                    }
                }else if (type == 2){
                    if (moveX - downX >= 0){
                        moveX = bWidth-sWidth;
                    }else if(Math.abs(moveX-downX)>=bWidth -sWidth){
                        moveX = 0;
                    }else {
                        moveX = moveX - (sWidth - (bWidth-downX));
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                if (type == 1){
                    if(upX-downX > (bWidth-sWidth)/2){
                        moveX =bWidth - sWidth;
                    }else{
                        moveX = 0;
                    }
                }else if(type==2){
                    if(Math.abs(upX-downX) < (bWidth-sWidth)/2){
                        moveX = bWidth - sWidth;
                    }else{
                        moveX = 0;
                    }
                }
                invalidate();
                break;
        }
        return true;
    }

    private void measureDrawble() {
        if (src != null) {
            sWidth = src.getIntrinsicWidth();
        }
        if (srcb != null) {
            bHeight = srcb.getIntrinsicHeight();
            bWidth = srcb.getIntrinsicWidth();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bp,0,0,new Paint());
        canvas.drawBitmap(bp2,moveX,0,new Paint());
        super.onDraw(canvas);
    }

}
