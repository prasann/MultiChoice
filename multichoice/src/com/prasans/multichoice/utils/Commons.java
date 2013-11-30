package com.prasans.multichoice.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Commons {

    public static void displayAlert(Context context, String title, String errorMessage, DialogInterface.OnClickListener o) {
        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(errorMessage)
                .setNeutralButton("Close", o).show();
    }
}
