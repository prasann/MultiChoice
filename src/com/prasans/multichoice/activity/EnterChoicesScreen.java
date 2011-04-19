package com.prasans.multichoice.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import com.prasans.multichoice.adapter.TestInfoDB;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.WHITE;
import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;
import static com.prasans.multichoice.utils.AppConstants.COUNT;
import static com.prasans.multichoice.utils.AppConstants.TEST_CODE;
import static com.prasans.multichoice.utils.AppConstants.TEST_NAME;
import static com.prasans.multichoice.utils.Commons.displayAlert;
import static java.lang.String.valueOf;

public class EnterChoicesScreen extends Activity {
    private TestInfoDB testInfoDB;
    private List<RadioGroup> radioGroupList = new ArrayList<RadioGroup>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        testInfoDB = new TestInfoDB(this);

        LinearLayout linearLayout = new LinearLayout(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(FILL_PARENT, WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(VERTICAL);
        addStaticFieldsAtTop(bundle, linearLayout);
        int count = bundle.getInt(COUNT);
        for (int i = 1; i <= count; i++) {
            linearLayout.addView(addRadioButtons(i));
        }
        linearLayout.addView(submitButton());
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(linearLayout);
        setContentView(scrollView);

    }

    private void addStaticFieldsAtTop(Bundle bundle, LinearLayout linearLayout) {
        linearLayout.addView(textView("Enter the Answers for the Questions"));
        linearLayout.addView(textView("Test Name :" + bundle.getString(TEST_NAME)));
        linearLayout.addView(textView("Test Code :" + bundle.getString(TEST_CODE)));
        linearLayout.addView(textView("No. Of Questions: " + bundle.getInt(COUNT)));
    }

    private TextView textView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        return textView;
    }

    private LinearLayout addRadioButtons(int rowId) {
        LinearLayout linearLayout = new LinearLayout(EnterChoicesScreen.this);
        TextView textView = questionNumber(rowId);
        RadioGroup radioGroup = radioGroup();
        linearLayout.addView(textView);
        linearLayout.addView(radioGroup);
        linearLayout.setPadding(0, 10, 0, 0);
        return linearLayout;
    }

    private TextView questionNumber(int rowId) {
        TextView textView = new TextView(EnterChoicesScreen.this);
        textView.setGravity(Gravity.CENTER);
        textView.setText(rowId + ". ");
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(WRAP_CONTENT, FILL_PARENT);
        textView.setLayoutParams(params);
        return textView;
    }

    private RadioGroup radioGroup() {
        RadioGroup radioGroup = new RadioGroup(EnterChoicesScreen.this);
        radioGroup.setOrientation(HORIZONTAL);
        radioGroup.addView(radioButton("a       "));
        radioGroup.addView(radioButton("b       "));
        radioGroup.addView(radioButton("c       "));
        radioGroup.addView(radioButton("d       "));
        radioGroupList.add(radioGroup);
        return radioGroup;
    }

    private RadioButton radioButton(String text) {
        RadioButton radioButton = new RadioButton(this);
        radioButton.setText(text);
        radioButton.setTextColor(WHITE);
        return radioButton;
    }

    private Button submitButton() {
        Button button = new Button(this);
        button.setHeight(WRAP_CONTENT);
        button.setText("Submit");
        button.setOnClickListener(submitListener);
        return button;
    }

    private View.OnClickListener submitListener = new View.OnClickListener() {
        public void onClick(View view) {
            String answers = buildAnswers();
            if (answers.length() != radioGroupList.size()) {
                displayAlert(EnterChoicesScreen.this, "Error", "Answers: "+answers + "RGL: " +radioGroupList.size(), null);
                return;
            }
            Bundle bundle = getIntent().getExtras();
            String testName = bundle.getString(TEST_NAME);
            String testCode = bundle.getString(TEST_CODE).toLowerCase();
            int count = bundle.getInt(COUNT);
            testInfoDB.createTestEntry(testName, testCode, count, answers);
            displayAlert(EnterChoicesScreen.this, "Success", "All the details stored successfully", proceedToHomeScreen());
        }
    };

    private DialogInterface.OnClickListener proceedToHomeScreen() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(EnterChoicesScreen.this, HomeScreen.class);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        };
    }

    private String buildAnswers() {
        StringBuilder answerBuilder = new StringBuilder();
        for (RadioGroup radioGroup : radioGroupList) {
            RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            if (radioButton != null) {
                answerBuilder.append(valueOf(radioButton.getText()).trim());
            }
        }
        return answerBuilder.toString();
    }
}


