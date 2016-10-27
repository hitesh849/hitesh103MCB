package com.app.mcb.viewControllers.transporter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.MyTripDetailsVPAdapter;
import com.app.mcb.adapters.TripDetailsVPAdapter;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.MyTripDetailsData;
import com.app.mcb.dao.TripData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.model.MyTripParcelListModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh on 28-10-2016.
 */
public class MyTripParcelActivity extends AbstractFragmentActivity implements View.OnClickListener {

    private ViewPager vpMyTripParcelList;
    private LinearLayout llCountDotsMain;
    private RelativeLayout rlMyTripParcelListMain;
    String TripId;
    private MyTripParcelListModel myTripParcelListModel=new MyTripParcelListModel();
    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.my_trip_parcel_list);
        rlMyTripParcelListMain = (RelativeLayout) findViewById(R.id.rlMyTripParcelListMain);
        vpMyTripParcelList = (ViewPager) findViewById(R.id.vpMyTripParcelList);
        llCountDotsMain = (LinearLayout) findViewById(R.id.llCountDotsMain);
        TripId=(String)getIntent().getStringExtra("TripId");
        getMyTripDetails(TripId);
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
                    if (myTripDetailsData.parcel != null)
                        vpMyTripParcelList.setAdapter(new MyTripDetailsVPAdapter(this,this, myTripDetailsData.parcel));

                    if (myTripDetailsData.response.size() <= 0)
                        Util.showOKSnakBar(rlMyTripParcelListMain, getResources().getString(R.string.trip_unavailable));
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(rlMyTripParcelListMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {

    }
}
