package com.app.mcb.viewControllers.sender;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.MatchingTripVPAdaptor;
import com.app.mcb.adapters.MyTripListVPAdapter;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.model.MatchingTripModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh on 01-11-2016.
 */
public class MatchingTripListActivity extends AbstractFragmentActivity implements View.OnClickListener {


    private ViewPager vpMyList;
    private LinearLayout llMatchingTripMain;
    private LinearLayout llCountDotsMain;
    private MyTripListVPAdapter myTripListVPAdapter;
    private String parcelId;
    private AppHeaderView appHeaderView;
    private ParcelDetailsData parcelDetailsData;
    private MatchingTripModel matchingTripModel = new MatchingTripModel();

    private void init() {
        //setHeader(getResources().getString(R.string.my_trip));
        llMatchingTripMain = (LinearLayout) findViewById(R.id.llMatchingTripMain);
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        vpMyList = (ViewPager) findViewById(R.id.vpMyList);
        llCountDotsMain = (LinearLayout) findViewById(R.id.llCountDotsMain);
        viewPagerChangeListener();
        drawPageSelectionIndicators(0);
        parcelDetailsData = (ParcelDetailsData) getIntent().getExtras().getSerializable("data");
        getParcelDetails(parcelDetailsData.id);
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

    @Override
    protected BasicModel getModel() {
        return matchingTripModel;
    }

    private void getParcelDetails(String parcelId) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            matchingTripModel.getParcelDetails(parcelId);
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
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
                        Util.showOKSnakBar(llMatchingTripMain, getResources().getString(R.string.trip_unavailable));
                    }
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llMatchingTripMain, getResources().getString(R.string.pls_try_again));
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
            Intent intent = new Intent(this, ParcelPayNowActivity.class);
            intent.putExtra("parcelId", myTripsData.id);
            startActivity(intent);
        }
    }
}
