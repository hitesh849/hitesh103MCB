package com.app.mcb.viewControllers.dashboardFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.model.UserProfileModel;
import com.app.mcb.sharedPreferences.Config;
import com.app.mcb.viewControllers.LoginActivity;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by u on 9/15/2016.
 */
public class MyProfileFragment extends AbstractFragment implements View.OnClickListener {

    private EditText etNameMyProfile;
    private EditText etMemberIdMyProfile;
    private EditText etEmailIdMyProfile;
    private EditText etMobileNumberMyProfile;
    private EditText etLandLineMyProfile;
    private EditText etCardNumberMyProfile;
    private EditText etAddressMyProfile;
    private LinearLayout llEditProfile;
    private LinearLayout llMyProfileMain;
    private TextView txtEditProfile;
    private UserProfileModel userProfileModel = new UserProfileModel();

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_profile, container, false);
        init(view);
        setValues();
        return view;
    }


    private void init(View view) {
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.my_profile));
        llMyProfileMain = (LinearLayout) view.findViewById(R.id.llMyProfileMain);
        txtEditProfile = (TextView) view.findViewById(R.id.txtEditProfile);
        etNameMyProfile = (EditText) view.findViewById(R.id.etNameMyProfile);
        etMemberIdMyProfile = (EditText) view.findViewById(R.id.etMemberIdMyProfile);
        etEmailIdMyProfile = (EditText) view.findViewById(R.id.etEmailIdMyProfile);
        etMobileNumberMyProfile = (EditText) view.findViewById(R.id.etMobileNumberMyProfile);
        etLandLineMyProfile = (EditText) view.findViewById(R.id.etLandLineMyProfile);
        etCardNumberMyProfile = (EditText) view.findViewById(R.id.etCardNumberMyProfile);
        etAddressMyProfile = (EditText) view.findViewById(R.id.etAddressMyProfile);
        txtEditProfile.setOnClickListener(this);
    }

    private void setValues() {
        etNameMyProfile.setText(Config.getUserFirstName() + " " + Config.getUserLastName());
        etMemberIdMyProfile.setText(Config.getUserId());
        etEmailIdMyProfile.setText(Config.getUserName());
        etMobileNumberMyProfile.setText(Config.getUserMobile());
        etLandLineMyProfile.setText(Config.getUserPhone());
        etCardNumberMyProfile.setText(Config.getUserPassportNumber());
        etAddressMyProfile.setText(Config.getUserAddress());

        etNameMyProfile.setFocusable(false);
        etMemberIdMyProfile.setFocusable(false);
        etMobileNumberMyProfile.setFocusable(false);
        etLandLineMyProfile.setFocusable(false);
        etCardNumberMyProfile.setFocusable(false);
        etAddressMyProfile.setFocusable(false);
        etEmailIdMyProfile.setFocusable(false);

    }

    private void setFocus() {
        etNameMyProfile.setFocusableInTouchMode(true);
        etNameMyProfile.setFocusable(true);
        etMobileNumberMyProfile.setFocusableInTouchMode(true);
        etMobileNumberMyProfile.setFocusable(true);
        etLandLineMyProfile.setFocusableInTouchMode(true);
        etLandLineMyProfile.setFocusable(true);
        etCardNumberMyProfile.setFocusableInTouchMode(true);
        etCardNumberMyProfile.setFocusable(true);
        etAddressMyProfile.setFocusableInTouchMode(true);
        etAddressMyProfile.setFocusable(true);

    }

    private UserInfoData getValue() {
        UserInfoData userInfoData = new UserInfoData();
        userInfoData.name = etNameMyProfile.getText().toString();
        userInfoData.id = etMemberIdMyProfile.getText().toString();
        userInfoData.email = etEmailIdMyProfile.getText().toString();
        userInfoData.mobile = etMobileNumberMyProfile.getText().toString();
        userInfoData.phone = etLandLineMyProfile.getText().toString();
        userInfoData.passportno = etCardNumberMyProfile.getText().toString();
        userInfoData.address = etAddressMyProfile.getText().toString();
        return userInfoData;
    }

    private boolean validation(UserInfoData userInfoData) {

        if (TextUtils.isEmpty(userInfoData.name)) {
            etNameMyProfile.setError(getResources().getString(R.string.can_not_be_empty));
            etNameMyProfile.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.mobile)) {
            etMobileNumberMyProfile.setError(getResources().getString(R.string.can_not_be_empty));
            etMobileNumberMyProfile.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(userInfoData.phone)) {
            etLandLineMyProfile.setError(getResources().getString(R.string.can_not_be_empty));
            etLandLineMyProfile.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.passportno)) {
            etCardNumberMyProfile.setError(getResources().getString(R.string.can_not_be_empty));
            etCardNumberMyProfile.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.address)) {
            etAddressMyProfile.setError(getResources().getString(R.string.can_not_be_empty));
            etAddressMyProfile.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    protected BasicModel getModel() {
        return userProfileModel;
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
                        LoginActivity.saveUserData(userInfoData);
                        Util.replaceFragment(getActivity(), R.id.fmHomeContainer, new MyProfileFragment());
                    }
                } else if (userInfoData.status.equals("Error")) {
                    Util.showOKSnakBar(llMyProfileMain, userInfoData.errorMessage);
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llMyProfileMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.txtEditProfile) {
            if (getResources().getString(R.string.edit).equals(txtEditProfile.getText())) {
                txtEditProfile.setText(getResources().getString(R.string.update));
                setFocus();
            } else if (getResources().getString(R.string.update).equals(txtEditProfile.getText())) {
                txtEditProfile.setText(getResources().getString(R.string.edit));
                UserInfoData userInfoData = getValue();
                if (validation(userInfoData)) {
                    updateProfile(userInfoData);
                }
            }
        }
    }

    private void updateProfile(UserInfoData userInfoData) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                userProfileModel.updateProfile(userInfoData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
