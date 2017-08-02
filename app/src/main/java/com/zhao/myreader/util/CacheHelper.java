package com.zhao.myreader.util;



import com.zhao.myreader.application.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;

/**
 * Created by zhao on 2016/10/20.
 */

public class CacheHelper {

    private static Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();
    private static final int CACHE_TIME = 60*60000;
    public static String WRITING_OR_READING_FILE_NAME = "";

    /**
     * 读取对象（Serializable）
     * @param file
     * @return
     * @throws IOException
     */
    public static Serializable readObject(String file){
        if(!isExistDataCache(file))  return null;
        while(WRITING_OR_READING_FILE_NAME.equals(file)){
            try {
                Thread.sleep(100);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        WRITING_OR_READING_FILE_NAME = file;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{
            fis = MyApplication.getApplication().openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable)ois.readObject();
        }catch(FileNotFoundException e){
            e.printStackTrace();
            //   return null;
        }catch(Exception e){
            e.printStackTrace();
            //反序列化失败 - 删除缓存文件
            if(e instanceof InvalidClassException){
                File data = MyApplication.getApplication().getFileStreamPath(file);
                data.delete();
            }
            //   return null;
        }finally{
            try {
                ois.close();
                fis.close();
                WRITING_OR_READING_FILE_NAME = "";
            } catch (Exception e) {
                e.printStackTrace();
                WRITING_OR_READING_FILE_NAME = "";
            }
        }
        return null;
    }


    public static boolean deleteFile(String file){
        while(WRITING_OR_READING_FILE_NAME.equals(file)){
            try {
                Thread.sleep(100);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        WRITING_OR_READING_FILE_NAME = file;
        boolean flag = MyApplication.getApplication().deleteFile(file);
        WRITING_OR_READING_FILE_NAME = "";
        return flag;
    }

    /**
     * 保存对象
     * @param ser
     * @param file
     * @throws IOException
     */
    public static boolean saveObject(Serializable ser, String file) {
        while(WRITING_OR_READING_FILE_NAME.equals(file)){
            try {
                Thread.sleep(100);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        WRITING_OR_READING_FILE_NAME = file;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try{
            fos = MyApplication.getApplication().openFileOutput(file, MyApplication.getApplication().MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            fos.flush();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            try {
                oos.close();
                fos.close();
                WRITING_OR_READING_FILE_NAME = "";
            } catch (Exception e) {
                e.printStackTrace();
                WRITING_OR_READING_FILE_NAME = "";

            }

        }
    }

    /**
     * 判断缓存是否存在
     * @param cachefile
     * @return
     */
    private static boolean isExistDataCache(String cachefile){
        boolean exist = false;
        File data = MyApplication.getApplication().getFileStreamPath(cachefile);
        if(data.exists())
            exist = true;
        return exist;
    }
}
