package com.prasans.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSMonitor extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null &&
                ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
            Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] messages = new SmsMessage[pduArray.length];

            for (int i = 0; i < pduArray.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                String message = messages[i].getDisplayMessageBody();
                String phoneNumber = messages[i].getDisplayOriginatingAddress();
                long receivedTime = messages[i].getTimestampMillis();
                evaluateReceivedText(message, phoneNumber, receivedTime,context).execute();
            }
            Log.d("MySMSMonitor", "SMS Message Received.");

        }
    }

    private EvaluateReceivedText evaluateReceivedText(String message, String phoneNumber, long receivedTime,Context context) {
        return new EvaluateReceivedText(phoneNumber,message,receivedTime, context);
    }
}
