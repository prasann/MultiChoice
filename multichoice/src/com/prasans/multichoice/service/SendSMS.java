package com.prasans.multichoice.service;

import android.telephony.SmsManager;
import android.util.Log;

import static android.telephony.SmsManager.getDefault;

public class SendSMS{
    public void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Log.d("SendSMS", "To: " + phoneNumber + "Message : " + message);
        } catch (Exception e) {
            Log.e("Failure - SendSMS", "To: " + phoneNumber + "Message : " + message);
        }
    }
}
