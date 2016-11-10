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
import com.app.mcb.adapters.ParcelListHomeAdapter;
import com.app.mcb.adapters.ParcelListVPSearchAdapter;
import com.app.mcb.adapters.ParcelsListVPAdapter;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.filters.TransporterFilter;
import com.app.mcb.model.HomeTripModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by u on 10/27/2016.
 */
public class ParcelsDetailsForSenderHome extends AbstractFragmentActivity implements View.OnClickListener, CommonListener {

    private ViewPager vpParcelList;
    private LinearLayout llCountDotsMain;
    private LinearLayout llParcelListMain;
    private AppHeaderView appHeaderView;
    private HomeTripModel homeTripModel = new HomeTripModel();
    private FilterData filterData = new FilterData();

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.parcel_details_for_sender_home);
        llParcelListMain = (LinearLayout) findViewById(R.id.llParcelListMain);
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        TransporterFilter.addFilterView(this, llParcelListMain, this);
        vpParcelList = (ViewPager) findViewById(R.id.vpParcelList);
        llCountDotsMain = (LinearLayout) findViewById(R.id.llCountDotsMain);
        filterData.fromDate = Util.getCurrentDate();
        filterData.toDate = Util.getNextDays(5);
        viewPagerChangeListener();
        Bundle bundle = getIntent().getBundleExtra("KEY_BUNDLE");
        if (bundle != null) {
            ParcelListData parcelListData = (ParcelListData) bundle.getSerializable("data");
            vpParcelList.setAdapter(new ParcelListVPSearchAdapter(this, parcelListData.response, this));
        }
        drawPageSelectionIndicators(0);
    }

    private void getParcelsListByFilter(FilterData filterData) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                homeTripModel.getParcelsListByFilter(filterData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected BasicModel getModel() {
        return homeTripModel;
    }

    @Override
    public void update(Observable o, Object data) {
        Util.dimissProDialog();

        try {
            if (data != null && data instanceof ParcelListData) {
                ParcelListData parcelListData = (ParcelListData) data;
                if (parcelListData.status.equals("success")) {

                    if (parcelListData.response.size() <= 0)
                        Util.showOKSnakBar(llParcelListMain, getResources().getString(R.string.trip_unavailable));
                    else {
                        Intent intent = new Intent();
                        intent.putExtra("data", parcelListData);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llParcelListMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void filterData(FilterData filterData) {
        filterData.type = Constants.SENDER;
        getParcelsListByFilter(filterData);
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

    private void viewPagerChangeListener() {
        vpParcelList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

}
