package com.prasans.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactInfoAdapter {
    private Context context;

    public ContactInfoAdapter(Context context) {
        this.context = context;
    }

    public String lookUp(String phoneNumber) {
        String name = null;
        Cursor c = null;
        try {
            Uri lookupUri = Uri.withAppendedPath(
                    ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            c = context.getContentResolver().query(lookupUri,
                    new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                    null, null, null);
            if(c.moveToFirst()){
                name = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        if(name != null){
            return name;
        }
        return phoneNumber;
    }
}
