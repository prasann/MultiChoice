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
    public static Context mCtx;

    private static final String DATABASE_TABLE = "results_info";

    public ResultsDB(Context context) {
        super(DATABASE_TABLE);
        mCtx = context;
    }

    public long createTestEntry(String testCode, String sender, String answers, int score, int totalCount) {
        this.open(mCtx);
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
        this.open(mCtx);
        Cursor cursor = mDb.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, TEST_CODE, SENDER, ANSWERS, SCORE,TOTAL_COUNT},
        "where test_code = " + testCode, null, null, null, null);
        this.close();
        return cursor;
    }
}
