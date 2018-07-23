package com.lx.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lx.utils.R;

/**
 * Created by lixiao2 on 2018/6/14.
 */

@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView implements Cloneable {

    private boolean isFloat = false;
    public MyTextView(Context context) {
        this(context,null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = null;
        try {
            //获得属性集合，并从属性集合中获得对应属性的资源
            array = getContext().obtainStyledAttributes(attrs,R.styleable.MyTextView);
            isFloat = array.getBoolean(R.styleable.MyTextView_isFloat,false);
        }finally {
            if(array != null){
                array.recycle();
            }
        }
    }

    public boolean getIsFloat(){
        return  isFloat;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        MyTextView a = null;
        a = (MyTextView) super.clone();
        return a;
    }
}
