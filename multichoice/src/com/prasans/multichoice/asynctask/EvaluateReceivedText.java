package com.prasans.multichoice.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import com.prasans.multichoice.adapter.ResultsDB;
import com.prasans.multichoice.adapter.TestInfoDB;
import com.prasans.multichoice.service.SendSMS;

import java.text.DecimalFormat;

import static com.prasans.multichoice.adapter.TestInfoDB.ANSWERS;
import static com.prasans.multichoice.adapter.TestInfoDB.CORRECT_ANSWERS_SCORE;
import static com.prasans.multichoice.adapter.TestInfoDB.WRONG_ANSWERS_SCORE;

public class EvaluateReceivedText extends AsyncTask<Void, Void, Void> {
    private TestInfoDB testInfoDB;
    private ResultsDB resultsDB;
    private String phoneNumber;
    private String message;
    private long receivedAt;
    private int wrongAnswerScore;
    private int correctAnswerScore;
    private String answersFromDb;

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
            int scoreOutOf = answers.length() * correctAnswerScore;
            String smsContent = smsContent(score, scoreOutOf) +
                    " The actual answers for this test are : " + answersFromDb;
            new SendSMS().sendSms(phoneNumber, smsContent);
        }
    }

    private String smsContent(float score, float scoreOutOf) {
        StringBuffer stringBuffer = new StringBuffer();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        Float percentage = (score / scoreOutOf) * 100;
        if (percentage > 40.00) {
            stringBuffer.append("Congrats !!");
        }
        stringBuffer.append("Your have scored ")
                .append((int)score).append("/").append((int)scoreOutOf)
                .append(" Percentage : ")
                .append(decimalFormat.format(percentage)).append("%");
        return stringBuffer.toString();
    }

    private int processAnswer(String testCode, String answers) {
        answersFromDb = fetchAnswerFromDb(testCode);
        if (answersFromDb != null) {
            int score = calculateScore(answers, answersFromDb);
            persistScoreInDb(testCode, score, answersFromDb.length());
            return score;
        }
        return -1;
    }

    private void persistScoreInDb(String testCode, int score, int totalCount) {
        resultsDB.createTestEntry(testCode, phoneNumber, message, receivedAt, score, totalCount * correctAnswerScore);
    }

    private int calculateScore(String userAnswer, String answersFromDb) {
        String shortAnswer = userAnswer.length() < answersFromDb.length() ? userAnswer : answersFromDb;
        userAnswer = userAnswer.substring(0, shortAnswer.length());
        answersFromDb = answersFromDb.substring(0, shortAnswer.length());
        int score = 0;
        for (int i = 0; i < shortAnswer.length(); i++) {
            if (userAnswer.charAt(i) == 'X' || userAnswer.charAt(i) == 'x') {
                continue;
            } else if (userAnswer.charAt(i) == answersFromDb.charAt(i)) {
                score = score + correctAnswerScore;
            } else {
                score = score + wrongAnswerScore;
            }
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
                    wrongAnswerScore = cursor.getInt(cursor.getColumnIndex(WRONG_ANSWERS_SCORE));
                    correctAnswerScore = cursor.getInt(cursor.getColumnIndex(CORRECT_ANSWERS_SCORE));
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
