package com.app.mcb.viewControllers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.CommonResponseData;
import com.app.mcb.dao.ForgotPasswordData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.model.UserAuthenticationModel;
import com.app.mcb.sharedPreferences.Config;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh on 02-11-2016.
 */
public class ForgotPasswordActivity extends AbstractFragmentActivity implements View.OnClickListener {

    private TextView txtSubmitForgotPassword;
    private EditText etEmailForgotPassword;
    private LinearLayout llLoginMain;
    private UserAuthenticationModel userAuthenticationModel = new UserAuthenticationModel();
    private Bundle bundle;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.forgot_password_activity);
        init();
    }

    private void init() {
        llLoginMain = (LinearLayout) findViewById(R.id.llLoginMain);
        txtSubmitForgotPassword = (TextView) findViewById(R.id.txtSubmitForgotPassword);
        etEmailForgotPassword = (EditText) findViewById(R.id.etEmailForgotPassword);
        etEmailForgotPassword.setText("lalit.sharma@byteclues.com");
        txtSubmitForgotPassword.setOnClickListener(this);
    }

    @Override
    protected BasicModel getModel() {
        return userAuthenticationModel;
    }

    @Override
    public void update(Observable o, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof ForgotPasswordData) {
                ForgotPasswordData userInfoData = (ForgotPasswordData) data;
                if (userInfoData.status.equals("success")) {
                    Util.showOKSnakBar(llLoginMain, userInfoData.response);
                } else if (userInfoData.status.equals("Error")) {
                    Util.showOKSnakBar(llLoginMain, userInfoData.errorMessage);
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llLoginMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public boolean validation(String email) {
        if (TextUtils.isEmpty(email)) {

            etEmailForgotPassword.setError(getString(R.string.can_not_be_empty));
            etEmailForgotPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void forgotPassword(String email) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                userAuthenticationModel.forgotPassword(email);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txtSubmitForgotPassword) {
            String email = etEmailForgotPassword.getText().toString();
            if (validation(email)) {
                forgotPassword(email);
            }
        }
    }
}

