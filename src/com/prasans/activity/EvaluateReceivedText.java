package com.prasans.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import com.prasans.R;
import com.prasans.adapter.ResultsDB;
import com.prasans.adapter.TestInfoDB;

import static com.prasans.adapter.TestInfoDB.ANSWERS;
import static com.prasans.utils.AppConstants.MESSAGE;
import static com.prasans.utils.AppConstants.PHONE_NUMBER;

public class EvaluateReceivedText extends Activity {
    private TestInfoDB testInfoDB;
    private ResultsDB resultsDB;
    private String phoneNumber;
    private String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        testInfoDB = new TestInfoDB(this);
        resultsDB = new ResultsDB(this);

        Bundle bundle = getIntent().getExtras();
        message = bundle.getString(MESSAGE);
        phoneNumber = bundle.getString(PHONE_NUMBER);
        String testCode = extractTestCode(message);
        String answers = extractAnswer(message);
        int score = processAnswer(testCode, answers);
        if (score != -1) {
            sendScoreInSms(score);
        }
    }

    private void sendScoreInSms(int score) {
        Intent intent = new Intent(EvaluateReceivedText.this, SendSMS.class);
        Bundle bundle = new Bundle();
        bundle.putInt("score", score);
        bundle.putString(PHONE_NUMBER, phoneNumber);
        intent.putExtras(bundle);
        startActivityForResult(intent, RESULT_FIRST_USER);
    }

    private int processAnswer(String testCode, String answers) {
        String answersFromDb = fetchAnswerFromDb(testCode);
        if (answersFromDb != null) {
            int score = calculateScore(answers, answersFromDb);
            persistScoreInDb(testCode, score, answersFromDb.length());
            return score;
        }
        return -1;
    }

    private long persistScoreInDb(String testCode, int score, int totalCount) {
        Log.d("Persist", phoneNumber);
        return resultsDB.createTestEntry(testCode, phoneNumber, message, score, totalCount);
    }

    private int calculateScore(String userAnswer, String answersFromDb) {
        String shortAnswer = userAnswer.length() < answersFromDb.length() ? userAnswer : answersFromDb;
        userAnswer = userAnswer.substring(0, shortAnswer.length());
        answersFromDb = answersFromDb.substring(0, shortAnswer.length());
        int score = 0;
        for (int i = 0; i < shortAnswer.length(); i++) {
            if (userAnswer.charAt(i) == answersFromDb.charAt(i))
                score++;
        }
        return score;
    }

    private String fetchAnswerFromDb(String testCode) {
        Cursor cursor = testInfoDB.fetchInfoFor(testCode);
        if (cursor.moveToFirst()) {
            for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex(TestInfoDB.OPEN)) == 1) {
                    return cursor.getString(cursor.getColumnIndex(ANSWERS));
                }
            }
        }
        cursor.close();
        testInfoDB.close();
        return null;
    }

    private String extractTestCode(String message) {
        return message.split(" ")[0];
    }

    private String extractAnswer(String message) {
        return message.split(" ")[1];
    }
}
