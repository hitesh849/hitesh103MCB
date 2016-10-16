package com.app.mcb.viewControllers.dashboardFragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.WithDrawVPAdapter;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.dao.WithDrawData;
import com.app.mcb.model.WithDrawModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by u on 9/15/2016.
 */
public class WithDrawFragment extends AbstractFragment implements View.OnClickListener {

    private RelativeLayout rlWithDrawMain;
    private TextView txtWithDrawStatus;
    private TextView txtNewUserWithDraw;
    private TextView txtBankNameWithDraw;
    private TextView txtAmountWithDraw;
    private TextView txtIFSCCodeWithDraw;
    private TextView txtStatusWithDraw;
    private TextView txtProcessingDateWithDraw;
    private EditText etAmountNewWithDraw;
    private EditText etAccountNumNewWithDraw;
    private EditText etReAccountNumNewWithDraw;
    private EditText etAccHolderNameWithDraw;
    private EditText etBankNameNewWithDraw;
    private EditText etIFSCNewWithDraw;
    private EditText etSwiftNumNewWithDraw;
    private TextView txtSubmitWithDraw;
    private UserInfoData userInfoData;
    private ViewPager vpWithDrawStatus;
    private LinearLayout llCountDotsMain;
    private WithDrawModel withDrawModel = new WithDrawModel();

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.withdraw, container, false);
        init(view);
        getUserDetails();
        return view;
    }

    private void init(View view) {
        rlWithDrawMain = (RelativeLayout) view.findViewById(R.id.rlWithDrawMain);
        txtWithDrawStatus = (TextView) view.findViewById(R.id.txtWithDrawStatus);
        txtNewUserWithDraw = (TextView) view.findViewById(R.id.txtNewUserWithDraw);
        txtWithDrawStatus.setOnClickListener(this);
        txtNewUserWithDraw.setOnClickListener(this);
        onClick(txtWithDrawStatus);
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.withdraw));
    }

    private void withDrawStatusInit(View view) {
        vpWithDrawStatus = (ViewPager) view.findViewById(R.id.vpWithDrawStatus);
        llCountDotsMain = (LinearLayout) view.findViewById(R.id.llCountDotsMain);
        viewPagerChangeListener();
        drawPageSelectionIndicators(0);
    }

    private void withDrawCreateNewInit(View view) {
        etAmountNewWithDraw = (EditText) view.findViewById(R.id.etAmountNewWithDraw);
        etAccountNumNewWithDraw = (EditText) view.findViewById(R.id.etAccountNumNewWithDraw);
        etReAccountNumNewWithDraw = (EditText) view.findViewById(R.id.etReAccountNumNewWithDraw);
        etAccHolderNameWithDraw = (EditText) view.findViewById(R.id.etAccHolderNameWithDraw);
        etBankNameNewWithDraw = (EditText) view.findViewById(R.id.etBanlNameNewWithDraw);
        etIFSCNewWithDraw = (EditText) view.findViewById(R.id.etIFSCNewWithDraw);
        etSwiftNumNewWithDraw = (EditText) view.findViewById(R.id.etSwiftNumNewWithDraw);
        txtSubmitWithDraw = (TextView) view.findViewById(R.id.txtSubmitWithDraw);
        txtSubmitWithDraw.setOnClickListener(this);
    }

    private void addViewInRelayout(int layout) {
        rlWithDrawMain.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(layout, null, false);
        rlWithDrawMain.addView(relativeLayout);
        if (layout == R.layout.withdraw_status) {
            withDrawStatusInit(rlWithDrawMain);
        } else {
            withDrawCreateNewInit(rlWithDrawMain);
            setMyWithdrawValue();
        }
    }

    @Override
    protected BasicModel getModel() {
        return withDrawModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof UserInfoData) {
                UserInfoData userInfoData = (UserInfoData) data;
                if ("success".equals(userInfoData.status)) {
                    if (userInfoData.response != null) {
                        this.userInfoData = userInfoData.response.get(0);
                    }
                } else if ("Error".equals(userInfoData.status)) {
                    Util.showOKSnakBar(rlWithDrawMain, userInfoData.errorMessage);
                }
            } else if (data != null && data instanceof WithDrawData) {
                WithDrawData withDrawData = (WithDrawData) data;
                if ("success".equals(withDrawData.status)) {
                    vpWithDrawStatus.setAdapter(new WithDrawVPAdapter(getActivity(), withDrawData.response));
                } else if ("Error".equals(userInfoData.status)) {
                    Util.showOKSnakBar(rlWithDrawMain, userInfoData.errorMessage);
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(rlWithDrawMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void getUserDetails() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                withDrawModel.getUserDetails();
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void withDrawStatusList() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                withDrawModel.withDrawStatusList();
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
        if (id == R.id.txtWithDrawStatus) {
            addViewInRelayout(R.layout.withdraw_status);
            unSelectTextView();
            selectLeftTextView();
            withDrawStatusList();
        } else if (id == R.id.txtNewUserWithDraw) {
            addViewInRelayout(R.layout.create_new_withdraw);
            unSelectTextView();
            selectRightTextView();
        } else if (id == R.id.txtSubmitWithDraw) {
            WithDrawData withDrawData = getValue();
            validation(withDrawData);
        }
    }

    private boolean validation(WithDrawData withDrawData) {
        if (TextUtils.isEmpty(withDrawData.amount)) {

            etAmountNewWithDraw.setError(getString(R.string.can_not_be_empty));
            etAmountNewWithDraw.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(withDrawData.acct_no)) {

            etAccountNumNewWithDraw.setError(getString(R.string.can_not_be_empty));
            etAccountNumNewWithDraw.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(withDrawData.re_acct_no)) {
            etReAccountNumNewWithDraw.setError(getString(R.string.can_not_be_empty));
            etReAccountNumNewWithDraw.requestFocus();
            return false;
        }
        if (!withDrawData.acct_no.equals(withDrawData.re_acct_no)) {
            etReAccountNumNewWithDraw.setError(getResources().getString(R.string.pass_do_not_match_error_msg));
            etReAccountNumNewWithDraw.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(withDrawData.act_name)) {
            etAccHolderNameWithDraw.setError(getString(R.string.can_not_be_empty));
            etAccHolderNameWithDraw.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(withDrawData.bank_name)) {
            etBankNameNewWithDraw.setError(getString(R.string.can_not_be_empty));
            etBankNameNewWithDraw.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(withDrawData.ifsc)) {
            etIFSCNewWithDraw.setError(getString(R.string.can_not_be_empty));
            etIFSCNewWithDraw.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(withDrawData.swift_code)) {
            etSwiftNumNewWithDraw.setError(getString(R.string.can_not_be_empty));
            etSwiftNumNewWithDraw.requestFocus();
            return false;
        }
        return true;
    }

    private void setMyWithdrawValue() {
        etAmountNewWithDraw.setText("0");
        etAccountNumNewWithDraw.setText(userInfoData.bank_act_no);
        etReAccountNumNewWithDraw.setText(userInfoData.bank_act_no);
        etAccHolderNameWithDraw.setText(userInfoData.bank_act_name);
        etBankNameNewWithDraw.setText(userInfoData.bank_name);
        etIFSCNewWithDraw.setText(userInfoData.bank_ifsc);
        etSwiftNumNewWithDraw.setText(userInfoData.bank_swift_code);
    }

    private WithDrawData getValue() {
        WithDrawData withDrawData = new WithDrawData();
        withDrawData.amount = etAmountNewWithDraw.getText().toString();
        withDrawData.acct_no = etAccountNumNewWithDraw.getText().toString();
        withDrawData.re_acct_no = etReAccountNumNewWithDraw.getText().toString();
        withDrawData.act_name = etAccHolderNameWithDraw.getText().toString();
        withDrawData.bank_name = etBankNameNewWithDraw.getText().toString();
        withDrawData.ifsc = etIFSCNewWithDraw.getText().toString();
        withDrawData.swift_code = etSwiftNumNewWithDraw.getText().toString();
        return withDrawData;
    }

    private void unSelectTextView() {
        txtWithDrawStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        txtNewUserWithDraw.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            txtNewUserWithDraw.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rect_right_corners_pink_border_grey_bg));
            txtWithDrawStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rect_left_corners_pink_border_grey_bg));
        }
    }

    private void selectLeftTextView() {
        txtWithDrawStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            txtWithDrawStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rect_left_corners_pink_bg));
        }
    }

    private void selectRightTextView() {
        txtNewUserWithDraw.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            txtNewUserWithDraw.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rect_right_corners_pink_bg));
        }
    }

    private void viewPagerChangeListener() {
        vpWithDrawStatus.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                drawPageSelectionIndicators(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void drawPageSelectionIndicators(int mPosition) {
        if (llCountDotsMain != null) {
            llCountDotsMain.removeAllViews();
        }

        ImageView[] dots = new ImageView[3];

        if (mPosition > 2) {
            mPosition = (mPosition % 3);
        }
        for (int i = 0; i < 3; i++) {


            dots[i] = new ImageView(getActivity());
            if (i == mPosition)
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.item_selected));
            else
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.vp_item_unselected));


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(5, 0, 5, 0);
            llCountDotsMain.addView(dots[i], params);
        }
    }
}
