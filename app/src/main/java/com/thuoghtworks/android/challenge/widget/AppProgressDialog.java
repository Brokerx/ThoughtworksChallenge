package com.thuoghtworks.android.challenge.widget;

import android.app.Dialog;
import android.content.Context;

import com.thuoghtworks.android.challenge.R;

/**
 * Created by Govind on 08-Jan-17.
 */

public class AppProgressDialog {

    public static Dialog show(Context ctx){
        final Dialog dialog = new Dialog(ctx, R.style.DialogFullScreen);
        dialog.setContentView(R.layout.app_progess_dialog);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }
}
