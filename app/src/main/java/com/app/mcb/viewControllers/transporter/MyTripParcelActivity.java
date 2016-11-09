package com.app.mcb.viewControllers.transporter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.MyTripDetailsVPAdapter;
import com.app.mcb.dao.BookingRequestData;
import com.app.mcb.dao.MyTripDetailsData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelBookingChangeStatusData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.model.MyTripParcelListModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.ArrayList;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh on 28-10-2016.
 */
public class MyTripParcelActivity extends AbstractFragmentActivity implements View.OnClickListener {

    private ViewPager vpMyTripParcelList;
    private LinearLayout llCountDotsMain;
    private RelativeLayout rlMyTripParcelListMain;
    MyTripsData myTripsData;
    private MyTripParcelListModel myTripParcelListModel = new MyTripParcelListModel();

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.my_trip_parcel_list);
        rlMyTripParcelListMain = (RelativeLayout) findViewById(R.id.rlMyTripParcelListMain);
        vpMyTripParcelList = (ViewPager) findViewById(R.id.vpMyTripParcelList);
        llCountDotsMain = (LinearLayout) findViewById(R.id.llCountDotsMain);
        Bundle bundle = getIntent().getBundleExtra("KEY_BUNDLE");
        if (bundle != null) {
            myTripsData = (MyTripsData) bundle.getSerializable("KEY_DATA");
        }
        getMyTripDetails(myTripsData.id);
        viewPagerChangeListener();
        drawPageSelectionIndicators(0);
    }

    private void getMyTripDetails(String tripId) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                myTripParcelListModel.getMyTripDetails(tripId);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void bookingRequest(String tripId, String trans_id) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                myTripParcelListModel.bookingRequest(tripId, trans_id);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void parcelRejectRequest(ParcelDetailsData parcelDetailsData) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                myTripParcelListModel.parcelRejectRequest(parcelDetailsData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void usrUpdateTripStatus(ParcelDetailsData parcelDetailsData, String status, String msg) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                myTripParcelListModel.usrUpdateTripStatus(parcelDetailsData, status, msg);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void viewPagerChangeListener() {
        vpMyTripParcelList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        llCountDotsMain = (LinearLayout) findViewById(R.id.llCountDotsMain);
        ImageView[] dots = new ImageView[3];

        if (mPosition > 2) {
            mPosition = (mPosition % 3);
        }
        for (int i = 0; i < 3; i++) {
            dots[i] = new ImageView(this);
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

    @Override
    protected BasicModel getModel() {
        return myTripParcelListModel;
    }

    @Override
    public void update(Observable observable, Object data) {

        Util.dimissProDialog();
        try {
            if (data != null && data instanceof MyTripDetailsData) {
                MyTripDetailsData myTripDetailsData = (MyTripDetailsData) data;
                if (myTripDetailsData.status.equals("success")) {
                    if ("Booked".equals(myTripsData.myClickOn) && myTripDetailsData.parcel != null && myTripDetailsData.parcel.size() > 0) {
                        vpMyTripParcelList.setAdapter(new MyTripDetailsVPAdapter(this, this, myTripDetailsData.parcel));

                    } else if ("Find".equals(myTripsData.myClickOn) && myTripDetailsData.parcellist != null && myTripDetailsData.parcellist.size() > 0) {
                        vpMyTripParcelList.setAdapter(new MyTripDetailsVPAdapter(this, this, myTripDetailsData.parcellist));
                    } else {
                        vpMyTripParcelList.setVisibility(View.GONE);
                        Util.showOKSnakBar(rlMyTripParcelListMain, getResources().getString(R.string.parcel_unavailable));
                    }
                }
            } else if (data != null && data instanceof BookingRequestData) {
                BookingRequestData commonResponseData = ((BookingRequestData) data);
                if ("success".equals(commonResponseData.status)) {
                    Util.showOKSnakBar(rlMyTripParcelListMain, commonResponseData.response);
                } else
                    Util.showOKSnakBar(rlMyTripParcelListMain, commonResponseData.errorMessage);

            } else if (data != null && data instanceof ParcelBookingChangeStatusData) {
                ParcelBookingChangeStatusData parcelBookingRejectedData = ((ParcelBookingChangeStatusData) data);
                if ("success".equals(parcelBookingRejectedData.status))
                    getMyTripDetails(myTripsData.id);
                else
                    Util.showOKSnakBar(rlMyTripParcelListMain, parcelBookingRejectedData.errorMessage);
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(rlMyTripParcelListMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private ArrayList<ParcelDetailsData> filterListWithStatus(String status, ArrayList<ParcelDetailsData> myTripParcel) {
        ArrayList<ParcelDetailsData> tempList = new ArrayList<ParcelDetailsData>();
        for (ParcelDetailsData parcelDetailsData : myTripParcel) {

            if (Integer.parseInt(status) == Integer.parseInt(parcelDetailsData.status)) {
                tempList.add(parcelDetailsData);
            }
        }
        return tempList;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgBookNowMyTripParcel) {

            ParcelDetailsData parcelDetailsData = ((ParcelDetailsData) view.getTag());
            if (Constants.ParcelBookedWithTR.equals(parcelDetailsData.status) || Constants.ParcelPaymentDue.equals(parcelDetailsData.status))
                parcelRejectRequest(parcelDetailsData);
            else
                bookingRequest(parcelDetailsData.id, myTripsData.id);
        } else if (id == R.id.txtCollectedMyTripParcel) {
            ParcelDetailsData parcelDetailsData = ((ParcelDetailsData) view.getTag());

            usrUpdateTripStatus(parcelDetailsData, Constants.ParcelCollected, "Parcel Collected");
        } else if (id == R.id.txtDeliveredMyTripParcel) {
            ParcelDetailsData parcelDetailsData = ((ParcelDetailsData) view.getTag());
            usrUpdateTripStatus(parcelDetailsData, "6", "Parcel Delivered");
        }

    }
}
