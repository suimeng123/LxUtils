package com.lx.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lixiao2 on 2018/6/22.
 */

public class BezierLineView extends View {

    private Path mPath;
    private Paint mPaint;

    public BezierLineView(Context context) {
        this(context, null);
    }

    public BezierLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public BezierLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
    }

    private int moveX = 0;
    private int moveY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getX();
                moveY = (int) event.getY();
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 圆的直径
     */
    private int ovalW = 100;

    // 当前滑动的坐标
    private float[] mCurrentPosition;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mPath.moveTo(100, 400);
//        mPath.quadTo(100, -150, 200, 0);
//        mPath.quadTo(100, 150, 200, 0);
//        mPath.lineTo(100, 400);
//        canvas.drawPath(mPath, mPaint);

//        if(moveX == 0 && moveY == 0){
//            canvas.drawOval(0,0,100,100,mPaint);
//        }else{
//            canvas.drawOval(moveX - ovalW/2,moveY - ovalW/2,moveX + ovalW/2,moveY + ovalW/2,mPaint);
//        }

        mPath.moveTo(100,100);
        mPath.rQuadTo(800,0,800,1200);
        mPath.lineTo(100,1300);
        canvas.drawPath(mPath,mPaint);
    }
}
