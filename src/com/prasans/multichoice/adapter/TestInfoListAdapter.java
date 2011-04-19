package com.prasans.multichoice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.prasans.R;
import com.prasans.multichoice.domain.TestInfo;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TestInfoListAdapter extends ArrayAdapter<TestInfo> {


    private List<TestInfo> items;
    private Context myContext;

    public TestInfoListAdapter(Context context, int textViewResourceId, List<TestInfo> items) {
        super(context, textViewResourceId, items);
        this.items = items;
        this.myContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) myContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.row_test_info, null);
        }
        TestInfo testInfo = items.get(position);
        if (testInfo != null) {
            TextView testName = (TextView) view.findViewById(R.id.text_testName);
            TextView testCode = (TextView) view.findViewById(R.id.text_testCode);
            if (testCode != null) {
                testCode.setText("Code: " + testInfo.getTestCode());
            }
            if (testName != null) {
                testName.setText(testInfo.getTestName());
            }
        }
        return view;
    }
}
