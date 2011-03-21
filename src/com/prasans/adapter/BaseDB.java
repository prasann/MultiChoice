package com.prasans.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import static java.util.Arrays.asList;

public class BaseDB {
    private String query1 = "create table results_info (_id integer primary key autoincrement," +
            "test_code text not null, sender text not null, answers text not null, score integer not null, total_count integer not null);";
    private String query2 = "create table contest_info (_id integer primary key autoincrement," +
            "test_name text not null, test_code text unique not null, test_count integer not null, answers text not null, open boolean);";
    private List<String> DATABASE_CREATE  =  asList(query1,query2);

    private final String DATABASE_NAME = "multi_choice";
    private final String DATABASE_TABLE;
    private final int DATABASE_VERSION = 3;


    private DatabaseHelper mDbHelper;
    public SQLiteDatabase mDb;

    public BaseDB(String DATABASE_TABLE) {
        this.DATABASE_TABLE = DATABASE_TABLE;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for (String query : DATABASE_CREATE) {
                db.execSQL(query);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(DATABASE_NAME, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        }
    }

    public BaseDB open(Context myContext) throws SQLiteException {
        mDbHelper = new DatabaseHelper(myContext);
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
