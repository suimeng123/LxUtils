package com.lx.utils.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lx.utils.R;
import com.lx.utils.util.CommUtils;
import com.lx.utils.util.DisplayUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by lixiao2 on 2018/4/25.
 */

public class ImagePartActivity extends Activity{

    private ImageView big_img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_image);
        big_img = findViewById(R.id.big_img);


//        int height = DisplayUtil.getAndroiodScreenProperty(this,true);
//        int width = DisplayUtil.getAndroiodScreenProperty(this,false);
//
//        try {
//            InputStream inputStream = getAssets().open("2.jpg");
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(inputStream,null,options);
//            int outwidth = options.outWidth;
//            int outHeight = options.outHeight;
//            Log.i("lx", "outwidth="+outwidth+" outHeight="+outHeight+" height="+height+" width="+width);
//
//
//            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(inputStream,false);
//            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            Bitmap bitmap = decoder.decodeRegion(new Rect(outwidth/2 - width/2 - 20,outHeight/2 - height/2,outwidth/2 + width/2 + 20,outHeight/2 + height/2),options);
//            big_img.setImageBitmap(bitmap);
//        } catch (java.io.IOException e) {
//            e.printStackTrace();
//        }
    }

}
