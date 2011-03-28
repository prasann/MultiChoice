package com.prasans.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.prasans.utils.AppConstants;

public class ScanSMS extends Activity {
    private static final Uri SMS_INBOX = Uri.parse("content://sms/inbox");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cursor cursor = getContentResolver().query(SMS_INBOX, null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String message = cursor.getString(cursor.getColumnIndex("body"));
            if (isMultiChoiceSMS(message)) {
                Log.d("Message : " , message);
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.MESSAGE, message);
                Intent intent = new Intent(this, EvaluateReceivedText.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        }
        cursor.close();
    }

    private boolean isMultiChoiceSMS(String message) {
        String[] strings = message.split(" ");
        return strings.length == 2;
    }
}
