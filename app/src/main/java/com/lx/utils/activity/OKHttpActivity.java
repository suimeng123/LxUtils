package com.lx.utils.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lx.utils.R;
import com.lx.utils.util.CommUtils;
import com.lx.utils.util.OKHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import okhttp3.OkHttpClient;

/**
 * Created by lixiao2 on 2018/4/27.
 */

public class OKHttpActivity extends Activity {

    private ImageView imageView;
    private EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        imageView = findViewById(R.id.img);
        editText = findViewById(R.id.url_ed);

        findViewById(R.id.get_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = editText.getText().toString();
                if(TextUtils.isEmpty(url)){
                    Toast.makeText(OKHttpActivity.this,"图片地址不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = OKHttpUtils.downloadFile(OKHttpActivity.this,url);
                        if(bitmap!=null){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageBitmap(bitmap);
                                }
                            });
                        }else{
                            Log.i("lx", "文件获取失败");
                        }
                    }
                }).start();
            }
        });

        // post请求
        findViewById(R.id.post_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("name","张三");
                            object.put("age","35");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        OKHttpUtils.doOKHttp("http://10.10.19.110:8086/springmvcmybatis/main/testAndroid",OKHttpUtils.POST,object.toString(), new OKHttpUtils.OKHttpCallBack() {
                            @Override
                            public void success(String response) {
                                try {
                                    JSONObject object1 = new JSONObject(response);
                                    final String name = object1.getString("name");
                                    final String age = object1.getString("age");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(OKHttpActivity.this, "name=" + name + ",age=" + age,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void fail() {

                            }
                        });
                    }
                }).start();
            }
        });


        //上传文件
        findViewById(R.id.upload_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap params = new HashMap();
                        params.put("name","李四");
                        params.put("age","34");

                        HashMap files = new HashMap();
                        files.put("file",new File(CommUtils.getSDCardUrl(),"1.jpg"));

                        OKHttpUtils.doOKHttpFile("http://10.10.19.110:8086/springmvcmybatis/main/upload",params, files,new OKHttpUtils.OKHttpCallBack() {
                            @Override
                            public void success(String response) {
                                try {
                                    JSONObject object1 = new JSONObject(response);
                                    final String name = object1.getString("name");
                                    final String age = object1.getString("age");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(OKHttpActivity.this, "name=" + name + ",age=" + age,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void fail() {

                            }
                        });
                    }
                }).start();
            }
        });
    }
}
