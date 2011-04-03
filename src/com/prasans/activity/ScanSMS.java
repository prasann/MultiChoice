package com.prasans.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.prasans.adapter.ResultsDB;
import com.prasans.utils.Commons;

import static com.prasans.utils.AppConstants.MESSAGE;
import static com.prasans.utils.AppConstants.PHONE_NUMBER;
import static com.prasans.utils.AppConstants.RECEIVED_TIME;

public class ScanSMS extends Activity {
    private ProgressDialog m_ProgressDialog = null;
    private static final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
    private ResultsDB resultsDB;
    private Runnable scanMessages;
    private Thread thread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultsDB = new ResultsDB(this);
        scanMessages = new Runnable() {
            public void run() {
                scanMessages();
            }
        };
        m_ProgressDialog = ProgressDialog.show(this,
                "Please wait...", "Scanning Messages ...", true);

        thread = new Thread(null, scanMessages, "MagentoBackground");
        thread.start();


    }

    private Runnable returnRes = new Runnable() {
        public void run() {
            m_ProgressDialog.dismiss();

            DialogInterface.OnClickListener onclickListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(ScanSMS.this, HomeScreen.class);
                    startActivityForResult(intent, RESULT_FIRST_USER);
                }
            };
            Commons.displayAlert(ScanSMS.this, "Success", "Completed message scanning", onclickListener);
        }
    };

    private void scanMessages() {
        Cursor cursor = getContentResolver().query(SMS_INBOX, null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String message = cursor.getString(cursor.getColumnIndex("body"));
            String sender = cursor.getString(cursor.getColumnIndex("address"));
            long receivedAt = cursor.getLong(cursor.getColumnIndex("date"));
            if (shouldProcess(message, sender, receivedAt)) {
                Log.d("Message : ", message);
                Bundle bundle = new Bundle();
                bundle.putString(MESSAGE, message);
                bundle.putString(PHONE_NUMBER, sender);
                bundle.putLong(RECEIVED_TIME, receivedAt);
                Intent intent = new Intent(this, EvaluateReceivedText.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        }
        cursor.close();
        runOnUiThread(returnRes);
    }

    private boolean shouldProcess(String message, String sender, long receivedAt) {
        Log.d("Received At: ", String.valueOf(receivedAt));
        String[] splitMessage = message.split(" ");
        if (splitMessage.length != 2) {
            return false;
        }
        Cursor cursor = resultsDB.fetchResultEntryFor(splitMessage[0], sender, receivedAt);
        boolean containsResult = cursor.moveToFirst();
        cursor.close();
        resultsDB.close();
        return !containsResult;
    }
}
