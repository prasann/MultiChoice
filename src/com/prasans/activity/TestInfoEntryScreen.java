package com.prasans.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.prasans.R;
import com.prasans.adapter.TestInfoDB;

import static com.prasans.utils.AppConstants.COUNT;
import static com.prasans.utils.AppConstants.TEST_CODE;
import static com.prasans.utils.AppConstants.TEST_NAME;
import static com.prasans.utils.Commons.displayAlert;
import static java.lang.Integer.parseInt;

public class TestInfoEntryScreen extends Activity {
    private TestInfoDB testInfoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_test);
        Button button = (Button) findViewById(R.id.quesBtn);
        button.setOnClickListener(buttonListener());
        testInfoDB = new TestInfoDB(this);
    }

    private View.OnClickListener buttonListener() {
        return new View.OnClickListener() {
            public void onClick(View view) {
                String quesCount = getValueFrom(R.id.questCount);
                String testName = getValueFrom(R.id.testName);
                String testCode = getValueFrom(R.id.testCode);
                if (checkForValidations(quesCount, testCode)) return;
                Intent intent = new Intent(view.getContext(), EnterAnswersScreen.class);
                createBundleToPassUpon(quesCount, testName, testCode, intent);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        };
    }

    private boolean checkForValidations(String quesCount, String testCode) {
        if (checkForValidQuesCount(quesCount)) {
            displayAlert(this, "Error", "Invalid Question Count", null);
            return true;
        }
        if (checkForTestCodeExistence(testCode)) {
            displayAlert(this, "Error", "Test Code already exists", null);
            return true;
        }
        return false;
    }

    private void createBundleToPassUpon(String quesCount, String testName, String testCode, Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putString(TEST_NAME, testName);
        bundle.putInt(COUNT, parseInt(quesCount));
        bundle.putString(TEST_CODE, testCode);
        intent.putExtras(bundle);
    }

    private boolean checkForTestCodeExistence(String testCode) {
        if (testCode == null || testCode.equals("")) {
            return true;
        }
        Cursor cursor = testInfoDB.fetchAllTests();
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            if (testCode.equals(cursor.getString(2))) {
                return true;
            }
        }
        cursor.close();
        testInfoDB.close();
        return false;
    }

    private String getValueFrom(int questCount) {
        EditText textBox = (EditText) findViewById(questCount);
        return textBox.getText().toString();
    }

    private boolean checkForValidQuesCount(String quesCount) {
        try {
            parseInt(quesCount);
        } catch (NumberFormatException nfe) {
            return true;
        }
        return false;
    }
}
