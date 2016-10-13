package com.app.mcb.viewControllers.dashboardFragments;

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
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.sharedPreferences.Config;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

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
    private TextView txtEditProfile;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_profile, container, false);
        init(view);
        setValues();
        return view;
    }


    private void init(View view) {
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.my_profile));
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
        userInfoData.mobile = etMobileNumberMyProfile.getText().toString();
        userInfoData.phone = etLandLineMyProfile.getText().toString();
        userInfoData.passportno = etCardNumberMyProfile.getText().toString();
        userInfoData.address = etAddressMyProfile.getText().toString();
        return userInfoData;
    }

    private boolean validation(UserInfoData userInfoData) {

        if (TextUtils.isEmpty(userInfoData.name)) {
            etNameMyProfile.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.mobile)) {
            etMobileNumberMyProfile.setError("Can't be Empty");
            return false;
        }

        if (TextUtils.isEmpty(userInfoData.phone)) {
            etLandLineMyProfile.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.passportno)) {
            etCardNumberMyProfile.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.address)) {
            etAddressMyProfile.setError("Can't be Empty");
            return false;
        }

        return true;
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {

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
                if (validation(getValue())) {

                }
            }

        }
    }
}
