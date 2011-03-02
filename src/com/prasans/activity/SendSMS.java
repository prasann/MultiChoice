package com.prasans.activity;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import static android.telephony.SmsManager.getDefault;

public class SendSMS extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String phoneNumber = bundle.getString("phoneNumber");
        String message = "Thanks!!";
        sendSms(phoneNumber, message);
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS Sent", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
