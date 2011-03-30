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
import com.prasans.utils.AppConstants;
import com.prasans.utils.Commons;

public class ScanSMS extends Activity {
    private ProgressDialog m_ProgressDialog = null;
    private static final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
    private ResultsDB resultsDB;
    private Runnable scanMessages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultsDB = new ResultsDB(this);
        scanMessages = new Runnable() {
            public void run() {
                scanMessages();
            }
        };
        Thread thread = new Thread(null, scanMessages, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(this,
                "Please wait...", "Scanning Messages ...", true);

        DialogInterface.OnClickListener onclickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ScanSMS.this, HomeScreen.class);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        };
        Commons.displayAlert(this, "Success", "Completed message scanning", onclickListener);

    }

    private Runnable returnRes = new Runnable() {
        public void run() {
            m_ProgressDialog.dismiss();
        }
    };

    private void scanMessages() {
        Cursor cursor = getContentResolver().query(SMS_INBOX, null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String message = cursor.getString(cursor.getColumnIndex("body"));
            String sender = cursor.getString(cursor.getColumnIndex("address"));
            if (shouldProcess(message, sender)) {
                Log.d("Message : ", message);
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.MESSAGE, message);
                Intent intent = new Intent(this, EvaluateReceivedText.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, RESULT_FIRST_USER);
            }
        }
        cursor.close();
        runOnUiThread(returnRes);
    }

    private boolean shouldProcess(String message, String sender) {
        String[] splitMessage = message.split(" ");
        if (splitMessage.length != 2) {
            return false;
        }
        Cursor cursor = resultsDB.fetchResultEntryFor(splitMessage[0], sender);
        boolean containsResult = cursor.moveToFirst();
        cursor.close();
        return !containsResult;
    }
}
