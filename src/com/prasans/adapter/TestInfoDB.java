package com.prasans.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class TestInfoDB {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CODE = "code";
    public static final String KEY_COUNT = "count";
    public static final String KEY_ANSWERS = "answers";

    private static final String DATABASE_CREATE = "create table contest_info (_id integer primary key autoincrement,"
            + "name text not null, code text unique not null, count integer not null, answers text not null);";

    private static final String DATABASE_NAME = "multi_choice";
    private static final String DATABASE_TABLE = "contest_info";
    private static final int DATABASE_VERSION = 3;

    public static final String TAG = "TestDbAdapter";

    private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static class DatabaseHelper extends SQLiteOpenHelper {
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
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        }
    }

    public TestInfoDB open() throws SQLiteException {
        mDbHelper = new DatabaseHelper(mCtx);
        try {
            mDb = mDbHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            mDb = mDbHelper.getReadableDatabase();
        }
        return this;
    }

    public TestInfoDB(Context ctx) {
        this.mCtx = ctx;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createTestEntry(String name, String code, int count, String answers) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_CODE, code);
        initialValues.put(KEY_COUNT, count);
        initialValues.put(KEY_ANSWERS, answers);
        long returnCode = mDb.insert(DATABASE_TABLE, null, initialValues);

        this.close();
        return returnCode;
    }

    public int updateEntry(long _rowIndex, String name) {

        String where = KEY_ROWID + "=" + _rowIndex;

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);

        return mDb.update(DATABASE_TABLE, initialValues, where, null);
    }

    public boolean deleteTestEntry(long rowId) {
        Toast.makeText(this.mCtx, "RowID:" + rowId, Toast.LENGTH_LONG).show();
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllTests() {
        this.open();
        Cursor cursor = mDb.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, KEY_NAME, KEY_CODE, KEY_COUNT, KEY_ANSWERS},
                null, null, null, null, null);
        this.close();
        return cursor;
    }

}