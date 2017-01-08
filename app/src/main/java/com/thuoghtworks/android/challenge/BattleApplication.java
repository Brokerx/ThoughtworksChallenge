package com.thuoghtworks.android.challenge;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.thuoghtworks.android.challenge.db.DaoMaster;
import com.thuoghtworks.android.challenge.db.DaoSession;

/**
 * Created by Govind on 08-Jan-17.
 */

public class BattleApplication extends Application {
    private static BattleApplication sInstance;

    private static final String DB_NAME = "ThoughtworksDB";
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public synchronized static BattleApplication getInstance() {
        return sInstance;
    }


    public static synchronized Context getContext() {
        return getInstance().getBaseContext();
    }

    public static synchronized Context getAppContext() {
        return getInstance().getApplicationContext();
    }

    public static DaoSession getDaoSession(){
        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(getAppContext(), DB_NAME, null); //create database db file if not exist
        SQLiteDatabase db = masterHelper.getWritableDatabase();  //get the created database db file
        DaoMaster master = new DaoMaster(db);//create masterDao
        DaoSession masterSession=master.newSession(); //Creates Session session
        return masterSession;
    }
}
