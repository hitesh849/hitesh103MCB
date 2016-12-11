package com.app.mcb.viewControllers.sender;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.MatchingTripVPAdaptor;
import com.app.mcb.adapters.MyTripListVPAdapter;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.BookingRequestData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.model.MatchingTripModel;
import com.app.mcb.retrointerface.TryAgainInterface;
import com.app.mcb.viewControllers.dashboardFragments.DashBoardFragment;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh on 11-11-2016.
 */
public class MyTripInParcelSearchActivity extends AbstractFragmentActivity implements View.OnClickListener {


    private ViewPager vpMyList;
    private LinearLayout llMatchingTripMain;
    private RelativeLayout rlParcelMatchingSub;
    private LinearLayout llCountDotsMain;
    private MyTripListVPAdapter myTripListVPAdapter;
    private String parcelId;
    private AppHeaderView appHeaderView;
    private ParcelDetailsData parcelDetailsData;
    private MatchingTripModel matchingTripModel = new MatchingTripModel();

    private void init() {
        //setHeader(getResources().getString(R.string.my_trip));
        llMatchingTripMain = (LinearLayout) findViewById(R.id.llMatchingTripMain);
        rlParcelMatchingSub = (RelativeLayout) findViewById(R.id.rlParcelMatchingSub);
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        vpMyList = (ViewPager) findViewById(R.id.vpMyList);
        llCountDotsMain = (LinearLayout) findViewById(R.id.llCountDotsMain);
        viewPagerChangeListener();
        drawPageSelectionIndicators(0);
        parcelDetailsData = (ParcelDetailsData) getIntent().getSerializableExtra("data");
        vpMyList.setAdapter(new MatchingTripVPAdaptor(this, this, parcelDetailsData.tripsmatch));
    }

    private void viewPagerChangeListener() {
        vpMyList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.parcel_matching_trip_fragment);
        init();
    }

    private void addMainView() {
        rlParcelMatchingSub.removeAllViews();
        rlParcelMatchingSub.addView(vpMyList);
        rlParcelMatchingSub.addView(llCountDotsMain);
    }

    @Override
    protected BasicModel getModel() {
        return matchingTripModel;
    }

    private void getParcelDetails(final String parcelId) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            matchingTripModel.getParcelDetails(parcelId);
        } else {
            rlParcelMatchingSub.addView(Util.getViewInternetNotFound(this, rlParcelMatchingSub, new TryAgainInterface() {
                @Override
                public void callBack() {
                    addMainView();
                    getParcelDetails(parcelId);
                }
            }));

        }
    }

    @Override
    public void update(Observable observable, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof ParcelDetailsData) {
                ParcelDetailsData parcelDetailsData = (ParcelDetailsData) data;
                this.parcelDetailsData = parcelDetailsData.response.get(0);
                if ("success".equals(parcelDetailsData.status)) {
                    if (parcelDetailsData.tripsmatch != null)
                        vpMyList.setAdapter(new MatchingTripVPAdaptor(this, this, parcelDetailsData.tripsmatch));
                    if (parcelDetailsData.tripsmatch == null || parcelDetailsData.tripsmatch.size() == 0) {
                        rlParcelMatchingSub.addView(Util.getViewDataNotFound(this, rlParcelMatchingSub, getString(R.string.trip_unavailable)));
                    }
                }
            } else if (data != null && data instanceof BookingRequestData) {
                BookingRequestData commonResponseData = ((BookingRequestData) data);
                if ("success".equals(commonResponseData.status)) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else
                    rlParcelMatchingSub.addView(Util.getViewDataNotFound(this, rlParcelMatchingSub, commonResponseData.errorMessage));

            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llMatchingTripMain, getResources().getString(R.string.pls_try_again));
                rlParcelMatchingSub.addView(Util.getViewServerNotResponding(this, rlParcelMatchingSub, new TryAgainInterface() {
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgBookNowParcelMatchingTrip) {
            MyTripsData myTripsData = ((MyTripsData) view.getTag());
            bookingRequest(parcelDetailsData.id, myTripsData.id);
        }
    }

    private void bookingRequest(String parcel_id, final String trans_id) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                matchingTripModel.bookingRequest(parcel_id, trans_id);
            } else {
                rlParcelMatchingSub.addView(Util.getViewInternetNotFound(this, rlParcelMatchingSub, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addMainView();
                        bookingRequest(parcelId, trans_id);
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
