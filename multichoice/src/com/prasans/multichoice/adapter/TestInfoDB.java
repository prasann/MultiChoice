package com.prasans.multichoice.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TestInfoDB extends BaseDB {

    public static final String KEY_ROWID = "_id";
    public static final String TEST_NAME = "test_name";
    public static final String TEST_CODE = "test_code";
    public static final String QUES_COUNT = "test_count";
    public static final String ANSWERS = "answers";
    public static final String WRONG_ANSWERS_SCORE = "wrong_score";
    public static final String CORRECT_ANSWERS_SCORE = "correct_score";
    public static final String OPEN = "open";
    public static Context mCtx;

    private static final String DATABASE_TABLE = "contest_info";

    public TestInfoDB(Context ctx) {
        super(DATABASE_TABLE);
        mCtx = ctx;
    }

    public long createTestEntry(String name, String code, int count, String answers,int wrongAnswers,int correctAnswers) {
        this.open(mCtx);
        ContentValues initialValues = new ContentValues();
        initialValues.put(TEST_NAME, name);
        initialValues.put(TEST_CODE, code);
        initialValues.put(QUES_COUNT, count);
        initialValues.put(ANSWERS, answers);
        initialValues.put(WRONG_ANSWERS_SCORE, wrongAnswers);
        initialValues.put(CORRECT_ANSWERS_SCORE, correctAnswers);
        initialValues.put(OPEN, true);
        long returnCode = mDb.insert(DATABASE_TABLE, null, initialValues);

        this.close();
        return returnCode;
    }

    public int closeTest(String testCode) {
        this.open(mCtx);
        String where = TEST_CODE + "=" + testCode;
        ContentValues initialValues = new ContentValues();
        initialValues.put(OPEN, false);
        int returnCode = mDb.update(DATABASE_TABLE, initialValues, where, null);
        this.close();
        return returnCode;
    }

    public Cursor fetchAllTests() {
        this.open(mCtx);
        return mDb.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, TEST_NAME, TEST_CODE, QUES_COUNT, ANSWERS, OPEN,WRONG_ANSWERS_SCORE,CORRECT_ANSWERS_SCORE},
                null, null, null, null, null);
    }

    public Cursor fetchInfoFor(String testCode) {
        this.open(mCtx);
        return mDb.query(DATABASE_TABLE,
                new String[]{KEY_ROWID, TEST_NAME, TEST_CODE, QUES_COUNT, ANSWERS, OPEN,WRONG_ANSWERS_SCORE,CORRECT_ANSWERS_SCORE},
                "test_code = '" + testCode + "' COLLATE NOCASE", null, null, null, null);

    }

    public void deleteTestEntry(String testCode) {
        this.open(mCtx);
        ResultsDB resultsDB = new ResultsDB(mCtx);
        resultsDB.deleteAllEntriesFor(testCode);
        mDb.delete(DATABASE_TABLE, TEST_CODE + "= '" + testCode + "'", null);
        this.close();
    }
}