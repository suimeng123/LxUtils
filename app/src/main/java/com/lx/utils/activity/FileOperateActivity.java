package com.lx.utils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lx.utils.R;
import com.lx.utils.util.CommUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by lixiao2 on 2018/4/17.
 */

public class FileOperateActivity extends Activity {

    private File file1;// 读取的文件
    private File file2; // 写入的文件
    private EditText mEdContent;
    private TextView mTvContent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_operate);


        mEdContent = findViewById(R.id.content_ed);
        mTvContent = findViewById(R.id.content_tv);
        file1 = CommUtils.createFile("123.txt");
        file2 = CommUtils.createFile("456.txt");
        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContentToFile(file1,mEdContent.getText().toString());
                mEdContent.setText("");
            }
        });
        findViewById(R.id.read_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvContent.setText(readContentToFile(file1));
            }
        });
    }

    // 往文件写入数据
    private void addContentToFile(File file,String content){
        if(CommUtils.isNull(content)){
            return;
        }
        try {
            // 字符流写入
//            FileWriter writer = new FileWriter(file,true);
//            writer.write(content);
//            writer.close();

            // 字节流写入
//            FileOutputStream output = new FileOutputStream(file,true);
//            byte[] bytes = content.getBytes();
//            output.write(bytes);
//            output.flush();
//            output.close();

            // RandomAccessFile 方式
            RandomAccessFile writer = new RandomAccessFile(file,"rw");
            byte[] bytes = content.getBytes();
            writer.seek(file.length());// 游标移到文件内容尾部便于累加数据
            writer.write(bytes);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取文件数据
    private String readContentToFile(File file) {
        String content = "";
        try {
//            // 字符流读取
//            FileReader reader = new FileReader(file);
//            char[] info = new char[32];
//            while (reader.read(info) != -1){
//                String str = new String(info);
//                content += str;
//            }
//            reader.close();

            // 字节流读取
//            FileInputStream input = new FileInputStream(file);
//            byte[] allBytes = new byte[0];
//            int defaultLen = 16;
//            byte[] bytes = new byte[defaultLen];
//            int len = 0;
//            while ((len = input.read(bytes)) != -1){
//                if(len!=defaultLen){
//                    byte[] dest = new byte[len];
//                    System.arraycopy(bytes, 0, dest, 0, len);// 截取有效的字节
//                    allBytes = CommUtils.byteMerger(allBytes, dest);
//                }else {
//                    allBytes = CommUtils.byteMerger(allBytes, bytes);
//                }
//            }
//            content = new String(allBytes,"utf-8");
//            input.close();

            //RandomAccessFile 方式
            RandomAccessFile reader = new RandomAccessFile(file,"rw");
            int defaultLen = 32;
            byte[] bytes = new byte[defaultLen];
            byte[] allBytes = new byte[0];
            int len = 0;
            while (( len = reader.read(bytes)) != -1){
                if(len!=defaultLen){
                    byte[] dest = new byte[len];
                    // 截取有效的字节
                    System.arraycopy(bytes, 0, dest, 0, len);
                    allBytes = CommUtils.byteMerger(allBytes, dest);
                }else {
                    allBytes = CommUtils.byteMerger(allBytes, bytes);
                }
            }
            content = new String(allBytes,"utf-8");
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
