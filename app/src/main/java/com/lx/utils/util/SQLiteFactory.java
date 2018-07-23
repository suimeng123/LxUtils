package com.lx.utils.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixiao2 on 2018/5/8.
 */

public class SQLiteFactory {
    public static List<MySQLiteOpenHelper> helpers = new ArrayList<>();
    public static MySQLiteOpenHelper getSQLiteDateBase(Context context,String name){
        for(MySQLiteOpenHelper helper : helpers){
            if(name.equals(helper.getDatabaseName())){
                return helper;
            }
        }
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(context,name);
        helpers.add(helper);
        return helper;
    }
}
