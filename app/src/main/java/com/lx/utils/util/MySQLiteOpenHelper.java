package com.lx.utils.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lixiao2 on 2018/5/8.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSON = 9;
    private Context context;
    /**
     * 创建数据库
     * @param context
     * @param name 数据库名称
     * @param factory 一般为null
     * @param version 数据库版本
     */
    private MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public MySQLiteOpenHelper(Context context, String name) {
        this(context, name, null, DATABASE_VERSON);
        this.context = context;
    }

    /**
     * 数据库创建的时候会回调此方法，可以用db对象执行SQL语
     * 句，创建所需要的表
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Lx", "MySQLiteOpenHelper onCreate");
        db.execSQL("CREATE TABLE IF NOT EXISTS TB_USER (name , age)");
//        db.execSQL("CREATE index IF NOT EXISTS idx on TB_USER(name)");
    }

    /**
     * 当数据库升级时调用此方法
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Lx", "onUpgrade oldVersion="+oldVersion+",newVersion="+newVersion);
//        db.execSQL("CREATE TABLE IF NOT EXISTS TB_USER_2(name , age)");

        // 将my.db数据库中 TB_USER_2 表中数据导出到 my.db2数据库中的TB_USER表中
//        String DB_PATH = context.getDatabasePath("my.db").toString();
//        db.execSQL("ATTACH DATABASE '" + DB_PATH + "' AS 'tempDb'");
//        db.execSQL("insert into TB_USER select name,age from tempDb.TB_USER_2");
//        db.execSQL("DETACH DATABASE 'tempDb'");

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
