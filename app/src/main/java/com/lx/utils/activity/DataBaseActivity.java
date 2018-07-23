package com.lx.utils.activity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.lx.utils.R;
import com.lx.utils.util.MySQLiteOpenHelper;


/**
 * Created by lixiao2 on 2018/5/8.
 */

public class DataBaseActivity extends Activity {

    SQLiteDatabase db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);


//        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this,"my.db");
        MySQLiteOpenHelper helper2 = new MySQLiteOpenHelper(this,"my.db2");
//        MySQLcipherHepler helper2 = new MySQLcipherHepler(this,"test_cipher_db");
//        db = helper.getWritableDatabase();
//        helper2.getWritableDatabase().execSQL("insert into TB_USER values('李三','30')");

//        SQLiteDatabase db1 = helper.getWritableDatabase();
        SQLiteDatabase db2 = helper2.getWritableDatabase();

        // 将my.db数据库中 TB_USER_2 表中数据导出到 my.db2数据库中的TB_USER表中
        String DB_PATH = getDatabasePath("my.db").toString();
        db2.execSQL("ATTACH DATABASE '" + DB_PATH + "' AS 'tempDb'");
        db2.execSQL("insert into TB_USER select name,age from tempDb.TB_USER_2");
        db2.execSQL("DETACH DATABASE 'tempDb'");

        Log.i("lx", "onCreate: "+db2.getVersion());
    }

    public void OnClick(View v){
        switch (v.getId()){
            case R.id.btn1:
//                db.execSQL("insert into TB_USER values('张三','10') ");
                break;
            case R.id.btn2:
//                queryDatas();
                break;
            default:
                break;
        }
    }


    /**
     * 查询全部数据
     */
//    public void queryDatas(){
//        //查询数据库
//        Cursor cursor = null;
//        try {
//            cursor = db.rawQuery("select * from TB_USER",null);
//            while (cursor.moveToNext()) {
//                int count = cursor.getColumnCount();
//                String columeName = cursor.getColumnName(0);//获取表结构列名
//                String  name = cursor.getString(0);//获取表结构列数据
//                Log.e("lx", "count = " + count + " columName = " + columeName + "  name =  " +name);
//            }
//            //关闭游标防止内存泄漏
//            if (cursor != null) {
//                cursor.close();
//            }
//        } catch (SQLException e) {
//            Log.e("lx", "queryDatas" + e.toString());
//        }
//        //关闭数据库
//        db.close();
//    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
