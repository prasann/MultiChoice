package com.prasans.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseDB {
    private final String DATABASE_CREATE;
    private final String DATABASE_NAME;
    private final String DATABASE_TABLE;
    private final int DATABASE_VERSION;
    public final String TAG;

    public final Context mCtx;
    private DatabaseHelper mDbHelper;
    public SQLiteDatabase mDb;

    public BaseDB(String DATABASE_CREATE, String DATABASE_NAME, String DATABASE_TABLE, int DATABASE_VERSION, String TAG, Context mCtx) {
        this.DATABASE_CREATE = DATABASE_CREATE;
        this.DATABASE_NAME = DATABASE_NAME;
        this.DATABASE_TABLE = DATABASE_TABLE;
        this.DATABASE_VERSION = DATABASE_VERSION;
        this.TAG = TAG;
        this.mCtx = mCtx;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        }
    }

    public BaseDB open() throws SQLiteException {
        mDbHelper = new DatabaseHelper(mCtx);
        try {
            mDb = mDbHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            mDb = mDbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


}
