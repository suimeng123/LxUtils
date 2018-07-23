package com.wy.livetelecast.mylibrary;

import android.content.Context;
import android.net.Uri;
import android.os.Debug;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * Created by lixiao2 on 2018/5/19.
 */

// 比较经典的应用就是在跳转过程中处理登陆事件，这样就不需要在目标页重复做登陆检查
// 拦截器会在跳转之间执行，多个拦截器会按优先级顺序依次执行
@Interceptor(priority = 8, name = "测试用拦截器")
public class TestInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        Uri u = postcard.getUri();
//        callback.onInterrupt(new RuntimeException("我觉得有点异常")); // 觉得有问题，中断路由流程
        callback.onContinue(postcard);  // 处理完成，交还控制权
        // 以上两种至少需要调用其中一种，否则不会继续路由

    }

    @Override
    public void init(Context context) {
    // 拦截器的初始化，会在sdk初始化的时候调用该方法，仅会调用一次

    }
}
