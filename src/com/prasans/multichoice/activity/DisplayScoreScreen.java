package com.prasans.multichoice.activity;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import com.prasans.R;
import com.prasans.multichoice.adapter.ContactInfoAdapter;
import com.prasans.multichoice.adapter.ResultsDB;
import com.prasans.multichoice.adapter.ScoreInfoListAdapter;
import com.prasans.multichoice.domain.ScoreInfo;
import com.prasans.multichoice.utils.AppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisplayScoreScreen extends ListActivity {
    private ResultsDB resultsDB;
    private ScoreInfoListAdapter scoreInfoListAdapter;
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
            String score = cursor.getString(cursor.getColumnIndex(ResultsDB.SCORE));
            int count = cursor.getInt(cursor.getColumnIndex(ResultsDB.TOTAL_COUNT));
			scoreInfo.setScore(score+"/"+String.valueOf(count));
            scoreInfoList.add(scoreInfo);
        }
        cursor.close();
        return scoreInfoList;
    }
}
