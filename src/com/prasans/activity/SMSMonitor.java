package com.prasans.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

            Bundle bundle = new Bundle();
            for (int i = 0; i < pduArray.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                bundle.putString("message",messages[i].getDisplayMessageBody());
                bundle.putString("phoneNumber",messages[i].getDisplayOriginatingAddress());
            }
            Log.d("MySMSMonitor", "SMS Message Received.");

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.putExtras(bundle);
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            callIntent.setClass(context, EvaluateReceivedText.class);
            context.startActivity(callIntent);
        }
    }
}
