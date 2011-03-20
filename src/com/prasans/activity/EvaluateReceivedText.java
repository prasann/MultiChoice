package com.prasans.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import com.prasans.R;
import com.prasans.adapter.ResultsDB;
import com.prasans.adapter.TestInfoDB;

import static com.prasans.adapter.TestInfoDB.ANSWERS;
import static com.prasans.adapter.TestInfoDB.TEST_CODE;

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
        message = bundle.getString("message");
        phoneNumber = bundle.getString("phoneNumber");
        String testCode = extractTestCode(message);
        String answers = extractAnswer(message);
        processAnswer(testCode, answers);
    }

    private int processAnswer(String testCode, String answers) {
        String answersFromDb = fetchAnswerFromDb(testCode);
        if (answersFromDb != null) {
            int score = calculateScore(answers, answersFromDb);
            persistScoreInDb(testCode, score, answersFromDb.length());
            return 1;
        }
        return -1;
    }

    private long persistScoreInDb(String testCode, int score, int totalCount) {
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
        Cursor cursor = testInfoDB.fetchAllTests();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (testCode.equals(cursor.getString(cursor.getColumnIndex(TEST_CODE))) &&
                    cursor.getInt(cursor.getColumnIndex(TestInfoDB.OPEN)) == 1) {
                return cursor.getString(cursor.getColumnIndex(ANSWERS));
            }
        }
        return null;
    }

    private String extractTestCode(String message) {
        return message.split(" ")[0];
    }

    private String extractAnswer(String message) {
        return message.split(" ")[1];
    }
}
