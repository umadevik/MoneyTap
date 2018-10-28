package com.android.moneytap.progressdialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager.BadTokenException;

import com.android.moneytap.R;


public class ProgressBar {

    Context context;

    public ProgressDialog dialog;


    public ProgressBar(Context mContext) {

        this.context = mContext;

    }
    public ProgressDialog createProgressDialog() {

        dialog = new ProgressDialog(context);

        try {
            dialog.setMessage("Please wait...");
            dialog.show();
        } catch (BadTokenException e) {
            e.printStackTrace();
        }
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        return dialog;
    }

    public  void dismissProgressDialog() {

        if (dialog != null) {

            dialog.dismiss();
        }
    }


}
