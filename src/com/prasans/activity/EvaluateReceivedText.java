package com.prasans.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import com.prasans.adapter.ResultsDB;
import com.prasans.adapter.TestInfoDB;

import static com.prasans.adapter.TestInfoDB.ANSWERS;

public class EvaluateReceivedText extends AsyncTask<Void, Void, Void> {
    private TestInfoDB testInfoDB;
    private ResultsDB resultsDB;
    private String phoneNumber;
    private String message;
    private long receivedAt;

    public EvaluateReceivedText(String phoneNumber, String message, long receivedAt, Context context) {
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.receivedAt = receivedAt;
        testInfoDB = new TestInfoDB(context);
        resultsDB = new ResultsDB(context);
    }

    public void process() {
        String testCode = extractTestCode(message);
        String answers = extractAnswer(message);
        int score = processAnswer(testCode, answers);
        if (score != -1) {
            new SendSMS().sendSms(phoneNumber, "Congrtas !! Your have scored " + score + "/" + answers.length() + " for the Test Code " + testCode);
        }
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

    private void persistScoreInDb(String testCode, int score, int totalCount) {
        resultsDB.createTestEntry(testCode, phoneNumber, message, receivedAt, score, totalCount);
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
        String answer = null;
        Cursor cursor = testInfoDB.fetchInfoFor(testCode);
        if (cursor.moveToFirst()) {
            for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex(TestInfoDB.OPEN)) == 1) {
                    answer = cursor.getString(cursor.getColumnIndex(ANSWERS));
                    break;
                }
            }
        }
        cursor.close();
        testInfoDB.close();
        return answer;
    }

    private String extractTestCode(String message) {
        return message.split(" ")[0];
    }

    private String extractAnswer(String message) {
        return message.split(" ")[1];
    }

    @Override
    protected void onPreExecute() {
        Log.d("ERT", "About to start evaluating Message");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        this.process();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
