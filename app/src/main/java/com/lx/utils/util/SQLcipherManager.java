package com.lx.utils.util;

import android.content.Context;
import android.database.Cursor;
import android.os.DropBoxManager;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by lixiao2 on 2018/5/9.
 */

public class SQLcipherManager implements DBmangerInterface {
    private SQLiteDatabase db;
    private boolean isReady = false;
    private Context context;
    private String dbName;
    private String dbSource;
    private String updateSource;
    private int version;
    private String password;


    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbSource() {
        return dbSource;
    }

    public void setDbSource(String dbSource) {
        this.dbSource = dbSource;
    }

    public String getUpdateSource() {
        return updateSource;
    }

    public void setUpdateSource(String updateSource) {
        this.updateSource = updateSource;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void close() {
        if(db!=null && db.isOpen()){
            db.close();
        }
        db = null;
    }

    @Override
    public void open() {
        if(db!=null && db.isOpen()) {
            if (isReady) {
                db = new MySQLcipherHepler(context, dbName,1).getReadableDatabase(password);
            } else {
                db = new MySQLcipherHepler(context, dbName,1).getWritableDatabase(password);
            }
        }
    }

    @Override
    public boolean isOpen() {
        if(db !=null && db.isOpen()){
            return true;
        }
        return false;
    }

    @Override
    public void beginTransaction() {
        if(db != null){
            db.beginTransaction();
        }
    }

    @Override
    public void endTransaction() {
        if(db != null){
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    @Override
    public void exec(String sql) {
        if(db!=null && db.isOpen()){
            db.execSQL(sql);
        }
    }

    @Override
    public void exec(String sql, Object[] args) {
        if(db !=null && db.isOpen()){
            db.execSQL(sql,args);
        }
    }

    @Override
    public Cursor query(String sql) {
        Cursor cursor = null;
        if(db!=null && db.isOpen()){
            cursor = db.rawQuery(sql,null);
            if(cursor!=null && cursor.getCount()>0){
                cursor.moveToFirst();
            }else{
                if(cursor !=null){
                    cursor.close();
                    cursor = null;
                }
            }
        }
        return cursor;
    }

    @Override
    public Cursor query(String sql, String[] args) {
        Cursor cursor = null;
        if(db!=null && db.isOpen()){
            cursor = db.rawQuery(sql,args);
            if(cursor!=null && cursor.getCount()>0){
                cursor.moveToFirst();
            }else{
                if(cursor !=null){
                    cursor.close();
                    cursor = null;
                }
            }
        }
        return cursor;
    }
}
