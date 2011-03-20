package com.prasans.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseDB {
    private final String DATABASE_CREATE;
    private final String DATABASE_NAME = "multi_choice";
    private final String DATABASE_TABLE;
    private final int DATABASE_VERSION = 3;

    public final Context mCtx;
    private DatabaseHelper mDbHelper;
    public SQLiteDatabase mDb;

    public BaseDB(Context mCtx, String DATABASE_TABLE, String DATABASE_CREATE) {
        this.mCtx = mCtx;
        this.DATABASE_TABLE = DATABASE_TABLE;
        this.DATABASE_CREATE = DATABASE_CREATE;
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
            Log.w(DATABASE_NAME, "Upgrading database from version " + oldVersion + " to "
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
