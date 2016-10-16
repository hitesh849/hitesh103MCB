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
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.model.UserAuthenticationModel;
import com.app.mcb.sharedPreferences.Config;

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
    private Bundle bundle;
    private TripTransporterData tripTransporterData;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.login);
        init();
        tripTransporterData = (TripTransporterData) getIntent().getSerializableExtra("data");
    }

    private void init() {
        llLoginMain = (LinearLayout) findViewById(R.id.llLoginMain);
        txtLogin = (TextView) findViewById(R.id.txtLogin);
        txtSignUp = (TextView) findViewById(R.id.txtSignUpLogin);
        txtForgetPasswordLogin = (TextView) findViewById(R.id.txtForgetPasswordLogin);
        etEmailLogin = (EditText) findViewById(R.id.etEmailLogin);
        etPasswordLogin = (EditText) findViewById(R.id.etPasswordLogin);
        etEmailLogin.setText("lalit.sharma@byteclues.com");
        etPasswordLogin.setText("123456");
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
                        saveUserData(userInfoData);
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("data", tripTransporterData);
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

    private void saveUserData(UserInfoData userInfoData) {
        Config.setLoginStatus(true);
        if (userInfoData.id != null) {
            Config.setUserId(userInfoData.id);
        }
        if (userInfoData.username != null) {
            Config.setUserName(userInfoData.username);
        }
        if (userInfoData.name != null) {
            Config.setUserFirstName(userInfoData.name);
        }
        if (userInfoData.l_name != null) {
            Config.setUserLastName(userInfoData.l_name);
        }
        if (userInfoData.role_id != null) {
            Config.setUserRoleId(userInfoData.role_id);
        }
        if (userInfoData.gender != null) {
            Config.setUserGender(userInfoData.gender);
        }
        if (userInfoData.addr1 != null) {
            String address = userInfoData.addr1;
            if (userInfoData.addr2 != null)
                address += " " + userInfoData.addr2;
            if (userInfoData.addr3 != null)
                address += " " + userInfoData.addr3;
            Config.setUserAddress(address);
        }
        if (userInfoData.photo != null) {
            Config.setUserImageURl(userInfoData.photo);
        }
        if (userInfoData.phone != null) {
            Config.setUserPhone(userInfoData.phone);
        }
        if (userInfoData.country_code != null) {
            Config.setUserCountryCode(userInfoData.country_code);
        }
        if (userInfoData.mobile != null) {
            Config.setUserMobile(userInfoData.mobile);
        }
        if (userInfoData.altr_mobile != null) {
            Config.setUserPinCode(userInfoData.altr_mobile);
        }
        if (userInfoData.bank_act_no != null) {
            Config.setUserAccountNumber(userInfoData.bank_act_no);
        }
        if (userInfoData.bank_act_name != null) {
            Config.setUserAccountName(userInfoData.bank_act_name);
        }
        if (userInfoData.bank_name != null) {
            Config.setUserBankName(userInfoData.bank_name);
        }
        if (userInfoData.bank_ifsc != null) {
            Config.setUserBankIFSC(userInfoData.bank_ifsc);
        }
        if (userInfoData.bank_swift_code != null) {
            Config.setUserBankSwiftCode(userInfoData.bank_swift_code);
        }
        if (userInfoData.wallet != null) {
            Config.setUserWallet(userInfoData.wallet);
        }
        if (userInfoData.passportno != null) {
            Config.setUserPassportNumber(userInfoData.passportno);
        }
        if (userInfoData.UserID != null) {
            Config.setUserModifyUserName(userInfoData.UserID);
        }
    }

    public boolean validation(String email, String password) {
        if (TextUtils.isEmpty(email)) {

            etEmailLogin.setError(getString(R.string.can_not_be_empty));
            etEmailLogin.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etPasswordLogin.setError(getString(R.string.can_not_be_empty));
            etPasswordLogin.requestFocus();
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
