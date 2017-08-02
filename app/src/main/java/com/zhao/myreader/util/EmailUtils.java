package com.zhao.myreader.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hmt on 2016/12/30.
 */

public class EmailUtils {
    /**
     * 检验邮箱格式
     * @param email
     * @return
     */
    public static boolean isMatched(String email){
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }
}
