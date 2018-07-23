package com.lx.utils.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.lx.utils.R;

/**
 * Created by lixiao2 on 2018/6/29.
 */

public class ScanningActivity extends Activity {

    private final static int CAMERA_OK = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanning);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 没有照相机权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_OK);
        }else{
            // 已经有相机权限
        }

        findViewById(R.id.zbar_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // zbar扫描
                startActivity(new Intent(ScanningActivity.this, ZBarScanActivity.class));
            }
        });

        findViewById(R.id.zxing_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //zxing扫描
            }
        });

        findViewById(R.id.pic_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照
                startActivity(new Intent(ScanningActivity.this,PictureActivity.class));
            }
        });

        findViewById(R.id.video_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //录像
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 有权限

                } else {
                    // 没有权限
                    Toast.makeText(ScanningActivity.this, "您没有开启相机权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
