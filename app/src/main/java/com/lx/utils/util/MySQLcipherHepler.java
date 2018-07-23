package com.lx.utils.util;


import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * Created by lixiao2 on 2018/5/9.
 */

public class MySQLcipherHepler extends SQLiteOpenHelper {
    // 创建数据库时需执行语句
    private String dataSource;
    // 更新数据库版本时需执行语句
    private String updateSource;
    public MySQLcipherHepler(Context context, String name,int version) {
        super(context, name, null, version);
        //不可忽略的 进行so库加载
        SQLiteDatabase.loadLibs(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS TB_USER (name , age ,sex)");
        }catch (Exception e){

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void doSQL(SQLiteDatabase db,String sql){
        String[] sqls = sql.split(";");
        for(int i = 0;i<sqls.length;i++){
            if(sqls[i].trim().length() == 0){
                return;
            }else{
                db.execSQL(sqls[i].trim());
            }
        }
    }
}
