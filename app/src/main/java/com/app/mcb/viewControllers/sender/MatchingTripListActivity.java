package com.app.mcb.viewControllers.sender;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.MatchingTripVPAdaptor;
import com.app.mcb.adapters.MyTripListVPAdapter;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.AddParcelData;
import com.app.mcb.dao.AddTripData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.model.MatchingTripModel;
import com.app.mcb.retrointerface.TryAgainInterface;

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
    private RelativeLayout rlParcelMatchingSub;
    private int tripCount;
    private MyTripsData myTripsData;
    private MatchingTripModel matchingTripModel = new MatchingTripModel();

    private void init() {
        //setHeader(getResources().getString(R.string.my_trip));
        llMatchingTripMain = (LinearLayout) findViewById(R.id.llMatchingTripMain);
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        vpMyList = (ViewPager) findViewById(R.id.vpMyList);
        llCountDotsMain = (LinearLayout) findViewById(R.id.llCountDotsMain);
        rlParcelMatchingSub = (RelativeLayout) findViewById(R.id.rlParcelMatchingSub);
        viewPagerChangeListener();
        parcelDetailsData = (ParcelDetailsData) getIntent().getExtras().getSerializable("data");
        appHeaderView.txtHeaderNamecenter.setText(getString(R.string.matching_trips));
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

    private void addMainView() {
        rlParcelMatchingSub.removeAllViews();
        rlParcelMatchingSub.addView(vpMyList);
        rlParcelMatchingSub.addView(llCountDotsMain);
    }

    private void drawPageSelectionIndicators(int mPosition) {
        if (llCountDotsMain != null) {
            llCountDotsMain.removeAllViews();
        }

        int totalDots = (tripCount > 2) ? 3 : tripCount;
        ImageView[] dots = new ImageView[totalDots];

        if (mPosition >= totalDots) {
            mPosition = (mPosition % totalDots);
        }
        for (int i = 0; i < totalDots; i++) {

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

    private void updateParcels(final ParcelDetailsData parcelDetailsData) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            matchingTripModel.updateParcels(parcelDetailsData);
        } else {
            rlParcelMatchingSub.addView(Util.getViewInternetNotFound(this, rlParcelMatchingSub, new TryAgainInterface() {
                @Override
                public void callBack() {
                    addMainView();
                    updateParcels(parcelDetailsData);
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
                    tripCount = parcelDetailsData.tripsmatch.size();
                    if (tripCount > 1)
                        drawPageSelectionIndicators(0);
                    if (parcelDetailsData.tripsmatch == null || parcelDetailsData.tripsmatch.size() == 0) {
                        rlParcelMatchingSub.addView(Util.getViewDataNotFound(this, rlParcelMatchingSub, getString(R.string.no_matching_transporter_found)));
                    }
                }
            } else if (data != null && data instanceof AddParcelData) {
                AddParcelData addTripData = ((AddParcelData) data);
                if ("success".equals(addTripData.status)) {
                    Intent intent = new Intent(this, ParcelPayNowActivity.class);
                    intent.putExtra("parcelId", myTripsData.id);
                    startActivity(intent);
                }
            } else if (data != null && data instanceof RetrofitError) {
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
            myTripsData = ((MyTripsData) view.getTag());
            if (!TextUtils.isEmpty(parcelDetailsData.weight) && Integer.parseInt(parcelDetailsData.weight) > Integer.parseInt(myTripsData.capacity)) {

                Util.showAlertDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateParcels(parcelDetailsData);
                    }
                }, String.format(getString(R.string.trip_capacity_check), myTripsData.capacity));
            } else {
                Intent intent = new Intent(this, ParcelPayNowActivity.class);
                intent.putExtra("parcelId", myTripsData.id);
                startActivity(intent);
            }

        }
    }
}
