package com.app.mcb.viewControllers.sender;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.filters.TransporterFilter;
import com.app.mcb.model.MatchingTripModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh on 10-11-2016.
 */
public class ParcelDetailsSenderSearchActivity extends AbstractFragmentActivity implements View.OnClickListener, CommonListener {

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
    private MyTripsData myTripsData;
    private MatchingTripModel matchingTripModel = new MatchingTripModel();

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.parcel_details);
        init();
        parcelDetailsData = (ParcelDetailsData) getIntent().getSerializableExtra("data");
        getParcelDetails(parcelDetailsData.id);
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
        llTranOrReceiverContainer = (LinearLayout) findViewById(R.id.llTranOrReceiverContainer);
        txtSelectorTransDetails.setOnClickListener(this);
        txtSelectorReceiverDetails.setOnClickListener(this);
    }


    private void getParcelDetails(String parcelId) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            matchingTripModel.getParcelDetails(parcelId);
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
        }
    }

    private void getMyTripDetails(String tripId) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            matchingTripModel.getMyTripDetails(tripId);
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
        }
    }


    @Override
    protected BasicModel getModel() {
        return matchingTripModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof ParcelDetailsData) {
                ParcelDetailsData parcelDetailsDataMain = (ParcelDetailsData) data;
                if ("success".equals(parcelDetailsDataMain.status)) {
                    parcelDetailsData = parcelDetailsDataMain.response.get(0);
                    if (parcelDetailsDataMain.trip != null)
                        myTripsData = parcelDetailsDataMain.trip.get(0);

                    if (Constants.ParcelPaymentDue.equals(parcelDetailsData.status))
                        getMyTripDetails(parcelDetailsData.trans_id);
                    addTransPorterView();
                    setValues(parcelDetailsData);
                }
            } else if (data != null && data instanceof MyTripsData) {
                MyTripsData myTripsData = ((MyTripsData) data);
                this.myTripsData = myTripsData.response.get(0);
                getUserDetails(this.myTripsData.processed_by);
                addTransPorterView();
            } else if (data != null && data instanceof UserInfoData) {
                UserInfoData userInfoData = (UserInfoData) data;
                if ("success".equals(userInfoData.status)) {
                    UserInfoData obj = userInfoData.response.get(0);
                    myTripsData.transporteremail = obj.username;
                    String transName = (obj.l_name != null) ? obj.name + " " + obj.l_name : obj.name;
                    myTripsData.transportername = transName;
                    addTransPorterView();
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llParcelDetailsMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getUserDetails(String userId) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            matchingTripModel.getUserDetails(userId);
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
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
            Bundle bundle=new Bundle();
            bundle.putSerializable("data", parcelDetailsData);
            intent.putExtra("KEY_BUNDLE",bundle);
            startActivity(intent);
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
        txtReceiverIdParcelDetails.setText(parcelDetailsData.MCBreceiverID);
        txtReceiverMobileParcelDetails.setText(parcelDetailsData.recv_mobile);
        txtReceiverNameParcelDetails.setText(parcelDetailsData.recv_name);
        txtReceiverEmailParcelDetails.setText(parcelDetailsData.receiveremail);
    }

    private void addTransPorterView() {

        if (parcelDetailsData.status != null && (parcelDetailsData.status.equals(Constants.ParcelIdCreated) || parcelDetailsData.status.equals(Constants.ParcelRejectedByTr)))
            initFindMatchingParcel(addViewInRelayout(R.layout.no_transformer_found));
        else
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
        txtTransporterNameParcelDetails.setText(myTripsData.transportername);
        txtTransporterEmailParcelDetails.setText(myTripsData.transporteremail);
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