package com.android.moneytap.alertmessage;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertMessage {
    private static final String TAG = "AlertMessage";


    Context context;

    static AlertDialog alertDialog;

    private AlertDialog.Builder alertDialogBuilder = null;

    public AlertMessage(Context mContext) {

        this.context = mContext;

    }

    // showAlertDialogNetworkChecking
    public AlertDialog showAlertDialogNetworkChecking() {

        alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle("Error");

        // set dialog message
        alertDialogBuilder
                .setMessage("Internet connection is not available.")
                .setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        return alertDialog;

    }
    // showAlertDialogServerIsnotResponding
    public AlertDialog showAlertDialogServerIsnotResponding() {

        alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle("Error");

        // set dialog message
        alertDialogBuilder
                .setMessage("Server is not Running")
                .setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        return alertDialog;

    }

}
