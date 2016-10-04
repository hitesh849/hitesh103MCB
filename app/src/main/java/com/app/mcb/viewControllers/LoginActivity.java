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
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.model.UserAuthenticationModel;
import com.app.mcb.sharedPreferences.Config;
import com.app.mcb.viewControllers.sender.SenderHomeFragment;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by u on 9/16/2016.
 */
public class LoginActivity extends AbstractFragmentActivity implements View.OnClickListener {

    private TextView txtLogin;
    private TextView txtSignUp;
    private EditText etEmailLogin;
    private EditText etPasswordLogin;
    private TextView txtForgetPasswordLogin;
    private LinearLayout llLoginMain;
    private UserAuthenticationModel userAuthenticationModel = new UserAuthenticationModel();

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {

        setContentView(R.layout.login);
        init();
    }

    private void init() {
        llLoginMain = (LinearLayout) findViewById(R.id.llLoginMain);
        txtLogin = (TextView) findViewById(R.id.txtLogin);
        txtSignUp = (TextView) findViewById(R.id.txtSignUpLogin);
        txtForgetPasswordLogin = (TextView) findViewById(R.id.txtForgetPasswordLogin);
        etEmailLogin = (EditText) findViewById(R.id.etEmailLogin);
        etPasswordLogin = (EditText) findViewById(R.id.etPasswordLogin);
        txtLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        txtForgetPasswordLogin.setOnClickListener(this);
    }

    @Override
    protected BasicModel getModel() {
        return userAuthenticationModel;
    }

    @Override
    public void update(Observable o, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof UserInfoData) {
                UserInfoData userInfoData = (UserInfoData) data;
                if (userInfoData.status.equals("success")) {
                    if (userInfoData.response != null) {
                        userInfoData = userInfoData.response.get(0);
                        Config.setLoginStatus(true);
                        Config.savePreferences();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
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

    public boolean validation(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            etEmailLogin.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etPasswordLogin.setError("Can't be Empty");
            return false;
        }
        return true;
    }

    private void userLogin(String email, String password) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                userAuthenticationModel.getLoginUser(email, password);
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
        if (id == R.id.txtLogin) {

            String email = etEmailLogin.getText().toString();
            String password = etPasswordLogin.getText().toString();
            if (validation(email, password)) {
                userLogin(email, password);
            }

        } else if (id == R.id.txtSignUpLogin) {
            startActivity(new Intent(this, SignUpActivity.class));
        } else if (id == R.id.txtForgetPasswordLogin) {

        }
    }
}
