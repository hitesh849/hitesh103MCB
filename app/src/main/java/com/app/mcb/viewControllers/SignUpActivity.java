package com.app.mcb.viewControllers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.TripListStateWiseAdapter;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.TripData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.model.UserAuthenticationModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.net.Authenticator;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by u on 9/16/2016.
 */
public class SignUpActivity extends AbstractFragmentActivity implements View.OnClickListener {

    private LinearLayout llSignUpMain;
    private TextView txtRagisterSignUp;
    private AppHeaderView appHeaderView;
    private EditText etextFirstName;
    private EditText etextLastName;
    private EditText etextEmail;
    private EditText etextPassword;
    private EditText etextMobile;
    private EditText etextConfirmPassword;
    private UserInfoData userInfoData = new UserInfoData();
    private UserAuthenticationModel userAuthenticationModel = new UserAuthenticationModel();

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {

        setContentView(R.layout.sign_up);
        init();
    }

    private void init() {
        llSignUpMain = (LinearLayout) findViewById(R.id.llSignUpMain);
        txtRagisterSignUp = (TextView) findViewById(R.id.txtRagisterSignUp);
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        etextFirstName = (EditText) findViewById(R.id.etextFirstName);
        etextLastName = (EditText) findViewById(R.id.etextLastName);
        etextEmail = (EditText) findViewById(R.id.etextEmail);
        etextMobile = (EditText) findViewById(R.id.etextMobile);
        etextPassword = (EditText) findViewById(R.id.etextPassword);
        etextConfirmPassword = (EditText) findViewById(R.id.etextConfirmPassword);
        appHeaderView.txtHeaderNamecenter.setText(getResources().getString(R.string.sign_up));
        txtRagisterSignUp.setOnClickListener(this);
    }

    @Override
    protected BasicModel getModel() {
        return userAuthenticationModel;
    }


    private boolean validation() {
        userInfoData.firstName = etextFirstName.getText().toString();
        userInfoData.lastName = etextLastName.getText().toString();
        userInfoData.email = etextEmail.getText().toString();
        userInfoData.password = etextPassword.getText().toString();
        userInfoData.mobile = etextMobile.getText().toString();


        if (TextUtils.isEmpty(userInfoData.firstName)) {
            etextFirstName.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.lastName)) {
            etextLastName.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.email)) {
            etextEmail.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.mobile)) {
            etextPassword.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.password)) {
            etextPassword.setError("Can't be Empty");
            return false;
        }

        if (TextUtils.isEmpty(etextConfirmPassword.getText().toString())) {
            etextConfirmPassword.setError("Can't be Empty");
            return false;
        }
        if (!userInfoData.password.equals(etextConfirmPassword.getText().toString())) {
            etextConfirmPassword.setError(getResources().getString(R.string.pass_do_not_match_error_msg));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txtRagisterSignUp) {
            if (validation()) {
                registerUser();
            }
        } else if (id == R.id.llBackHeader) {
            onBackPressed();
        }
    }

    private void registerUser() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                userAuthenticationModel.registerUser(userInfoData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof UserInfoData) {
                UserInfoData userInfoData = (UserInfoData) data;
                if (userInfoData.status.equals("success")) {
                    if (userInfoData.response != null) {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                } else if (userInfoData.status.equals("Error")) {
                    Util.showOKSnakBar(llSignUpMain, userInfoData.errorMessage);
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llSignUpMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
