package org.byteclues.lib.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Toast;

import org.byteclues.lib.init.Env;
import org.json.JSONObject;

/**
 * Created by admin on 19-07-2015.
 */
public class Util {
    public static final String VAL_OK = "OK";
    private static ProgressDialog progressDialog = null;
    private static AlertDialog alertDialog = null;
    public static String FLD_STATUS = "status";
    public static final String STATUS_CODE_USER_LOGOUT = "UserLogout";
    public static final String ACTION_USER_LOGOUT = "userLogout";
    public static final String KEY_USER_TOKEN = "user_token";
    public static final String KEY_LOGOUT_MESSAGE = "logout_message";


    public static void showProDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }
    }

    public static void dimissProDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void showCenteredToast(Context ctx, String msg) {
        Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static boolean isDeviceOnline() {
        boolean isDeviceOnLine = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) Env.appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isDeviceOnLine = netInfo != null && netInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDeviceOnLine;
    }

    public static boolean checkValueForKey(JSONObject jsonObject, String key) {
        boolean b = false;
        if (jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key)) {
            b = true;
        }
        return b;
    }

    public static AlertDialog showAlertDialog(DialogInterface.OnClickListener okClicklistener, String msg) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Env.currentActivity);
        DialogInterface.OnClickListener clickListener = okClicklistener != null ? okClicklistener : new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                alertDialog = null;
            }
        };
        builder.setPositiveButton("Ok", clickListener);
        builder.setMessage(msg);
        alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
