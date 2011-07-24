package com.prasans.multichoice.activity;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.prasans.R;
import com.prasans.multichoice.adapter.TestInfoDB;
import com.prasans.multichoice.adapter.TestInfoListAdapter;
import com.prasans.multichoice.domain.TestInfo;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class DeleteReportEntry extends ListActivity {
    private TestInfoDB testInfoDB;
    private TestInfoListAdapter testInfoAdapter;
    private List<TestInfo> infoList;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_reports);
        testInfoDB = new TestInfoDB(this);
        infoList = getTestInfo();
        testInfoAdapter = new TestInfoListAdapter(this, R.layout.row_test_info_chkbox, infoList);
        this.setListAdapter(testInfoAdapter);
        listView = getListView();
        registerForContextMenu(listView);
        this.addContentView(submitButton(),new ViewGroup.LayoutParams(FILL_PARENT, WRAP_CONTENT));
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
        cursor.close();
        testInfoDB.close();
        return testInfoList;
    }

    private Button submitButton() {
        Button button = new Button(this);
        button.setHeight(WRAP_CONTENT);
        button.setText("Delete Selected");
        return button;
    }


}
