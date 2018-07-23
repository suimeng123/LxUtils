package com.lx.utils.model;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lixiao2 on 2018/7/11.
 */

public class Student {
    private int age;
    private String name;
    public String sex;

    private void privateInfo(String str) {
        System.out.println("调用privateInfo方法:" + str);
    }

    public static void main(String[] args) throws Exception {
        try {
            Class student = Class.forName("com.lx.utils.model.Student");
            Object object = student.newInstance();
            Method privateInfo = student.getDeclaredMethod("privateInfo");
            privateInfo.setAccessible(true);
            privateInfo.invoke(object,"调用成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
