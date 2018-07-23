package com.lx.utils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.lx.utils.R;
import com.lx.utils.view.TwoPageLayout;

import java.lang.reflect.AccessibleObject;

/**
 * Created by lixiao2 on 2018/3/12.
 */
public class TwoPageViewActivity extends Activity {

    TwoPageLayout tpl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_page);
        tpl = (TwoPageLayout) findViewById(R.id.tpl);
    }

    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.text1:
                tpl.scrollTo(1);
                break;
            case R.id.text2:
                tpl.scrollTo(0);
                break;
        }
    }
}
