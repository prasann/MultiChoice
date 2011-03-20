package com.prasans.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ResultsDB extends BaseDB {
    public static final String KEY_ROWID = "_id";
    public static final String TEST_CODE = "test_code";
    public static final String SENDER = "sender";
    public static final String ANSWERS = "answers";
    public static final String SCORE = "score";
    public static final String TOTAL_COUNT = "total_count";


    private static final String DATABASE_CREATE = "create table results_info (_id integer primary key autoincrement,"
            + "test_code text not null, sender text not null, answers text not null, score integer not null, total_count integer not null);";

    private static final String DATABASE_TABLE = "results_info";

    public ResultsDB(Context mCtx) {
        super(mCtx, DATABASE_TABLE, DATABASE_CREATE);
    }

    public long createTestEntry(String testCode, String sender, String answers, int score, int totalCount) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(TEST_CODE, testCode);
        initialValues.put(SENDER, sender);
        initialValues.put(ANSWERS, answers);
        initialValues.put(SCORE, score);
        initialValues.put(TOTAL_COUNT, totalCount);
        long returnCode = mDb.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return returnCode;
    }

    public Cursor fetchAllResultsFor(String testCode) {
        this.open();
        Cursor cursor = mDb.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, TEST_CODE, SENDER, ANSWERS, SCORE,TOTAL_COUNT},
        "where test_code = " + testCode, null, null, null, null);
        this.close();
        return cursor;
    }
}
