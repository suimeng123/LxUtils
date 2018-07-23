package com.lx.utils.util;

import android.util.Size;

import java.util.Comparator;

/**
 * Created by lixiao2 on 2018/7/4.
 */

public class CompareSizeByArea implements Comparator<Size> {
    @Override
    public int compare(Size lhs, Size rhs) {
        //确保乘法不会溢出范围
        return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                (long) rhs.getWidth() * rhs.getHeight());
    }
}
