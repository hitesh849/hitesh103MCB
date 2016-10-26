package com.app.mcb.viewControllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.TripDetailsVPAdapter;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.filters.HomeFilter;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.model.HomeTripModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh on 9/16/2016.
 */
public class CommonDetailsActivity extends AbstractFragmentActivity implements View.OnClickListener, CommonListener {

    private ViewPager vpTripDetails;
    private LinearLayout llCountDotsMain;
    private AppHeaderView appHeaderView;
    private LinearLayout llTripDetailsMain;
    private HomeTripModel homeTripModel = new HomeTripModel();
    private TripTransporterData tripTransporterData;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.trip_details);
        init();
        Bundle bundle = getIntent().getBundleExtra("KEY_BUNDLE");
        if (bundle != null) {
            tripTransporterData = (TripTransporterData) bundle.getSerializable("KEY_DATA");
            vpTripDetails.setAdapter(new TripDetailsVPAdapter(this, tripTransporterData.response, this));
        }
        drawPageSelectionIndicators(0);
    }

    private void init() {
        llTripDetailsMain = (LinearLayout) findViewById(R.id.llTripDetailsMain);
        HomeFilter.addFilterView(this, llTripDetailsMain, this);
        vpTripDetails = (ViewPager) findViewById(R.id.vpTripDetails);
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        appHeaderView.txtHeaderNamecenter.setText(getResources().getString(R.string.trip_details));
        viewPagerChangeListener();
    }

    private void viewPagerChangeListener() {
        vpTripDetails.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @Override
    protected BasicModel getModel() {
        return homeTripModel;
    }

    @Override
    public void update(Observable o, Object data) {
        Util.dimissProDialog();

        try {
            if (data != null && data instanceof TripTransporterData) {
                TripTransporterData tripTransporterData = (TripTransporterData) data;
                if (tripTransporterData.status.equals("success")) {
                    if (tripTransporterData.response != null)
                        vpTripDetails.setAdapter(new TripDetailsVPAdapter(this, tripTransporterData.response, this));

                    if (tripTransporterData.response.size() <= 0)
                        Util.showOKSnakBar(llTripDetailsMain, getResources().getString(R.string.trip_unavailable));
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llTripDetailsMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.llBookNoewTripDetailsRow1 || id == R.id.llBookNoewTripDetailsRow2) {

            TripTransporterData tripTransporterData=(TripTransporterData)v.getTag();
            Intent intent=new Intent(this, LoginActivity.class);
            intent.putExtra("data",tripTransporterData);
            /*Bundle bundle=new Bundle();
            bundle.putSerializable();
            intent.putExtra("bundle",bundle);*/
            startActivity(intent);
        }
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
    public void filterData(FilterData filterData) {
        filterData.type = Constants.KEY_TRANSPORTER;
        getTripByFilter(filterData);
    }

    private void getTripByFilter(FilterData filterData) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                homeTripModel.getTripListByFilter(filterData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
