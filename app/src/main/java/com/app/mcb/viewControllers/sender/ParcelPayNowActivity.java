package com.app.mcb.viewControllers.sender;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.GenerateOrderData;
import com.app.mcb.dao.MyTripDetailsData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.TripData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.model.PayNowModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by u on 11/3/2016.
 */
public class ParcelPayNowActivity extends AbstractFragmentActivity implements View.OnClickListener, CommonListener {

    private ViewPager vpParcelList;
    private TextView txtFromShortParcelDetails;
    private TextView txtToShortParcelDetails;
    private TextView txtFromLongParcelDetails;
    private TextView txtToLongParcelDetails;
    private TextView txtFromDateParcelDetails;
    private TextView txtToDateParcelDetails;
    private TextView txtParcelIdParcelDetails;
    private TextView txtParcelWeightParcelDetails;
    private TextView txtParcelTypeParcelDetails;
    private TextView txtSelectorTransDetails;
    private TextView txtSelectorReceiverDetails;
    private ParcelDetailsData parcelDetailsData;
    private UserInfoData userInfoData;
    private LinearLayout llTranOrReceiverContainer;
    private TextView txtReceiverIdParcelDetails;
    private TextView txtReceiverMobileParcelDetails;
    private TextView txtReceiverNameParcelDetails;
    private TextView txtReceiverEmailParcelDetails;
    private TextView txtTransporterNameParcelDetails;
    private TextView txtTransporterEmailParcelDetails;
    private TextView txtFlightNumberParcelDetails;
    private TextView txtDepartureDateParcelDetails;
    private TextView txtDepartureTimeParcelDetails;
    private TextView txtArrivalDateParcelDetails;
    private TextView txtArrivalTimeParcelDetails;
    private LinearLayout llFindParcelsMyParcel;
    private LinearLayout llParcelDetailsMain;
    private TextView txtPayNowParcelPayment;
    private TextView txtConfirmParcelPayment;
    private CheckBox chkUseWallet;
    private TripData myTripsData;
    private boolean isWalletUse;
    private PayNowModel payNowModel = new PayNowModel();


    @Override
    protected void onCreatePost(Bundle savedInstanceState) {

        setContentView(R.layout.parcel_paynow_activity);
        init();
        getParcelDetails(((String) getIntent().getSerializableExtra("parcelId")));
    }


    private void setValues(ParcelDetailsData parcelDetailsData) {
        txtFromShortParcelDetails.setText(Util.getFirstName(parcelDetailsData.source));
        txtToShortParcelDetails.setText(Util.getFirstName(parcelDetailsData.destination));
        txtFromLongParcelDetails.setText(parcelDetailsData.source);
        txtToLongParcelDetails.setText(parcelDetailsData.destination);
        txtFromDateParcelDetails.setText(Util.getDateFromDateTimeFormat(parcelDetailsData.created));
        txtToDateParcelDetails.setText(Util.getDDMMYYYYFormat(parcelDetailsData.till_date, "yyyy-MM-dd"));
        txtParcelIdParcelDetails.setText(parcelDetailsData.ParcelID);
        txtParcelWeightParcelDetails.setText(parcelDetailsData.weight);
        txtParcelTypeParcelDetails.setText(Util.getParcelType(parcelDetailsData.type));
    }

