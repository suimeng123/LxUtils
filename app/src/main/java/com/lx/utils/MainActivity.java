package com.lx.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lx.utils.activity.BezierLineActivity;
import com.lx.utils.activity.DESActivity;
import com.lx.utils.activity.DataBaseActivity;
import com.lx.utils.activity.DateViewActivity;
import com.lx.utils.activity.IOActivity;
import com.lx.utils.activity.LeftScrollDeleteActivity;
import com.lx.utils.activity.MusicPlayerActivity;
import com.lx.utils.activity.MyStatusActivity;
import com.lx.utils.activity.OKHttpActivity;
import com.lx.utils.activity.RecycleViewActivity;
import com.lx.utils.activity.ReflushLinearLayoutActivity;
import com.lx.utils.activity.ScanningActivity;
import com.lx.utils.activity.SmallVideoScaleActivity;
import com.lx.utils.activity.SwitchActivity;
import com.lx.utils.activity.TwoPageViewActivity;
import com.wy.livetelecast.mylibrary.AliArouterActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MainActivity extends Activity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        // sqlitestudio 注册
//        SQLiteStudioService.instance().start(this);

        // 通过h5 a标签打开并传递的参数
//        Intent intent = getIntent();
//        String name = intent.getStringExtra("pname");
//        String age = intent.getStringExtra("age");
//        Toast.makeText(this,"name="+name+",age="+age,Toast.LENGTH_SHORT).show();
//        Glide.with(this).load(R.mipmap.ic_launcher).into(new ImageView(this));

    }

    public void OnClick(View v){
        switch (v.getId()){
            case R.id.btn1://RecyclerView组件
                startActivity(new Intent(this,RecycleViewActivity.class));
                break;
            case R.id.btn2://RecyclerView组件
                startActivity(new Intent(this,TwoPageViewActivity.class));
                break;
            case R.id.btn3://滑动开关
                startActivity(new Intent(this,SwitchActivity.class));
                break;
            case R.id.btn4://linearlayout布局上下拉刷新
                startActivity(new Intent(this,ReflushLinearLayoutActivity.class));
                break;
            case R.id.btn5://自定义日历
                startActivity(new Intent(this,DateViewActivity.class));
                break;
            case R.id.btn6://io流操作
                startActivity(new Intent(this,IOActivity.class));
                break;
            case R.id.btn7://io流操作
                PackageManager packageManager = getPackageManager();
                if (checkPackInfo(packname)) {
                    Intent intent = packageManager.getLaunchIntentForPackage(packname);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "没有安装" + packname, Toast.LENGTH_SHORT).show();
                }
//                loadApps();
                break;
            case R.id.btn8://加解密
                startActivity(new Intent(this,DESActivity.class));
                break;
            case R.id.btn9://okhttp使用
                startActivity(new Intent(this,OKHttpActivity.class));
                break;
            case R.id.btn10://okhttp使用
                startActivity(new Intent(this,DataBaseActivity.class));
                break;
            case R.id.btn11://阿里 ARouter使用
                startActivity(new Intent(this,AliArouterActivity.class));
                break;
            case R.id.btn12:// 仿微信朋友圈状态栏
                startActivity(new Intent(this,MyStatusActivity.class));
                break;
            case R.id.btn13:// 音乐播放器
                startActivity(new Intent(this,MusicPlayerActivity.class));
                break;
            case R.id.btn14:// 贝塞尔曲线
                startActivity(new Intent(this,BezierLineActivity.class));
                break;
            case R.id.btn15:// 扫描
                startActivity(new Intent(this,ScanningActivity.class));
                break;
            case R.id.btn16:// 左滑删除
                startActivity(new Intent(this,LeftScrollDeleteActivity.class));
                break;
            case R.id.btn17:// 小视频滑动放大缩小
                startActivity(new Intent(this,SmallVideoScaleActivity.class));
                break;
            default:
                break;
        }
    }

    String packname = "io.dcloud.HBuilder";//io.dcloud.PandoraEntry首页

    /**
     * 检查包是否存在
     *
     * @param packname
     * @return
     */
    private boolean checkPackInfo(String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    private void loadApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List apps = getPackageManager().queryIntentActivities(intent, 0);
        //排序
        Collections.sort(apps, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                return String.CASE_INSENSITIVE_ORDER.compare(
                        a.loadLabel(getPackageManager()).toString(),
                        b.loadLabel(getPackageManager()).toString()
                );
            }
        });
        //for循环遍历ResolveInfo对象获取包名和类名
        for (int i = 0; i < apps.size(); i++) {
            ResolveInfo info = (ResolveInfo) apps.get(i);
            String packageName = info.activityInfo.packageName;
            CharSequence cls = info.activityInfo.name;
            CharSequence name = info.activityInfo.loadLabel(getPackageManager());
            //log打印
            Log.e("Lx",name+"----"+packageName+"----"+cls);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // sqlitestudio 注销
//        SQLiteStudioService.instance().stop();
    }
}
