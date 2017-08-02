package com.zhao.myreader.greendao;


import com.zhao.myreader.application.MyApplication;
import com.zhao.myreader.greendao.gen.DaoMaster;
import com.zhao.myreader.greendao.gen.DaoSession;
import com.zhao.myreader.greendao.util.MySQLiteOpenHelper;

/**
 * Created by zhao on 2017/3/15.
 */

public class GreenDaoManager {
    private static GreenDaoManager instance;
    private static DaoMaster daoMaster;
    private static MySQLiteOpenHelper mySQLiteOpenHelper;

    public static GreenDaoManager getInstance() {
        if (instance == null) {
            instance = new GreenDaoManager();
        }
        return instance;
    }

    public GreenDaoManager(){
        mySQLiteOpenHelper = new MySQLiteOpenHelper(MyApplication.getmContext(), "read" , null);
        daoMaster = new DaoMaster(mySQLiteOpenHelper.getWritableDatabase());
    }



    public DaoSession getSession(){
       return daoMaster.newSession();
    }

}
