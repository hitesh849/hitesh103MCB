package com.app.mcb.viewControllers.transporter;

import android.content.DialogInterface;
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
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.BookingRequestData;
import com.app.mcb.dao.MyTripDetailsData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelBookingChangeStatusData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.model.MyTripParcelListModel;
import com.app.mcb.retrointerface.TryAgainInterface;

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
    private MyTripsData myTripsData;
    private int parcelCount;
    private AppHeaderView appHeaderView;
    private MyTripParcelListModel myTripParcelListModel = new MyTripParcelListModel();

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.my_trip_parcel_list);
        rlMyTripParcelListMain = (RelativeLayout) findViewById(R.id.rlMyTripParcelListMain);
        vpMyTripParcelList = (ViewPager) findViewById(R.id.vpMyTripParcelList);
        llCountDotsMain = (LinearLayout) findViewById(R.id.llCountDotsMain);
        appHeaderView = ((AppHeaderView) findViewById(R.id.appHeaderView));

        Bundle bundle = getIntent().getBundleExtra("KEY_BUNDLE");
        if (bundle != null) {
            myTripsData = (MyTripsData) bundle.getSerializable("KEY_DATA");
        }
        getMyTripDetails(myTripsData.id);
        viewPagerChangeListener();
        appHeaderView.txtHeaderNamecenter.setText((myTripsData.myClickOn.equals("Find") ? getString(R.string.matching_parcels) : getString(R.string.booked_parcels)));

    }

    private void addMainView() {
        rlMyTripParcelListMain.removeAllViews();
        rlMyTripParcelListMain.addView(vpMyTripParcelList);
        rlMyTripParcelListMain.addView(llCountDotsMain);
    }

    private void getMyTripDetails(final String tripId) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                myTripParcelListModel.getMyTripDetails(tripId);
            } else {
                rlMyTripParcelListMain.addView(Util.getViewInternetNotFound(this, rlMyTripParcelListMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addMainView();
                        getMyTripDetails(tripId);
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void bookingRequest(final String tripId, final String trans_id) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                myTripParcelListModel.bookingRequest(tripId, trans_id);
            } else {
                rlMyTripParcelListMain.addView(Util.getViewInternetNotFound(this, rlMyTripParcelListMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addMainView();
                        bookingRequest(tripId, trans_id);
                    }
                }));
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
                        parcelCount = myTripDetailsData.parcel.size();
                        if (parcelCount > 1)
                            drawPageSelectionIndicators(0);
                    } else if ("Find".equals(myTripsData.myClickOn) && myTripDetailsData.parcellist != null && myTripDetailsData.parcellist.size() > 0) {
                        vpMyTripParcelList.setAdapter(new MyTripDetailsVPAdapter(this, this, myTripDetailsData.parcellist));
                        parcelCount = myTripDetailsData.parcellist.size();
                        if (parcelCount > 1)
                            drawPageSelectionIndicators(0);
                    } else {
                        vpMyTripParcelList.setVisibility(View.GONE);
                        rlMyTripParcelListMain.addView(Util.getViewDataNotFound(this, rlMyTripParcelListMain, getString(R.string.parcel_unavailable)));
                    }
                }
            } else if (data != null && data instanceof BookingRequestData) {
                BookingRequestData commonResponseData = ((BookingRequestData) data);
                if ("success".equals(commonResponseData.status)) {
                    Util.showAlertDialog(null, commonResponseData.response);
                } else
                    Util.showAlertDialog(null, commonResponseData.errorMessage);

            } else if (data != null && data instanceof ParcelBookingChangeStatusData) {
                ParcelBookingChangeStatusData parcelBookingRejectedData = ((ParcelBookingChangeStatusData) data);
                if ("success".equals(parcelBookingRejectedData.status))
                    getMyTripDetails(myTripsData.id);
                else
                    Util.showAlertDialog(null, parcelBookingRejectedData.errorMessage);
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(rlMyTripParcelListMain, getResources().getString(R.string.pls_try_again));
                rlMyTripParcelListMain.addView(Util.getViewServerNotResponding(this, rlMyTripParcelListMain, new TryAgainInterface() {
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

            final ParcelDetailsData parcelDetailsData = ((ParcelDetailsData) view.getTag());
            if (Constants.ParcelBookedWithTR.equals(parcelDetailsData.status) || Constants.ParcelPaymentDue.equals(parcelDetailsData.status)) {
                Util.showAlertWithCancelDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        parcelRejectRequest(parcelDetailsData);
                    }
                }, getString(R.string.are_you_sure_delete_this_parcel));

            } else {
                float tripAvailable = Float.parseFloat(myTripsData.awailableweight);
                if (Integer.parseInt(parcelDetailsData.weight) > tripAvailable) {
                    float extendsWeight = Float.parseFloat(myTripsData.awailableweight) * 20 / 100;
                    tripAvailable += extendsWeight;
                    if (Integer.parseInt(parcelDetailsData.weight) > tripAvailable) {
                        Util.showAlertDialog(null, getString(R.string.parcel_weight_available_capacity));
                    } else {
                        bookingRequest(parcelDetailsData.id, myTripsData.id);
                    }
                } else {
                    bookingRequest(parcelDetailsData.id, myTripsData.id);
                }
            }

        } else if (id == R.id.txtCollectedMyTripParcel) {
            ParcelDetailsData parcelDetailsData = ((ParcelDetailsData) view.getTag());

            usrUpdateTripStatus(parcelDetailsData, Constants.ParcelCollected, "Parcel Collected");
        } else if (id == R.id.txtDeliveredMyTripParcel) {
            ParcelDetailsData parcelDetailsData = ((ParcelDetailsData) view.getTag());
            usrUpdateTripStatus(parcelDetailsData, "6", "Parcel Delivered");
        }

    }
}
