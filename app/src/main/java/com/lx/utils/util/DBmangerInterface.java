package com.lx.utils.util;

import android.database.Cursor;

/**
 * Created by lixiao2 on 2018/5/9.
 */

public interface DBmangerInterface {
    void close();
    void open();
    boolean isOpen();
    void beginTransaction();
    void endTransaction();
    void exec(String sql);
    void exec(String sql,Object[] args);
    Cursor query(String sql);
    Cursor query(String sql,String[] args);
}
