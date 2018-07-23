package com.lx.utils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lx.utils.R;
import com.lx.utils.util.DesUtils;

/**
 * Created by lixiao2 on 2018/4/26.
 */

public class DESActivity extends Activity {

    private EditText mMingEd;
    private TextView mMingText,mMiText;
    // 秘钥
    private static final String SEED_KEY = "nihao";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_des);
        // 输入的明文
        mMingEd = findViewById(R.id.encry_ed);
        // 解密后的明文
        mMingText = findViewById(R.id.ming_text);
        // 加密后的密文
        mMiText = findViewById(R.id.mi_text);
    }

    public void OnClick(View v){
        String mingEd = mMingEd.getText().toString();
        String mingText = mMingText.getText().toString();
        String miText = mMiText.getText().toString();
        switch (v.getId()){
            case R.id.des_en_btn://DES加密
                if(TextUtils.isEmpty(mingEd)){
                    Toast.makeText(DESActivity.this,"请输入明文",Toast.LENGTH_SHORT).show();
                    return;
                }
                mMiText.setText(DesUtils.encryptDES(mingEd,SEED_KEY));
                break;
            case R.id.aes_en_btn://AES加密
                if(TextUtils.isEmpty(mingEd)){
                    Toast.makeText(DESActivity.this,"请输入明文",Toast.LENGTH_SHORT).show();
                    return;
                }
                mMiText.setText(DesUtils.encryptAES(mingEd,SEED_KEY));
                break;
            case R.id.md5_en_btn://MD5加密
                if(TextUtils.isEmpty(mingEd)){
                    Toast.makeText(DESActivity.this,"请输入明文",Toast.LENGTH_SHORT).show();
                    return;
                }
                mMiText.setText(DesUtils.encryMD5(mingEd));
                break;
            case R.id.des_de_btn://DES解密
                if(TextUtils.isEmpty(miText)){
                    Toast.makeText(DESActivity.this,"没有密文解密",Toast.LENGTH_SHORT).show();
                    return;
                }
                mMingText.setText(DesUtils.decryptDES(miText,SEED_KEY));
                break;
            case R.id.aes_de_btn://AES解密
                if(TextUtils.isEmpty(miText)){
                    Toast.makeText(DESActivity.this,"没有密文解密",Toast.LENGTH_SHORT).show();
                    return;
                }
                mMingText.setText(DesUtils.decryAES(miText,SEED_KEY));
                break;
            case R.id.rsa_u_en_btn://RSA公钥加密
                if(TextUtils.isEmpty(mingEd)){
                    Toast.makeText(DESActivity.this,"请输入明文",Toast.LENGTH_SHORT).show();
                    return;
                }
                mMiText.setText(DesUtils.encryByPublicKey(mingEd));
                break;
            case R.id.rsa_i_de_btn://RSA私钥解密
                if(TextUtils.isEmpty(miText)){
                    Toast.makeText(DESActivity.this,"没有密文解密",Toast.LENGTH_SHORT).show();
                    return;
                }
                mMingText.setText(DesUtils.decryByPrivateKey(miText));
                break;
            case R.id.rsa_i_en_btn://RSA私钥加密
                if(TextUtils.isEmpty(mingEd)){
                    Toast.makeText(DESActivity.this,"请输入明文",Toast.LENGTH_SHORT).show();
                    return;
                }
                mMiText.setText(DesUtils.encryByPrivateKey(mingEd));
                break;
            case R.id.rsa_u_de_btn://RSA公钥解密
                if(TextUtils.isEmpty(miText)){
                    Toast.makeText(DESActivity.this,"没有密文解密",Toast.LENGTH_SHORT).show();
                    return;
                }
                mMingText.setText(DesUtils.decryByPublicKey(miText));
                break;
        }
    }
}
