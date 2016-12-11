package com.app.mcb.viewControllers.sender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.AddTripData;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.filters.TransporterFilter;
import com.app.mcb.model.MatchingTripModel;
import com.app.mcb.retrointerface.TryAgainInterface;
import com.app.mcb.sharedPreferences.Config;
import com.app.mcb.viewControllers.LoginActivity;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.ArrayList;
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
    private ParcelDetailsData parcelDetailsData;
    private LinearLayout llTranOrReceiverContainer;
    private TextView txtReceiverIdParcelDetails;
    private TextView txtReceiverMobileParcelDetails;
    private TextView txtReceiverNameParcelDetails;
    private TextView txtReceiverEmailParcelDetails;
    private LinearLayout llFindParcelsMyParcel;
    private LinearLayout llParcelDetailsMain;
    private ImageView imgSettingParcelDetailsSearch;
    private LinearLayout llParcelSearchMain;
    private LinearLayout llParcelSearchSub;
    private TextView txtAddTripParcel;
    private MyTripsData myTripsData;
    PopupMenu popup;
    private MatchingTripModel matchingTripModel = new MatchingTripModel();

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.parcel_details_parcel_search);
        init();
        parcelDetailsData = (ParcelDetailsData) getIntent().getSerializableExtra("data");
        getParcelDetails(parcelDetailsData.id);
    }

    private void addMainView() {
        llParcelSearchMain.removeAllViews();
        llParcelSearchMain.addView(llParcelSearchSub);
    }

    private void setValues(ParcelDetailsData parcelDetailsData) {
        txtFromShortParcelDetails.setText(Util.getFirstName(parcelDetailsData.source));
        txtToShortParcelDetails.setText(Util.getFirstName(parcelDetailsData.destination));
        txtFromLongParcelDetails.setText(parcelDetailsData.source);
        txtToLongParcelDetails.setText(parcelDetailsData.destination);
        txtFromDateParcelDetails.setText(Util.getDateFromDateTimeFormat(parcelDetailsData.created));
        txtToDateParcelDetails.setText(Util.getDDMMYYYYFormat(parcelDetailsData.till_date, "yyyy-MM-dd"));
        txtParcelIdParcelDetails.setText(parcelDetailsData.ParcelID);
        txtParcelWeightParcelDetails.setText(parcelDetailsData.weight + " " + "KG");
        txtParcelTypeParcelDetails.setText(Util.getParcelType(parcelDetailsData.type));
    }

    private void init() {
        llParcelDetailsMain = (LinearLayout) findViewById(R.id.llParcelDetailsMain);
        llParcelSearchMain = (LinearLayout) findViewById(R.id.llParcelSearchMain);
        llParcelSearchSub = (LinearLayout) findViewById(R.id.llParcelSearchSub);
        txtFromShortParcelDetails = (TextView) findViewById(R.id.txtFromShortParcelDetails);
        txtToShortParcelDetails = (TextView) findViewById(R.id.txtToShortParcelDetails);
        txtFromLongParcelDetails = (TextView) findViewById(R.id.txtFromLongParcelDetails);
        txtToLongParcelDetails = (TextView) findViewById(R.id.txtToLongParcelDetails);
        txtFromDateParcelDetails = (TextView) findViewById(R.id.txtFromDateParcelDetails);
        txtToDateParcelDetails = (TextView) findViewById(R.id.txtToDateParcelDetails);
        txtParcelIdParcelDetails = (TextView) findViewById(R.id.txtParcelIdParcelDetails);
        txtParcelWeightParcelDetails = (TextView) findViewById(R.id.txtParcelWeightParcelDetails);
        txtParcelTypeParcelDetails = (TextView) findViewById(R.id.txtParcelTypeParcelDetails);
        llTranOrReceiverContainer = (LinearLayout) findViewById(R.id.llTranOrReceiverContainer);
        imgSettingParcelDetailsSearch = (ImageView) findViewById(R.id.imgSettingParcelDetailsSearch);
        txtAddTripParcel = (TextView) findViewById(R.id.txtAddTripParcel);
        imgSettingParcelDetailsSearch.setOnClickListener(this);
        txtAddTripParcel.setOnClickListener(this);
        TransporterFilter.addFilterView(this, llParcelDetailsMain, this);
        popup = new PopupMenu(this, imgSettingParcelDetailsSearch);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.parcel_add_my_trip, popup.getMenu());
    }

    private void getParcelDetails(final String parcelId) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            matchingTripModel.getParcelDetails(parcelId);
        } else {
            llParcelSearchMain.addView(Util.getViewInternetNotFound(this, llParcelSearchMain, new TryAgainInterface() {
                @Override
                public void callBack() {
                    addMainView();
                    getParcelDetails(parcelId);
                }
            }));
        }
    }

    private void getMyTripDetails(final String tripId) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            matchingTripModel.getMyTripDetails(tripId);
        } else {
            llParcelSearchMain.addView(Util.getViewInternetNotFound(this, llParcelSearchMain, new TryAgainInterface() {
                @Override
                public void callBack() {
                    addMainView();
                    getMyTripDetails(tripId);
                }
            }));
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

                    if (parcelDetailsDataMain.tripsmatch != null) {
                        parcelDetailsData.tripsmatch = parcelDetailsDataMain.tripsmatch;
                    }
                    setValues(parcelDetailsData);
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showSnakBar(llParcelDetailsMain, getResources().getString(R.string.pls_try_again));
                llParcelSearchMain.addView(Util.getViewServerNotResponding(this, llParcelSearchMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        startActivity(getIntent());
                        finish();
                    }
                }));
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
        if (id == R.id.llFindParcelsMyParcel) {

        } else if (id == R.id.imgSettingParcelDetailsSearch) {

            popup.show();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.action_my_trip:
                            if (Config.getLoginStatus()) {
                                sendToScreen();
                            } else {
                                Intent intent = new Intent(ParcelDetailsSenderSearchActivity.this, LoginActivity.class);
                                startActivityForResult(intent, 502);
                            }
                            break;
                    }
                    return false;
                }
            });
        } else if (id == R.id.txtAddTripParcel) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 503);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (502): {
                if (resultCode == Activity.RESULT_OK) {
                    sendToScreen();
                }
                break;
            }

            case (503): {
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(Constants.ADD_TRIP_KEY, new AddTripData());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            }
        }
    }

    private void sendToScreen() {
        ArrayList<MyTripsData> tripsmatch = new ArrayList<MyTripsData>();
        for (MyTripsData myTripsData : parcelDetailsData.tripsmatch) {
            if (myTripsData.t_id.equalsIgnoreCase(Config.getUserId())) {
                tripsmatch.add(myTripsData);
            }
        }
        parcelDetailsData.tripsmatch = tripsmatch;
        if (parcelDetailsData.tripsmatch.size() > 0) {
            Intent intent = new Intent(ParcelDetailsSenderSearchActivity.this, MyTripInParcelSearchActivity.class);
            intent.putExtra("data", parcelDetailsData);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("KEY_PARCEL_DETAIL", parcelDetailsData);
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


    @Override
    public void filterData(FilterData filterData) {

        Intent intent = new Intent();
        intent.putExtra("FILTER_DATA", filterData);
        setResult(RESULT_OK, intent);
        finish();
    }
}