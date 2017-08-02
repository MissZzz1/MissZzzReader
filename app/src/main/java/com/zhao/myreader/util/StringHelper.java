package com.zhao.myreader.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhao on 2016/9/9.
 */
public class StringHelper {

    /**
     * 是否是Emoji表情符
     * @param string
     * @return
     */
    public static boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }

    /**
     * 字符集编码
     * @param encoded
     * @return
     */
    public static String encode(String encoded){
        String res = encoded;
        try {
            res = URLEncoder.encode(encoded,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 字符集编码
     * @param encoded
     * @return
     */
    public static String encode(String encoded, String charsetName){
        String res = encoded;
        try {
            res = URLEncoder.encode(encoded,charsetName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 字符集解码
     * @param decoded
     * @return
     */

    public static String decode(String decoded, String charsetName){
        String res = decoded;
        try {
            res = URLDecoder.decode(decoded,charsetName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 字符集解码
     * @param decoded
     * @return
     */

    public static String decode(String decoded){
        String res = decoded;
        try {
            res = URLDecoder.decode(decoded,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //生成随机数字和字母,
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    public static String jidToUsername(String jid){
        if(jid != null){
            if(jid.contains("@")){
                return  jid.substring(0,jid.indexOf("@"));

            }else {
                return jid;
            }
        }
        return "";
    }

    public static boolean isEmpty(String str){
        return str == null || str.equals("");
    }

    /**
     * 缩减字符串
     * @param strlocation
     * @param maxLength
     * @return
     */
    public static String reduceString(String strlocation, int maxLength){
        if(strlocation != null) {
            String res = strlocation;
            if (strlocation.length() > maxLength) {
                char[] tem = res.toCharArray();
                res = String.copyValueOf(tem, 0, maxLength);
                res += "...";
            }
            return res;
        }else {
            return null;
        }
    }

    /**
     * 两字符串是否相等或者都为空
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isEquals(String str1, String str2){
        if(isEmpty(str1) && isEmpty(str2)){
            return true;
        }else return !isEmpty(str1) && !isEmpty(str2) && str1.equals(str2);
    }


}
