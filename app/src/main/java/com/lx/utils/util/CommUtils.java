package com.lx.utils.util;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixiao2 on 2018/4/17.
 */

public class CommUtils {
    /**
     * 获取sd卡路径
     * @return
     */
    public static File getSDCardUrl(){
        //插入sd卡
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File sdCardDir = Environment.getExternalStorageDirectory();
            return sdCardDir;
        }
        throw new NullPointerException("sd卡路径不存在");
    }

    /**
     * 判断文件或者文件夹是否存在 不存在就创建(在sd卡根目录下面创建)
     * @param fileStr
     */
    public static File createFile(String fileStr) {
        File file = new File(getSDCardUrl(),fileStr);
        if(fileStr.indexOf(".") == -1) {
            if (!file.exists()) {
                file.mkdirs();
            }
        }else {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }
    /**
     * 判空
     * @param str
     * @return
     */
    public static boolean isNull(String str){
        return str==null || "".equals(str);
    }

    //System.arraycopy()方法
    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
}
