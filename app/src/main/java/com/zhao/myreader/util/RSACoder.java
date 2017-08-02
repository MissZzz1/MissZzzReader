package com.zhao.myreader.util;

import android.util.Base64;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * <br> *RSA安全编码组件
 * 
 */
public abstract class RSACoder extends Coder {
	public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
     
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivatekey";
     
    /**
     * 用私钥对信息生成数字签名
     * @param data 加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        //解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);
         
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
         
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
         
        //取私钥对象
        PrivateKey pKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
         
        //用私钥生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(pKey);
        signature.update(data);
         
        return encryptBASE64(signature.sign());
    }
     
    /**
     * 校验数字签名
     * @param data 加密数据
     * @param publicKey 公钥
     * @param sign 数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
         
        //解密有base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);
         
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
         
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
         
        //取公钥对象
        PublicKey pKey = keyFactory.generatePublic(keySpec);
         
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pKey);
        signature.update(data);
        //验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }
     
    /**
     * 解密
     *  用私钥解密
     * @param data 加密数据
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptPrivateKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = decryptBASE64(key);
         
        //取得私钥
        PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key pKey = factory.generatePrivate(encodedKeySpec);
         
        //对数据解密
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pKey);
         
        return cipher.doFinal(data);
    }
     
    /**
     * 用公钥解密
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey( byte[] data, String key) throws Exception {
         
        //解密
        byte[] keyBytes = decryptBASE64(key);
         
        //取得公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key pKey = keyFactory.generatePublic(keySpec);
         
        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pKey);
         
        return cipher.doFinal(data);
    }
     
    /**
     * 用公钥加密
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey( byte[] data, String key) throws Exception {
         
        byte[] keyBytes = decryptBASE64(key);
         
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key pKey = keyFactory.generatePublic(keySpec);
         
         
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pKey);
         
        return cipher.doFinal(data);
    }
     
    /**
     * 用私钥加密
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
         
        byte[] keyBytes = decryptBASE64(key);
         
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(keySpec);
         
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
         
        return cipher.doFinal(data);
    }
     
    /**
     * 取得私钥
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
 
        Key key = (Key) keyMap.get(PRIVATE_KEY);
         
        return encryptBASE64(key.getEncoded());
    }
     
    /**
     * 取得公钥
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
 
        Key key = (Key) keyMap.get(PUBLIC_KEY);
         
        return encryptBASE64(key.getEncoded());
    }
    /**
     * 初始化密钥
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
         
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(1024);
         
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
         
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
         
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PRIVATE_KEY, privateKey);
        keyMap.put(PUBLIC_KEY, publicKey);
        return keyMap;
    }
    
    public static void test() throws Exception
    {
       Map<String , Object> keyMap = RSAUtilV2.genKeyPair();
        
        String publicKey = RSAUtilV2.getPublicKey(keyMap).replace("\n","");
        String privateKey = RSAUtilV2.getPrivateKey(keyMap).replace("\n","");
         
        System.out.println("公钥：\n" + publicKey);
        System.out.println("私钥：\n" + privateKey);
        System.out.println("公钥加密——私钥解密");
        String inputStr = "{\\\"mobilePhone\\\":\\\"18577113435\\\",\\\"officePhone\\\":\\\"\\\",\\\"email\\\":\\\"test1@dwb.gxi.gov.cn\\\",\\\"token\\\":\\\"d87a6879-f5d3-4327-a5a5-43ed3ec31c8d\\\",\\\"realNameSzm\\\":\\\"c\\\",\\\"realNamePy\\\":\\\"ceshi1\\\",\\\"userState\\\":\\\"1\\\",\\\"sex\\\":\\\"male\\\",\\\"duty\\\":\\\"\\\",\\\"emailPwd\\\":\\\"test1@123456\\\",\\\"jurisdiction\\\":{\\\"meeting\\\":true,\\\"file\\\":true,\\\"supervise\\\":true,\\\"notice\\\":true,\\\"calendar\\\":true,\\\"document\\\":false,\\\"im\\\":true,\\\"email\\\":true,\\\"contacts\\\":true,\\\"commission\\\":false,\\\"meetingCount\\\":true,\\\"interview\\\":true},\\\"userName\\\":\\\"test01\\\",\\\"realName\\\":\\\"测试1\\\",\\\"userKey\\\":\\\"会议报名\\\",\\\"password\\\":\\\"e10adc3949ba59abbe56e057f20f883e\\\",\\\"activitiSync\\\":1,\\\"status\\\":1,\\\"currentDepartId\\\":\\\"8a8afb90582942f501582944770d0028\\\",\\\"currentDepartName\\\":\\\"文电处\\\",\\\"id\\\":\\\"40287d81585174ba0158523e3db20010\\\"}";
        byte[] data = inputStr.getBytes(); 
 
        byte[] encodedData = RSAUtilV2.encryptByPublicKey(data, publicKey);

        String enconde = Base64.encodeToString(encodedData, Base64.DEFAULT);
        
       
        
        byte[] decodedData = RSAUtilV2.decryptByPrivateKey(Base64.decode(enconde, Base64.DEFAULT),
                privateKey); 
 
        String outputStr = new String(decodedData);
        System.out.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
    }
    
    public static void test2() throws Exception
    {
       Map<String , Object> keyMap = RSACoder.initKey();
        
        String publicKey = RSACoder.getPublicKey(keyMap);
        String privateKey = RSACoder.getPrivateKey(keyMap);
         
        System.out.println("公钥：\n" + publicKey);
        System.out.println("私钥：\n" + privateKey);
        
    	System.err.println("私钥加密——公钥解密");
        String inputStr = "sign";
        byte[] data = inputStr.getBytes(); 
 
        byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey); 
 
        byte[] decodedData = RSACoder 
                .decryptByPublicKey(encodedData, publicKey); 
 
        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
      
 
        System.err.println("私钥签名——公钥验证签名");
        // 产生签名 
        String sign = RSACoder.sign(encodedData, privateKey);
        System.err.println("签名:\r" + sign);
 
        // 验证签名 
        boolean status = RSACoder.verify(encodedData, publicKey, sign); 
        System.err.println("状态:\r" + status);
        
    }
    
  /*  public static void main(String[] args) throws Exception {
    	
    	
    	//test2();
    	test();
    }*/
}