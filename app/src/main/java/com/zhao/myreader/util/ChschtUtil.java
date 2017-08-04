package com.zhao.myreader.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhao on 2017/8/4.
 */

public class ChschtUtil {

    public static String big5ToChinese(String s) {
        try {
            if (s == null || s.equals(""))
                return ("");
            String newstring = null;
            newstring = new String(s.getBytes("big5"), "gb2312");
            return (newstring);
        } catch (UnsupportedEncodingException e) {
            return (s);
        }
    }

    public static String ChineseTobig5(String s) {
        try {
            if (s == null || s.equals(""))
                return ("");
            String newstring = null;
            newstring = new String(s.getBytes("gb2312"), "big5");
            return (newstring);
        } catch (UnsupportedEncodingException e) {
            return (s);
        }
    }
}
