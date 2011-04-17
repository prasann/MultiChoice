package com.prasans.activity;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import com.prasans.R;
import com.prasans.adapter.ContactInfoAdapter;
import com.prasans.adapter.ResultsDB;
import com.prasans.adapter.ScoreInfoListAdapter;
import com.prasans.domain.ScoreInfo;
import com.prasans.utils.AppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisplayScoreScreen extends ListActivity {
    private ResultsDB resultsDB;
    private ScoreInfoListAdapter scoreInfoListAdapter;
    private int count;
    private ContactInfoAdapter contactInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_sheet);
        resultsDB = new ResultsDB(this);
        contactInfoAdapter = new ContactInfoAdapter(this);
        Bundle bundle = getIntent().getExtras();
        String testCode = bundle.getString(AppConstants.TEST_CODE);
        List<ScoreInfo> infoList = getScoresFor(testCode);
        scoreInfoListAdapter = new ScoreInfoListAdapter(this,R.layout.row_score,infoList);
        this.setListAdapter(scoreInfoListAdapter);
        TextView testName = (TextView)findViewById(R.id.testName);
        testName.setText(bundle.getString(AppConstants.TEST_NAME));
        TextView receivedCount = (TextView)findViewById(R.id.answerReceivedCount);
        receivedCount.setText(String.valueOf(infoList.size()));
        TextView questCount = (TextView)findViewById(R.id.totalQuestcount);
        questCount.setText(String.valueOf(count));
    }

    private List<ScoreInfo> getScoresFor(String testCode) {
        List<ScoreInfo> scoreInfoList = new ArrayList<ScoreInfo>();
        Cursor cursor = resultsDB.fetchAllResultsFor(testCode);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            long timeInMillis = cursor.getLong(cursor.getColumnIndex(ResultsDB.RECEIVED_TIME));
            Date date = new Date(timeInMillis);
            String formattedDate = new SimpleDateFormat("dd-MMM-yy HH:mm:ss").format(date);

            ScoreInfo scoreInfo = new ScoreInfo();
            String number = cursor.getString(cursor.getColumnIndex(ResultsDB.SENDER));
            scoreInfo.setPhoneNumber(contactInfoAdapter.lookUp(number));
            scoreInfo.setReceivedTime(formattedDate);
            scoreInfo.setScore(cursor.getString(cursor.getColumnIndex(ResultsDB.SCORE)));
            scoreInfoList.add(scoreInfo);
            count = cursor.getInt(cursor.getColumnIndex(ResultsDB.TOTAL_COUNT));
        }
        cursor.close();
        return scoreInfoList;
    }
}
