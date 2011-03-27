package com.prasans.activity;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.prasans.R;
import com.prasans.adapter.TestInfoDB;
import com.prasans.adapter.TestInfoListAdapter;
import com.prasans.domain.TestInfo;

import java.util.ArrayList;
import java.util.List;

public class ReportsScreen extends ListActivity {

    private TestInfoDB testInfoDB;
    private TestInfoListAdapter testInfoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_list);
        testInfoDB = new TestInfoDB(this);
        List<TestInfo> infoList = getTestInfo();
        testInfoAdapter = new TestInfoListAdapter(this, R.layout.row_list, infoList);
        this.setListAdapter(testInfoAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        TestInfo testInfo = (TestInfo)this.getListAdapter().getItem(position);
        Toast.makeText(this, "TestCode: "+testInfo.getTestCode(), Toast.LENGTH_LONG).show();
    }

    private List<TestInfo> getTestInfo() {
        List<TestInfo> testInfoList = new ArrayList<TestInfo>();
        Cursor cursor = testInfoDB.fetchAllTests();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            TestInfo testInfo = new TestInfo();
            testInfo.setTestCode(cursor.getString(cursor.getColumnIndex(TestInfoDB.TEST_CODE)));
            testInfo.setTestName(cursor.getString(cursor.getColumnIndex(TestInfoDB.TEST_NAME)));
            testInfo.setStatus(cursor.getInt(cursor.getColumnIndex(TestInfoDB.OPEN)));
            testInfoList.add(testInfo);
        }
        return testInfoList;
    }
}

