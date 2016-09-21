package com.app.mcb.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.Window;
import android.widget.ProgressBar;

import com.app.mcb.R;

/**
 * Created by u on 9/21/2016.
 */
public class ProgressDialog extends Dialog {
    public ProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.progress_dialog);
    }
}
