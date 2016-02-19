package com.yk3372.sdk.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yk3372.sdk.base.BaseApplication;

/**
 * Created by yukai on 16-2-22.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "demo";
    private static final int VERSION = 1;

    public static SQLiteDatabase dbInstance = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void excuteSql(SQLiteDatabase sqlitedatabase, String sql) {
        try {
            sqlitedatabase.execSQL(sql);
        } catch (Exception e) {
        }
    }

    public static synchronized SQLiteDatabase getDbInstance() {
        if (dbInstance == null || !dbInstance.isOpen()) {
            DBHelper dbOpenHelper = new DBHelper(BaseApplication.getInstance());
            dbInstance = dbOpenHelper.getWritableDatabase();
        }
        return dbInstance;
    }

    public static synchronized boolean closeDB() {
        if (dbInstance == null) {
            return true;
        }

        if (dbInstance.isOpen() && !dbInstance.isDbLockedByOtherThreads()) {
            dbInstance.close();
        }
        return !dbInstance.isOpen();
    }
}
