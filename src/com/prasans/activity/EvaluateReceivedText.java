package com.prasans.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import com.prasans.R;
import com.prasans.adapter.TestInfoDB;

public class EvaluateReceivedText extends Activity {
    private TestInfoDB testInfoDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        testInfoDB = new TestInfoDB(this);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        String phoneNumber = bundle.getString("phoneNumber");
        String testCode = extractTestCode(message);
        String answers = extractAnswer(message);
        int score = processAnswer(testCode, answers);
        if (score != -1) {
            String alertMessage = "From: " + phoneNumber +
                    "Score : " + score;

            new AlertDialog.Builder(this).setTitle("Score")
                        .setMessage(alertMessage)
                        .setNeutralButton("Close", null).show();
        }
    }

    private String extractAnswer(String message) {
        return message.split(" ")[1];
    }

    private int processAnswer(String testCode, String answers) {
        String answersFromDb = fetchAnswerFromDb(testCode);
        if (answersFromDb != null) {
            return calculateScore(answers, answersFromDb);
        }
        return -1;
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
        Cursor cursor = testInfoDB.fetchAllTests();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (testCode.equals(cursor.getString(2))) {
                return cursor.getString(4);
            }
        }
        return null;
    }

    private String extractTestCode(String message) {
        return message.split(" ")[0];
    }
}