    private void init() {

        llParcelDetailsMain = (LinearLayout) findViewById(R.id.llParcelDetailsMain);
        txtFromShortParcelDetails = (TextView) findViewById(R.id.txtFromShortParcelDetails);
        txtToShortParcelDetails = (TextView) findViewById(R.id.txtToShortParcelDetails);
        txtFromLongParcelDetails = (TextView) findViewById(R.id.txtFromLongParcelDetails);
        txtToLongParcelDetails = (TextView) findViewById(R.id.txtToLongParcelDetails);
        txtFromDateParcelDetails = (TextView) findViewById(R.id.txtFromDateParcelDetails);
        txtToDateParcelDetails = (TextView) findViewById(R.id.txtToDateParcelDetails);
        txtParcelIdParcelDetails = (TextView) findViewById(R.id.txtParcelIdParcelDetails);
        txtParcelWeightParcelDetails = (TextView) findViewById(R.id.txtParcelWeightParcelDetails);
        txtParcelTypeParcelDetails = (TextView) findViewById(R.id.txtParcelTypeParcelDetails);
        txtSelectorTransDetails = (TextView) findViewById(R.id.txtSelectorTransDetails);
        txtSelectorReceiverDetails = (TextView) findViewById(R.id.txtSelectorReceiverDetails);
        txtPayNowParcelPayment = (TextView) findViewById(R.id.txtPayNowParcelPayment);
        txtConfirmParcelPayment = (TextView) findViewById(R.id.txtConfirmParcelPayment);
        llTranOrReceiverContainer = (LinearLayout) findViewById(R.id.llTranOrReceiverContainer);
        chkUseWallet = (CheckBox) findViewById(R.id.chkUseWallet);
        txtSelectorTransDetails.setOnClickListener(this);
        txtSelectorReceiverDetails.setOnClickListener(this);
        txtPayNowParcelPayment.setOnClickListener(this);
        txtConfirmParcelPayment.setOnClickListener(this);
        chkUseWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isWalletUse = isChecked;
            }
        });
    }


    private void getParcelDetails(String parcelId) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            payNowModel.getMyTripDetails(parcelId);
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
        }
    }

    private void getUserDetails(String parcelId) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            payNowModel.getUserDetails(parcelId);
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
        }
    }

    private void generateOrder() {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            payNowModel.generateOrder(parcelDetailsData, myTripsData, userInfoData, isWalletUse);
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
        }
    }


    @Override
    protected BasicModel getModel() {
        return payNowModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof MyTripDetailsData) {
                MyTripDetailsData parcelDetailsDataMain = (MyTripDetailsData) data;
                if ("success".equals(parcelDetailsDataMain.status)) {
                    parcelDetailsData = parcelDetailsDataMain.parcellist.get(0);
                    if (parcelDetailsDataMain.parcellist != null)
                        myTripsData = parcelDetailsDataMain.response.get(0);
                    addTransPorterView();
                    setValues(parcelDetailsData);
                    getUserDetails(parcelDetailsData.recv_id);

                }
            } else if (data != null && data instanceof UserInfoData) {
                UserInfoData userInfoData = (UserInfoData) data;
                if ("success".equals(userInfoData.status)) {
                    this.userInfoData = userInfoData.response.get(0);
                }
            } else if (data != null && data instanceof GenerateOrderData) {
                GenerateOrderData generateOrderData = ((GenerateOrderData) data).response.get(0);
                if ("success".equals(generateOrderData.status)) {

                } else if ("successpayment".equals(generateOrderData.status)) {

                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llParcelDetailsMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.txtSelectorTransDetails) {
            txtSelectorTransDetails.setTextColor(getResources().getColor(R.color.white));
            txtSelectorReceiverDetails.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtSelectorTransDetails.setBackgroundResource(R.drawable.rect_left_corners_pink_bg);
            txtSelectorReceiverDetails.setBackgroundResource(R.drawable.rect_right_corners_pink_border_grey_bg);
            addTransPorterView();

        } else if (id == R.id.txtSelectorReceiverDetails) {
            txtSelectorTransDetails.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtSelectorReceiverDetails.setTextColor(getResources().getColor(R.color.white));
            txtSelectorTransDetails.setBackgroundResource(R.drawable.rect_left_corners_pink_border_grey_bg);
            txtSelectorReceiverDetails.setBackgroundResource(R.drawable.rect_right_corners_pink_bg);
            initReceiverInfoParcelDetails(addViewInRelayout(R.layout.receiver_info_parcel_details));
        } else if (id == R.id.llFindParcelsMyParcel) {
            Intent intent = new Intent(this, MatchingTripListActivity.class);
            intent.putExtra("data", parcelDetailsData);
            startActivity(intent);
        } else if (id == R.id.txtPayNowParcelPayment) {
            generateOrder();
        } else if (id == R.id.txtConfirmParcelPayment) {

        }
    }

    private LinearLayout addViewInRelayout(int layout) {
        llTranOrReceiverContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout llLayout = (LinearLayout) inflater.inflate(layout, null, false);
        llTranOrReceiverContainer.addView(llLayout);
        return llTranOrReceiverContainer;
    }

    private void initReceiverInfoParcelDetails(View view) {
        txtReceiverIdParcelDetails = (TextView) view.findViewById(R.id.txtReceiverIdParcelDetails);
        txtReceiverMobileParcelDetails = (TextView) view.findViewById(R.id.txtReceiverMobileParcelDetails);
        txtReceiverNameParcelDetails = (TextView) view.findViewById(R.id.txtReceiverNameParcelDetails);
        txtReceiverEmailParcelDetails = (TextView) view.findViewById(R.id.txtReceiverEmailParcelDetails);
        if (userInfoData != null) {
            txtReceiverIdParcelDetails.setText(userInfoData.UserID);
            txtReceiverMobileParcelDetails.setText(userInfoData.mobile);
            txtReceiverNameParcelDetails.setText(userInfoData.name);
            txtReceiverEmailParcelDetails.setText(userInfoData.email);
        }

    }

    private void addTransPorterView() {

        initTransporterInfoParcelDetails(addViewInRelayout(R.layout.transporter_info_parcel_details));

    }

    private void initFindMatchingParcel(View view) {
        llFindParcelsMyParcel = (LinearLayout) view.findViewById(R.id.llFindParcelsMyParcel);
        llFindParcelsMyParcel.setOnClickListener(this);
    }

    private void initTransporterInfoParcelDetails(View view) {
        txtTransporterNameParcelDetails = (TextView) view.findViewById(R.id.txtTransporterNameParcelDetails);
        txtTransporterEmailParcelDetails = (TextView) view.findViewById(R.id.txtTransporterEmailParcelDetails);
        txtFlightNumberParcelDetails = (TextView) view.findViewById(R.id.txtFlightNumberParcelDetails);
        txtDepartureDateParcelDetails = (TextView) view.findViewById(R.id.txtDepartureDateParcelDetails);
        txtDepartureTimeParcelDetails = (TextView) view.findViewById(R.id.txtDepartureTimeParcelDetails);
        txtArrivalDateParcelDetails = (TextView) view.findViewById(R.id.txtArrivalDateParcelDetails);
        txtArrivalTimeParcelDetails = (TextView) view.findViewById(R.id.txtArrivalTimeParcelDetails);
        // txtTransporterNameParcelDetails.setText(myTripsData.transportername);
        //txtTransporterEmailParcelDetails.setText(myTripsData.transporteremail);
        txtFlightNumberParcelDetails.setText(myTripsData.flight_no);
        txtDepartureDateParcelDetails.setText(Util.getDDMMYYYYFormat(myTripsData.dep_time, "yyyy-MM-dd HH:mm:ss"));
        txtArrivalDateParcelDetails.setText(Util.getDDMMYYYYFormat(myTripsData.arrival_time, "yyyy-MM-dd HH:mm:ss"));
        txtDepartureTimeParcelDetails.setText(Util.getTimeFromDateTimeFormat(myTripsData.dep_time));
        txtArrivalTimeParcelDetails.setText(Util.getTimeFromDateTimeFormat(myTripsData.arrival_time));

    }

    @Override
    public void filterData(FilterData filterData) {

    }
}