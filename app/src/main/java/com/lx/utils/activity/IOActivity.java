package com.lx.utils.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.lx.utils.R;

/**
 * Created by lixiao2 on 2018/4/17.
 */

public class IOActivity extends Activity {
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_io);

        if(isSDK23()){
            if(!isGranted(Manifest.permission.READ_EXTERNAL_STORAGE) || !isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }
    }


    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.btn1: //文件操作
                startActivity(new Intent(mContext,FileOperateActivity.class));
                break;
            case R.id.btn2: //图片操作
                startActivity(new Intent(mContext,ImageOperateActivity.class));
                break;
        }
    }

    // 检查某个权限是否已授权
    private boolean isGranted(String permission){
        int checkPermission = ActivityCompat.checkSelfPermission(this,permission);
        if(checkPermission == PackageManager.PERMISSION_GRANTED){//已授权
            return true;
        }else {
            return  false;
        }
    }

    // 检查版本是否大于等于6.0
    private boolean isSDK23(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){// 大于等于6.0
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){//用户授权

                }else {//用户拒绝授权
                    finish();
                }
                break;
        }
    }
}
