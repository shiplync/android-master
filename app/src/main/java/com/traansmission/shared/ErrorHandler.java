package com.traansmission.shared;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.WindowManager;

/**
 * Created by SAMBUCA on 3/8/16.
 */
public class ErrorHandler {

    private static final String NETWORK_ERROR_MSG = "No internet connection";

    private static ErrorHandler mInstance = null;

    private ErrorHandler(){

    }

    public static ErrorHandler getInstance(){
        if(mInstance == null)
        {
            mInstance = new ErrorHandler();
        }
        return mInstance;
    }

    public interface ErrorCallback {
        public void alertDismissed(Constants.ERR_TYPE errorType);
    }

    public void processError(Throwable err, String msg, Context context, final ErrorCallback callback) {
        if (msg == null || msg == "") {
            msg = "We apologize, but there was a problem processing your request.";
        }

        final Constants.ERR_TYPE errorType;
        if (err != null && err.getClass()==java.net.ConnectException.class) {
            msg = "Unable to connect to the internet";
            errorType = Constants.ERR_TYPE.NETWORK;
        } else {
            errorType = Constants.ERR_TYPE.UNKNOWN;
        }


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        callback.alertDismissed(errorType);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }


}
