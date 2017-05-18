package com.example.wallase.locall.app;

/**
 * Created by wallase on 2017/4/7.
 */

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.wallase.locall.green_dao.DaoMaster;
import com.example.wallase.locall.green_dao.DaoSession;
import com.example.wallase.locall.green_dao.UserDao;

import org.androidannotations.annotations.EApplication;

@EApplication
public class MyApp extends Application {

    private static final String DB_NAME = "Locall.db";
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(this,DB_NAME,null);
        SQLiteDatabase db = masterHelper.getWritableDatabase();
        DaoMaster master = new DaoMaster(db);
        daoSession = new DaoMaster(db).newSession();
    }

    public UserDao getUserDao(){
        return daoSession.getUserDao();
    }

}
