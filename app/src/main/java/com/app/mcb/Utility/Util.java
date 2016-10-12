package com.app.mcb.Utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mcb.R;
import com.squareup.picasso.Picasso;

import org.byteclues.lib.init.Env;
import org.byteclues.lib.view.AbstractFragment;
import org.byteclues.lib.view.AbstractFragmentActivity;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by u on 9/15/2016.
 */
public class Util {
    public static final String VAL_OK = "OK";
    private static ProgressDialog progressDialog = null;
    private static AlertDialog alertDialog = null;
    private static Snackbar retrySnackbar = null;
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

    public static void writeDBfileOnSdcard(Context context, String databaseFileName) {
        try {
            File f = new File(context.getDatabasePath(databaseFileName).getAbsolutePath());
            FileInputStream fis = null;
            FileOutputStream fos = null;

            try {
                fis = new FileInputStream(f);
                fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + databaseFileName + ".sqlite");
                while (true) {
                    int i = fis.read();
                    if (i != -1) {
                        fos.write(i);
                    } else {
                        break;
                    }
                }
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Context context, ImageView imgView, String url, int defaultResource) {
        try {
            Picasso.with(context).load(url).fit().centerCrop().error(defaultResource).fit().placeholder(defaultResource).fit().into(imgView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) Env.currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void replaceFragment(Context context, int container, AbstractFragment abstractFragment) {
        ((AbstractFragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(container, abstractFragment).commit();
    }

    public static void addFragment(Context context, int container, AbstractFragment abstractFragment) {
        ((AbstractFragmentActivity) context).getSupportFragmentManager().beginTransaction().add(container, abstractFragment).commit();
    }

    public static void showOKSnakBar(android.view.View view, String msg) {
        try {
            if (view != null && msg != null) {
                retrySnackbar = Snackbar
                        .make(view, msg, Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                retrySnackbar.dismiss();
                            }
                        });
                ViewGroup group = (ViewGroup) retrySnackbar.getView();
                group.setBackgroundColor(Env.appContext.getResources().getColor(R.color.trans_blue));
                retrySnackbar.setActionTextColor(Env.appContext.getResources().getColor(R.color.primary_blue));
                View sbView = retrySnackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);

                textView.setTextColor(Env.appContext.getResources().getColor(R.color.primary_blue));
                retrySnackbar.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static String getCurrentDate() {
        DateTime now = new DateTime();
        LocalDate today = now.toLocalDate();
        return today.toString().replaceAll("-", "/");
    }

    public static String getDateFromDateTimeFormat(String dateTime) {

        try {

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime obj = formatter.parseDateTime(dateTime).toDateTime();
            DateTimeFormatter localDateFormat = DateTimeFormat.forPattern("dd MMM yyyy");
            obj.toString(localDateFormat);
            return  obj.toString(localDateFormat);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getDateDDMMYY(String YYMMDD) {

        try {

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime obj = formatter.parseDateTime(YYMMDD).toDateTime();
            DateTimeFormatter localDateFormat = DateTimeFormat.forPattern("dd/MM/yyyy");
            obj.toString(localDateFormat);
            return  obj.toString(localDateFormat);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static String getTimeFromDateTimeFormat(String dateTime) {

        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime obj = formatter.parseDateTime(dateTime).toDateTime();
            DateTimeFormatter localDateFormat = DateTimeFormat.forPattern("HH:mm a");
            obj.toString(localDateFormat);
            return  obj.toString(localDateFormat);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static String getFirstName(String str)
    {
        String code[] = str.split(" ");
        return code[0];
    }

    public static String getdd_MM_YYYYFormat(String dateTime) {
        try {

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime obj = formatter.parseDateTime(dateTime).toDateTime();
            DateTimeFormatter localDateFormat = DateTimeFormat.forPattern("dd MMM yyyy");
            obj.toString(localDateFormat);
            return  obj.toString(localDateFormat);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
