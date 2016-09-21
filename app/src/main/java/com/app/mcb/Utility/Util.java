package com.app.mcb.Utility;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.app.mcb.R;

import org.byteclues.lib.view.AbstractFragment;
import org.byteclues.lib.view.AbstractFragmentActivity;

/**
 * Created by u on 9/15/2016.
 */
public class Util {
    private static ProgressDialog progressDialog;

    public static void replaceFragment(Context context, int container, AbstractFragment abstractFragment) {
        ((AbstractFragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(container, abstractFragment).commit();
    }

    public static void addFragment(Context context, int container, AbstractFragment abstractFragment) {
        ((AbstractFragmentActivity) context).getSupportFragmentManager().beginTransaction().add(container, abstractFragment).commit();
    }

    public static Dialog showProDialog(Context context) {
        try {

            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.progress_spiner));
               // progressDialog.setCancelable(false);
                progressDialog.setMessage("Please Wait..");
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;
    }


    public static Dialog dimissProDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;
    }

}
