package com.lx.utils.util;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by lixiao2 on 2018/4/26.
 * 加解密工具类
 */

public class DesUtils {
    /*------------------------------------------DES加解密（对称加解密）--------------------------------------------------*/
    //初始化向量，随意填写
    private static byte[] ivp = {0,1,2,3,4,5,6,7,8,9};

    /**
     * DES数据加密
     * @param encryString 加密明文
     * @param encryKey 加密秘钥
     * 返回加密后的密文
     */
    public static String encryptDES(String encryString,String encryKey){
        try {
            // 实例化IvParameterSpec对象，使用指定的初始化向量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivp);
            //实例化SecretKeySpec，根据传入的密钥获得字节数组来构造SecretKeySpec
//            SecretKeySpec keySpec = new SecretKeySpec(encryKey.getBytes(),"DES");
            // 对秘钥进行加密处理
            SecretKeySpec keySpec = (SecretKeySpec) getRawKey(encryKey);
            //创建密码器
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            //用秘钥初始化Cipher对象 加密模式ENCRYPT_MODE
            cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivParameterSpec);
            //执行加密操作
            byte[] encryData = cipher.doFinal(encryString.getBytes());
            // 用Base64对密文进行再次加密
            return Base64.encodeToString(encryData,0);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    // 对密钥进行处理
    private static Key getRawKey(String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("DES");
        //for android
        SecureRandom sr = null;
        // 在4.2以上版本中，SecureRandom获取方式发生了改变
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        // for Java
        // secureRandom = SecureRandom.getInstance(SHA1PRNG);
        sr.setSeed(key.getBytes());
        kgen.init(64, sr); //DES固定格式为64bits，即8bytes。
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return new SecretKeySpec(raw, "DES");
    }

    /**
     * DES解密算法
     * @param decryString 解密密文
     * @param decryKey 解密key与加密key要一致
     * @return 返回解密后的明文
     */
    public static String decryptDES(String decryString,String decryKey){
        try {
            // 先用Base64解密
            byte[] byteMi = Base64.decode(decryString,0);
            //初始化IvParameterSpec对象，使用指定的初始化向量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivp);
            //实例化SecretKeySpec，根据传入的秘钥获取字节数组构造SecretKeySpec
//            SecretKeySpec keySpec = new SecretKeySpec(decryKey.getBytes(),"DES");
            SecretKeySpec keySpec = (SecretKeySpec) getRawKey(decryKey);
            //创建密码器
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            //用密钥初始化Cipher对象,这是解密模式DECRYPT_MODE
            cipher.init(Cipher.DECRYPT_MODE,keySpec,ivParameterSpec);
            // 解密
            byte[] byteMing = cipher.doFinal(byteMi);
            return new String(byteMing);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
/*------------------------------------------DES加解密（对称加解密）--------------------------------------------------*/

/*------------------------------------------AES加解密（对称加解密）--------------------------------------------------*/
    /**
     * AES加密
     * @param encryString 加密明文
     * @param encryKey 加密key
     * @return 加密后的明文
     */
    public static String encryptAES(String encryString,String encryKey){
        //
        byte[] miKey = getRawKey(encryKey.getBytes());
        //获取加密后的密文byte[]
        byte[] result = encrypt(miKey,encryString.getBytes());

        //方法一  将密文由十进制数转换为十六进制数来保存
        return toHex(result);

        //方法二  将密文由base64加密后保存
//        return Base64.encodeToString(result,0);
    }

    /**
     * AES解密
     * @param decryString 密文
     * @param decryKey 秘钥
     * @return 解密后的明文
     */
    public static String decryAES(String decryString,String decryKey){
        // 对秘钥进行加密
        byte[] rawByte = getRawKey(decryKey.getBytes());
        // 获取密文的byte[]

        //方法一 对应上面AES加密方法一 将十六进制数据转换为十进制字
        byte[] enc = toByte(decryString);

        //方法二 对应上面AES加密方法二  将用base64加密后的数据先解密出来
//        byte[] enc = Base64.decode(decryString,0);


        // 解密
        byte[] result = decrypt(rawByte,enc);
        return new String(result);
    }

    /**
     * 对秘钥进行加密
     * @param seed 秘钥
     * @return 加密后的秘钥byte[]
     */
    private static byte[] getRawKey(byte[] seed) {
        try {
            //获取密钥生成器
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr ;
            // 在4.2以上版本中，SecureRandom获取方式发生了改变
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            } else {
                sr = SecureRandom.getInstance("SHA1PRNG");
            }
            sr.setSeed(seed);
            //生成位的AES密码生成器
            kgen.init(128, sr);
            //生成密钥
            SecretKey skey = kgen.generateKey();
            //编码格式
            byte[] raw = skey.getEncoded();
            return raw;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密数据
     * @param seed 加密后的秘钥byte[]
     * @param clear 明文byte[]
     * @return 加密后的密文byte[]
     */
    private static byte[] encrypt(byte[] seed, byte[] clear) {
        try {
            //生成一系列扩展密钥，并放入一个数组中
            SecretKeySpec skeySpec = new SecretKeySpec(seed, "AES");
//            Cipher cipher = Cipher.getInstance("AES");//4.2以上有bug
            Cipher cipher = Cipher.getInstance("AES/ECB/ZeroBytePadding");
            //使用ENCRYPT_MODE模式，用skeySpec密码组，生成AES加密方法
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            //得到加密数据
            byte[] encrypted = cipher.doFinal(clear);
            return encrypted;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param raw 秘钥
     * @param encrypted 密文
     * @return
     */
    private static byte[] decrypt(byte[] raw, byte[] encrypted) {
        try {
            //生成一系列扩展密钥，并放入一个数组中
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//            Cipher cipher = Cipher.getInstance("AES");//4.2以上有bug
            Cipher cipher = Cipher.getInstance("AES/ECB/ZeroBytePadding");
            //使用DECRYPT_MODE模式，用skeySpec密码组，生成AES解密方法
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            //得到加密数据
            byte[] decrypted = cipher.doFinal(encrypted);
            return decrypted;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;

    }

    //将十进制数转为十六进制
    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }
    //将十六进制字符串转为十进制字节数组
    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }
    //将十六进制字符串转换位十进制字符串
    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }
    //将十进制字节数组转换为十六进制
    public static String toHex(byte[]buf){
        if(buf==null){
            return "";
        }
        StringBuffer result=new StringBuffer(2*buf.length);
        for(int i=0;i<buf.length;i++){
            appendHex(result,buf[i]);
        }
        return result.toString();
    }
    private final static String HEX="0123456789ABCDEF";
    private static void appendHex(StringBuffer sb,byte b){
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }
    /*------------------------------------------AES加解密（对称加解密）--------------------------------------------------*/


    /*------------------------------------------MD5加密(md5是单向加密没有解密的)--------------------------------------------------*/
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F' };
    /**
     * MD5加密
     * @param encryString
     * @return 返回加密后的密文
     */
    public static String encryMD5(String encryString){
        try {
            // 实例化MessageDigest对象
            MessageDigest digest = MessageDigest.getInstance("md5");
            //获取明文的byte[]
            byte[] result = digest.digest(encryString.getBytes());
            StringBuilder sb = new StringBuilder(result.length * 2);
            for (int i = 0; i < result.length; i++) {
                sb.append(HEX_DIGITS[(result[i] & 0xf0) >>> 4]);
                sb.append(HEX_DIGITS[result[i] & 0x0f]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "error";
    }
    /*------------------------------------------MD5加密(md5是单向加密没有解密的)--------------------------------------------------*/


    /*------------------------------------------RSA加解密（非对称加解密）(可以公钥加密私钥解密，也可私钥加密公钥解密)--------------------------------------------------*/
    //公钥
    private static PublicKey publicKey = null;
    //私钥
    private static PrivateKey privateKey = null;
    /**
     * 得到KeyPair密钥对（包含公钥和私钥）
     * @param keyLen 密钥对长度1024长度现在能再24h内被破解不安全 所以一般用2048长度
     * @return KeyPair密钥对
     */
    private static KeyPair getRSAKeyPair(int keyLen){
        KeyPair keyPair = null;
        try {
            // 初始化RSA秘钥对生成器
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            //设置秘钥长度
            generator.initialize(keyLen);
            //生成秘钥对
            keyPair = generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyPair;
    }

    /**
     * 数据加解密通用方法
     * @param encryString 明文或者密文
     * @param mode 加密(Cipher.ENCRYPT_MODE)或者解密(Cipher.DECRYPT_MODE)
     * @param key 公钥或者私钥
     * @return  加密或者解密后的 byte[]
     */
    private static byte[] getRawKey(byte[] encryString,Key key,int mode){
        // 用来接收返回的结果集
        byte[] resultByte = null;
        try {
            // 构建加解密器需要传入一个字符串对象 格式必须为"algorithm/mode/padding"或者"algorithm/",意为"算法/加密模式/填充方式"
            Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
            cipher.init(mode,key);
            resultByte = cipher.doFinal(encryString);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return resultByte;
    }

    /**
     * 使用公钥加密
     * @param encryString 明文
     * @return 加密后的密文
     */
    public static String encryByPublicKey(String encryString){
        if(publicKey == null){
            KeyPair keyPair = getRSAKeyPair(2048);
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        }
        //得到加密后的密文
        byte[] resultByte = getRawKey(encryString.getBytes(),publicKey,Cipher.ENCRYPT_MODE);
        // 使用Base64再次加密
        return Base64.encodeToString(resultByte,0);
    }

    /**
     * 使用私钥解密
     * @param decryString 密文
     * @return 解密后的明文
     */
    public static String decryByPrivateKey(String decryString){
        //先用base64进行解密
        byte[] miString = Base64.decode(decryString.getBytes(),0);
        //得到解密后的明文
        byte[] resultByte = getRawKey(miString,privateKey,Cipher.DECRYPT_MODE);
        return  new String(resultByte);
    }

    /**
     * 私钥进行加密
     * @param encryString 明文
     * @return 密文
     */
    public static String encryByPrivateKey(String encryString){
        if(privateKey == null){
            KeyPair keyPair = getRSAKeyPair(2048);
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        }
        // 得到加密后的密文
        byte[] resultByte = getRawKey(encryString.getBytes(),privateKey,Cipher.ENCRYPT_MODE);
        // 将密文进行base64加密
        return Base64.encodeToString(resultByte,0);
    }

    /**
     * 使用公钥进行解密
     * @param decryString 密文
     * @return 解密后的明文
     */
    public static String decryByPublicKey(String decryString){
        //先将密文进行base64解密
        byte[] miString = Base64.decode(decryString,0);
        // 再用公钥将密文进行解密
        byte[] resultByte = getRawKey(miString,publicKey,Cipher.DECRYPT_MODE);
        return new String(resultByte);
    }
    /*------------------------------------------RSA加解密（非对称加解密）--------------------------------------------------*/
}
