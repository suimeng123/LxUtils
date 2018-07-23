package com.lx.utils.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.lx.utils.R;
import com.lx.utils.util.CommUtils;
import com.lx.utils.util.DisplayUtil;

/**
 * Created by lixiao2 on 2018/4/25.
 */

public class ImageOperateActivity extends Activity{

    private ImageView source_img,big_img;
    private EditText bs_ed;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_image);
        source_img = findViewById(R.id.source_img);
        big_img = findViewById(R.id.big_img);
        bs_ed = findViewById(R.id.bs_ed);
    }


    // 根据比例设置图片
    private void setProportionOptions(int x){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = x;// 压缩比例
        Bitmap bitmap = BitmapFactory.decodeFile(CommUtils.getSDCardUrl().toString()+"/1.jpg",options);
        if(bitmap != null){
            source_img.setImageBitmap(bitmap);
        }
    }

    // 根据手机宽高来适配图片
    private void toPhoneHeightOrWidth(){
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只获取图片的宽高 不加载图片内容
        Bitmap bitmap = BitmapFactory.decodeFile(CommUtils.getSDCardUrl().toString()+"/1.jpg",options);
        int mWidth = options.outWidth;// 获取图片真实宽度
        int mHeight = options.outHeight; // 获取图片真实高度
        int dx = mHeight/height;
        int dy = mWidth/width;
        Log.i("lx", "mWidth="+mWidth+" mheight="+mHeight+" height="+height+" width="+width);
        if(dx < dy && dy > 1){
            options.inSampleSize = dy;
        }else if(dy < dx && dx > 1){
            options.inSampleSize = dy;
        }else{
            options.inSampleSize = 1;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap1 = BitmapFactory.decodeFile(CommUtils.getSDCardUrl()+"/1.jpg",options);
        source_img.setImageBitmap(bitmap1);
    }


    public void OnClick(View v){
        switch (v.getId()){
            case R.id.sure_btn://缩放
                int x = 1;
                if(!TextUtils.isEmpty(bs_ed.getText().toString())){
                    x = Integer.parseInt(bs_ed.getText().toString());
                }
                setProportionOptions(x);
                break;
            case R.id.sure_btn2:// 适配手机
                toPhoneHeightOrWidth();
                break;
            case R.id.sure_btn3: // 翻入大图
                startActivity(new Intent(ImageOperateActivity.this,ImagePartActivity.class));
                break;
        }
    }
}
