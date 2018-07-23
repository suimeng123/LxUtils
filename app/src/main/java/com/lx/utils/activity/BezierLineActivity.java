package com.lx.utils.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.lx.utils.R;

/**
 * Created by lixiao2 on 2018/6/22.
 */

public class BezierLineActivity extends Activity{

    private ImageView imageView;
    private Path mPath;
    private PathMeasure pathMeasure;
    private ValueAnimator valueAnimator;

    private float[] mCurrentPosition = new float[2];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_line);

        imageView = findViewById(R.id.img);


        pathMeasure = new PathMeasure();
        mPath = new Path();
        mPath.moveTo(0,0);
        mPath.rQuadTo(800,0,800,1200);
        pathMeasure.setPath(mPath,false);
        valueAnimator = ValueAnimator.ofFloat(0,pathMeasure.getLength());
        valueAnimator.setDuration(3000);
        //匀速
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float val = (Float) animation.getAnimatedValue();
                pathMeasure.getPosTan(val,mCurrentPosition,null);
                imageView.setTranslationX(mCurrentPosition[0]);
                imageView.setTranslationY(mCurrentPosition[1]);
            }
        });
        valueAnimator.start();

        findViewById(R.id.reset_btn).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(valueAnimator!=null && valueAnimator.isRunning()){
                    return;
                }
                valueAnimator.start();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
