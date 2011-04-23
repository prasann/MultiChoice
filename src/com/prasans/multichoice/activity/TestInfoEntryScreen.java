package com.prasans.multichoice.activity;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.prasans.R;
import com.prasans.multichoice.adapter.TestInfoDB;

import static com.prasans.multichoice.utils.AppConstants.*;
import static com.prasans.multichoice.utils.Commons.displayAlert;
import static java.lang.Integer.parseInt;

public class TestInfoEntryScreen extends Activity {
    private TestInfoDB testInfoDB;
    private WheelView wrongAnswers;
    private WheelView correctAnswers;
    private NumericWheelAdapter wrongAnswersAdapter;
    private NumericWheelAdapter correctAnswersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_test);
        Button button = (Button) findViewById(R.id.quesBtn);
        button.setOnClickListener(buttonListener());
        testInfoDB = new TestInfoDB(this);
        seekBarForQuesCount();
        setWheelAdapters();
    }

	private void seekBarForQuesCount() {
		SeekBar questSeekBar = (SeekBar) findViewById(R.id.questSeek);
        
        questSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {	
			public void onStopTrackingTouch(SeekBar arg0) {
			}
			
			public void onStartTrackingTouch(SeekBar arg0) {
			}
			
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				((TextView) TestInfoEntryScreen.this.findViewById(R.id.questCount)).setText(String.valueOf(progress+1));
			}
		});
	}

	private void setWheelAdapters() {
		wrongAnswers = (WheelView) findViewById(R.id.wrongAnswers);
        wrongAnswersAdapter = new NumericWheelAdapter(this,-5,0);
		wrongAnswers.setViewAdapter(wrongAnswersAdapter);
		wrongAnswers.setCurrentItem(5);
		
		correctAnswers = (WheelView) findViewById(R.id.correctAnswers);
		correctAnswersAdapter = new NumericWheelAdapter(this,0,5);
		correctAnswers.setViewAdapter(correctAnswersAdapter);
		correctAnswers.setCurrentItem(1);
	}

    private View.OnClickListener buttonListener() {
        return new View.OnClickListener() {
            public void onClick(View view) {
                TextView textView = (TextView)findViewById(R.id.questCount);
                String quesCount = textView.getText().toString();
                String testName = getValueFrom(R.id.testName);
                String testCode = getValueFrom(R.id.testCode).toLowerCase();
                if (checkForValidations(quesCount, testCode)) return;
                Intent intent = new Intent(view.getContext(), EnterChoicesScreen.class);
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
        bundle.putInt(WRONG_ANSWER_SCORE,Integer.valueOf(String.valueOf(wrongAnswersAdapter.getItemText(wrongAnswers.getCurrentItem()))));
        bundle.putInt(CORRECT_ANSWER_SCORE,Integer.valueOf(String.valueOf(correctAnswersAdapter.getItemText(correctAnswers.getCurrentItem()))));
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
