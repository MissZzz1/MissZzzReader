package com.zhao.myreader.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by zhao on 2017/3/7.
 */

public class PingUtil {

   public static boolean ping(String host, int port) {
        if (port == 0) port = 80;

        Socket connect = new Socket();
        try {
            connect.connect(new InetSocketAddress(host, port), 10 * 1000);
            return connect.isConnected();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                connect.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
