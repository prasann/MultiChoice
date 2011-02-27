package com.prasans.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.prasans.R;

import static com.prasans.utils.AppConstants.COUNT;
import static com.prasans.utils.AppConstants.TEST_NAME;
import static java.lang.String.valueOf;

public class EnterAnswerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_answers);
        Bundle bundle = getIntent().getExtras();
        setTextTo(R.id.testNameDisplay, bundle.getString(TEST_NAME));
        setTextTo(R.id.quesCountDisplay, valueOf(bundle.getInt(COUNT)));
    }

    private void setTextTo(int textField, String testName) {
        TextView textView = (TextView) findViewById(textField);
        textView.setText(testName);
    }

}
