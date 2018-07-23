package com.lx.utils.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lixiao2 on 2018/4/27.
 */

public class OKHttpUtils {
    public static final String POST = "post";
    public static final String GET = "get";
    //使用JSON数据格式请求
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //文件请求格式
    public static final MediaType FILE = MediaType.parse("application/octet-stream");
    //设置连接超时时间 秒为单位 在OkHttpClient中设置单位
    public static final int CONNECT_TIMEOUT = 15;
    //设置读取超时时间 秒为单位 在OkHttpClient中设置单位
    public static final int READ_TIMEOUT = 15;
    //设置写入超时时间 秒为单位 在OkHttpClient中设置单位
    public static final int WRITE_TIMEOUT = 15;

    //请求回调
    public interface OKHttpCallBack{
        void success(String response);
        void fail();
    }

    /**
     * get请求方式
     * @param url 请求地址
     * @param method 请求方式 POST or GET
     * @param jsonParam 请求参数是JSON格式的字符串，POST方式的时候需要
     * @param callBack 请求回调
     */
    public static void doOKHttp(String url, String method, String jsonParam, final OKHttpCallBack callBack){
        //可设置请求相关参数 也可不设如：OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS).readTimeout(READ_TIMEOUT,TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS).build();
        //请求体
        Request request = null;
        if(POST.equals(method)){//POST方式
            // 设置为JSON格式的参数请求方式并且要与服务端一致。 默认请求参数方式如：RequestBody body = new FormBody.Builder().add("name","zhangsan").build();
            RequestBody body = RequestBody.create(JSON,jsonParam);
            //初始化请求体
            request = new Request.Builder().post(body).url(url).build();
        }else {//get方式
            //初始化请求体
            request = new Request.Builder().get().url(url).build();
        }
        //请求返回监听 另一只方式直接返回结果。如：Response response = client.newCall(request).execute();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {//失败
                callBack.fail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {//成功
                if(response.isSuccessful()) {
                    callBack.success(response.body().string());
                }else{
                    callBack.fail();
                }
            }
        });
    }

    /**
     * 带图片和参数的请求 必须为POST请求
     * @param url 请求地址
     * @param params 请求参数
     * @param files 请求参数 文件列表
     * @param callBack 请求回调
     */
    public static void doOKHttpFile(String url, HashMap<String,String> params, HashMap<String,File> files, final OKHttpCallBack callBack){
        OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(READ_TIMEOUT,TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS).connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS).build();
        //文件和混合参数请求
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //将字符串参数放入请求参数体body里面
        for(String key : params.keySet()){
            //addFormDataPart用来传入字符串参数
            body.addFormDataPart(key,params.get(key).toString());
        }
        //将文件参数放入请求参数体body里面
        for(String name : files.keySet()){
            RequestBody fileBody = RequestBody.create(FILE,files.get(name));
            //addPart用来传入文件参数
            body.addPart(Headers.of("Content-Disposition", "form-data; name=\""+name+"\";filename=\""+files.get(name).getName()+"\""),fileBody);
        }
        //将数据放入请求头里面
        Request request = new Request.Builder().post(body.build()).url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.fail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    callBack.success(response.body().string());
                }else{
                    callBack.fail();
                }
            }
        });
    }

    /**
     * 下载图片 默认GET方式
     * @param url 图片地址
     */
    public static Bitmap downloadFile(Context context, String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        try {
            Response response = client.newCall(request).execute();
            InputStream input = response.body().byteStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
