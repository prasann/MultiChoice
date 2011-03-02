package com.prasans.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.prasans.R;

public class HomeScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Button button = (Button) findViewById(R.id.createBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateTest.class);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        });
    }
}
