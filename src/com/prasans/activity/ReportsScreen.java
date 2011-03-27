package com.prasans.activity;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.prasans.R;
import com.prasans.adapter.TestInfoDB;
public class ReportsScreen extends ListActivity {

    private TestInfoDB testInfoDB;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        testInfoDB = new TestInfoDB(this);
        Cursor cursor = testInfoDB.fetchAllTests();
        startManagingCursor(cursor);
        String[] cols = new String[]{TestInfoDB.TEST_NAME};
        int[] names = new int[]{R.id.row_tv};

        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.report_list, cursor, cols, names);
        this.setListAdapter(simpleCursorAdapter);
    }
}

