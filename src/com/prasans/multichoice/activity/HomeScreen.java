package com.prasans.multichoice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.prasans.R;

public class HomeScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Button createTestBtn = (Button) findViewById(R.id.createBtn);
        createTestBtn.setOnClickListener(createTestBtnClk());

        Button reports = (Button) findViewById(R.id.reportsBtn);
        reports.setOnClickListener(reportsBtnClk());

        ImageView imageView = (ImageView)findViewById(R.id.aboutBtn);
        imageView.setOnClickListener(aboutBtnClk());
    }

    private View.OnClickListener aboutBtnClk() {
        return new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SplashScreen.class);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        };
    }

    private View.OnClickListener createTestBtnClk() {
        return new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TestInfoEntryScreen.class);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        };
    }

    private View.OnClickListener reportsBtnClk() {
        return new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReportsScreen.class);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        };
    }
}
