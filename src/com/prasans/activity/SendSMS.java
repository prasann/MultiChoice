package com.prasans.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

import static android.telephony.SmsManager.getDefault;
import static com.prasans.utils.AppConstants.PHONE_NUMBER;

public class SendSMS extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String phoneNumber = bundle.getString(PHONE_NUMBER);
        int score = bundle.getInt("score");
        String message = "You have scored " + score;
        sendSms(phoneNumber, message);
        Intent intent = new Intent(this, HomeScreen.class);
        startActivityForResult(intent, RESULT_FIRST_USER);
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Log.d("SendSMS", "To: " + phoneNumber + "Message : " + message);
        } catch (Exception e) {
            Log.e("Failure - SendSMS", "To: " + phoneNumber + "Message : " + message);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
