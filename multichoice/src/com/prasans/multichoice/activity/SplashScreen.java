package com.prasans.multichoice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.prasans.R;

public class SplashScreen extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        LinearLayout parent = (LinearLayout) findViewById(R.id.splash);
        TextView about = (TextView)findViewById(R.id.aboutTxt);
        StringBuilder aboutText = new StringBuilder();
        aboutText.append("A SMS Based application to conduct a multiple choice tests/contests.\n\n");
        aboutText.append("Allows you to store a set of choices for a specific test and monitors your incoming SMSes for the answer pattern. ");
        aboutText.append("Whenever an answer pattern matches evaluates the answer and sends back the score to the Sender. ");
        aboutText.append("Also you can view the reports summary for your created tests/contests.");
        about.setText(aboutText);

        parent.setOnClickListener(closeSplashScreen());
        about.setOnClickListener(closeSplashScreen());
    }

    private View.OnClickListener closeSplashScreen() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        };
    }
}