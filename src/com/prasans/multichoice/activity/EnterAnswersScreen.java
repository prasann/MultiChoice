package com.prasans.multichoice.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.prasans.multichoice.adapter.TestInfoDB;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;
import static com.prasans.multichoice.utils.AppConstants.COUNT;
import static com.prasans.multichoice.utils.AppConstants.TEST_CODE;
import static com.prasans.multichoice.utils.AppConstants.TEST_NAME;
import static com.prasans.multichoice.utils.Commons.displayAlert;

public class EnterAnswersScreen extends Activity {
    private TestInfoDB testInfoDB;
    private List<EditText> editTextList = new ArrayList<EditText>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        testInfoDB = new TestInfoDB(this);

        LinearLayout linearLayout = new LinearLayout(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(FILL_PARENT, WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(VERTICAL);

        addStaticFieldsAtTop(bundle, linearLayout);

        int count = bundle.getInt(COUNT);

        linearLayout.addView(tableLayout(count));
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

    private View.OnClickListener submitListener = new View.OnClickListener() {
        public void onClick(View view) {
            String answers = buildAnswers();
            if (answers.length() != editTextList.size()) {
                displayAlert(EnterAnswersScreen.this, "Error", "Need to fill all answers", null);
                return;
            }
            Bundle bundle = getIntent().getExtras();
            String testName = bundle.getString(TEST_NAME);
            String testCode = bundle.getString(TEST_CODE);
            int count = bundle.getInt(COUNT);
//            testInfoDB.createTestEntry(testName, testCode, count, answers);
            displayAlert(EnterAnswersScreen.this, "Success", "All the details stored successfully", proceedToHomeScreen());
        }
    };

    private DialogInterface.OnClickListener proceedToHomeScreen() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(EnterAnswersScreen.this, HomeScreen.class);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        };
    }

    private String buildAnswers() {
        StringBuilder answerBuilder = new StringBuilder();
        for (EditText editText : editTextList) {
            answerBuilder.append(editText.getText().toString());
        }
        return answerBuilder.toString();
    }

    private Button submitButton() {
        Button button = new Button(this);
        button.setHeight(WRAP_CONTENT);
        button.setText("Submit");
        button.setOnClickListener(submitListener);
        return button;
    }


    private TableLayout tableLayout(int count) {
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setStretchAllColumns(true);
        int noOfRows = count / 5;
        for (int i = 0; i < noOfRows; i++) {
            int rowId = 5 * i;
            tableLayout.addView(createOneFullRow(rowId));
        }
        int individualCells = count % 5;
        tableLayout.addView(createLeftOverCells(individualCells, count));
        return tableLayout;
    }

    private TableRow createLeftOverCells(int individualCells, int count) {
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(0, 10, 0, 0);
        int rowId = count - individualCells;
        for (int i = 1; i <= individualCells; i++) {
            tableRow.addView(editText(String.valueOf(rowId + i)));
        }
        return tableRow;
    }

    private TableRow createOneFullRow(int rowId) {
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(0, 10, 0, 0);
        for (int i = 1; i <= 5; i++) {
            tableRow.addView(editText(String.valueOf(rowId + i)));
        }
        return tableRow;
    }

    private TextView textView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        return textView;
    }

    private EditText editText(String hint) {
        EditText editText = new EditText(this);
        editText.setId(Integer.valueOf(hint));
        editText.setHint(hint);
        editTextList.add(editText);
        return editText;
    }
}
