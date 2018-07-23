package com.lx.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.lx.utils.R;
import com.lx.utils.util.DisplayUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by lixiao2 on 2018/4/25.
 */

public class BigView extends View {

    private Drawable src;
    private Bitmap bitmap;
    private int phoneWidth,phoneHeight,outWidth,outHeight;
    private Scroller mScroller;

    public BigView(Context context) {
        this(context, null);
    }

    public BigView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        phoneHeight = DisplayUtil.getAndroiodScreenProperty(context,true);
        phoneWidth = DisplayUtil.getAndroiodScreenProperty(context,false);
        mScroller = new Scroller(context);

        // 取图片Bitmap方法一（滑动效果快） 但是貌似只加载除了图片的3/4
        TypedArray array = null;
        //获得属性集合，并从属性集合中获得对应属性的资源
        array = getContext().obtainStyledAttributes(attrs, R.styleable.BigView);
        src = array.getDrawable(R.styleable.BigView_bigSrc);
        // drawable转bitmap
        BitmapDrawable bitmapDrawable = (BitmapDrawable) src;
        bitmap = bitmapDrawable.getBitmap();
        outWidth = bitmap.getWidth();
        outHeight = bitmap.getHeight();
        Log.i("lx", "outWidth:"+outWidth+" outHeight"+outHeight);
        if(array != null) {
            array.recycle();
        }


        // 取图片Bitmap方法二（滑动效果稍慢）可以将图片全部加载
//        try {
//            InputStream inputStream = context.getAssets().open("2.jpg");
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = false;
//            bitmap = BitmapFactory.decodeStream(inputStream,null,options);
//            outWidth = options.outWidth;
//            outHeight = options.outHeight;
//            Log.i("lx", "outWidth:"+outWidth+" outHeight"+outHeight);
//        } catch (java.io.IOException e) {
//            e.printStackTrace();
//        }




        // drawable转byte[]
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//
//        // 获取图片真实宽高
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        Bitmap bitmap2 = BitmapFactory.decodeByteArray(baos.toByteArray(),0,baos.toByteArray().length,options);
//        int outWidth = options.outWidth;
//        int outHeight = options.outHeight;



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect dist  = new Rect(0,0,phoneWidth,phoneHeight);
        Rect source = new Rect(outWidth/2 - phoneWidth/2 + dx,outHeight/2 - phoneHeight/2 +dy,outWidth/2 + phoneWidth/2 + dx,outHeight/2 + phoneHeight/2 + dy);
        canvas.drawBitmap(bitmap,source,dist,new Paint());
        invalidate();
    }

    int dx = 0;
    int dy = 0;
    int downX = 0;
    int downY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX() + dx;
                downY = (int) event.getY() + dy;
                Log.i("lx", "downX="+downX + ";downY="+downY);
                break;
            case MotionEvent.ACTION_MOVE:
                dx =  (downX - (int)event.getX());
                dy = (downY - (int)event.getY());
                Log.i("lx", "downX="+downX + ";downY="+downY +";X="+(int)event.getX()+";Y="+(int)event.getY());

                checkWidthOrHeight();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                dx = (downX - (int) event.getX());
                dy = (downY - (int) event.getY());
                Log.i("lx", "dx="+dx + ";dy="+dy);
                checkWidthOrHeight();
                invalidate();
                break;
        }
        return true;
    }

    private void checkWidthOrHeight() {
        if(dx<= (phoneWidth/2 - outWidth/2)){
            dx = (phoneWidth/2 - outWidth/2);
        }
        if(dx>=(outWidth/2 - phoneWidth/2)){
            dx = outWidth/2 - phoneWidth/2;
        }
        if(dy<=(phoneHeight/2 - outHeight/2)){
            dy = (phoneHeight/2 - outHeight/2);
        }
        if(dy>=(outHeight/2 - phoneHeight/2)){
            dy = outHeight/2 - phoneHeight/2;
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }
}
