package com.prasans.multichoice.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.prasans.R;
import com.prasans.multichoice.adapter.TestInfoDB;
import com.prasans.multichoice.adapter.TestInfoListAdapter;
import com.prasans.multichoice.domain.TestInfo;

import java.util.ArrayList;
import java.util.List;

import static com.prasans.multichoice.utils.AppConstants.TEST_CODE;
import static com.prasans.multichoice.utils.AppConstants.TEST_NAME;

public class ReportsScreen extends ListActivity {

    private TestInfoDB testInfoDB;
    private TestInfoListAdapter testInfoAdapter;
    private List<TestInfo> infoList;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_list);
        testInfoDB = new TestInfoDB(this);
        infoList = getTestInfo();
        testInfoAdapter = new TestInfoListAdapter(this, R.layout.row_test_info, infoList);
        this.setListAdapter(testInfoAdapter);
        listView = getListView();
        registerForContextMenu(listView);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        TestInfo testInfo = (TestInfo) this.getListAdapter().getItem(position);
        Intent intent = new Intent(v.getContext(), DisplayScoreScreen.class);
        Bundle bundle = new Bundle();
        bundle.putString(TEST_CODE, testInfo.getTestCode());
        bundle.putString(TEST_NAME, testInfo.getTestName());
        intent.putExtras(bundle);
        startActivityForResult(intent, RESULT_FIRST_USER);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle("Actions");
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            Log.e("", "bad menuInfo", e);
            return false;
        }
        Long id = testInfoAdapter.getItemId(info.position);
        TestInfo itemAtPosition = (TestInfo) listView.getItemAtPosition(id.intValue());
        if (item.getTitle().toString().contains("Delete")) {
            deleteReportEntry(itemAtPosition);
        }
        return true;
    }

    private void deleteReportEntry(TestInfo itemAtPosition) {
        testInfoDB.deleteTestEntry(itemAtPosition.getTestCode());
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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
}

