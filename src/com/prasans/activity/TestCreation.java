package com.prasans.activity;

import android.app.Activity;
import android.app.AlertDialog;
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
import static java.lang.Integer.parseInt;

public class TestCreation extends Activity {
    private TestInfoDB dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_test);
        Button button = (Button) findViewById(R.id.quesBtn);
        button.setOnClickListener(buttonListener());
        dbAdapter = new TestInfoDB(this);
    }

    private View.OnClickListener buttonListener() {
        return new View.OnClickListener() {
            public void onClick(View view) {
                String quesCount = getValueFrom(R.id.questCount);
                String testName = getValueFrom(R.id.testName);
                String testCode = getValueFrom(R.id.testCode);
                if (checkForValidQuesCount(quesCount)) {
                    displayAlert("Invalid Question Count");
                    return;
                }
                if (checkForTestCodeExistence(testCode)) {
                    displayAlert("Test Code already exists");
                    return;
                }
                Intent intent = new Intent(view.getContext(), EnterAnswer.class);
                Bundle bundle = new Bundle();
                bundle.putString(TEST_NAME, testName);
                bundle.putInt(COUNT, parseInt(quesCount));
                bundle.putString(TEST_CODE, testCode);
                intent.putExtras(bundle);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        };
    }

    private boolean checkForTestCodeExistence(String testCode) {
        if (testCode == null || testCode.equals("")) {
            return false;
        }
        dbAdapter.open();
        Cursor cursor = dbAdapter.fetchAllTests();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (testCode.equals(cursor.getString(2))) {
                return false;
            }
        }
        return true;
    }

    private String getValueFrom(int questCount) {
        EditText textBox = (EditText) findViewById(questCount);
        return textBox.getText().toString();
    }

    private void displayAlert(String errorMessage) {
        new AlertDialog.Builder(this).setTitle("Error")
                .setMessage(errorMessage)
                .setNeutralButton("Close", null).show();
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
