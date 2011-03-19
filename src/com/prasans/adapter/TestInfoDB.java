package com.prasans.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

public class TestInfoDB extends BaseDB{

    public static final String KEY_ROWID = "_id";
    public static final String TEST_NAME = "name";
    public static final String TEST_CODE = "code";
    public static final String QUES_COUNT = "count";
    public static final String ANSWERS = "answers";

    private static final String DATABASE_CREATE = "create table contest_info (_id integer primary key autoincrement,"
            + "name text not null, code text unique not null, count integer not null, answers text not null);";

    private static final String DATABASE_NAME = "multi_choice";
    private static final String DATABASE_TABLE = "contest_info";
    private static final int DATABASE_VERSION = 3;

    public static final String TAG = "TestDbAdapter";

    public TestInfoDB(Context ctx) {
        super(DATABASE_CREATE, DATABASE_NAME, DATABASE_TABLE, DATABASE_VERSION, TAG, ctx);
    }

    public long createTestEntry(String name, String code, int count, String answers) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(TEST_NAME, name);
        initialValues.put(TEST_CODE, code);
        initialValues.put(QUES_COUNT, count);
        initialValues.put(ANSWERS, answers);
        long returnCode = mDb.insert(DATABASE_TABLE, null, initialValues);

        this.close();
        return returnCode;
    }

    public int updateEntry(long _rowIndex, String name) {
        String where = KEY_ROWID + "=" + _rowIndex;
        ContentValues initialValues = new ContentValues();
        initialValues.put(TEST_NAME, name);
        return mDb.update(DATABASE_TABLE, initialValues, where, null);
    }

    public boolean deleteTestEntry(long rowId) {
        Toast.makeText(this.mCtx, "RowID:" + rowId, Toast.LENGTH_LONG).show();
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllTests() {
        this.open();
        Cursor cursor = mDb.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, TEST_NAME, TEST_CODE, QUES_COUNT, ANSWERS},
                null, null, null, null, null);
        this.close();
        return cursor;
    }

}