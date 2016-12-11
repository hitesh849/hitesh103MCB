package com.app.mcb.viewControllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.ParcelListHomeAdapter;
import com.app.mcb.adapters.TripDetailsVPAdapter;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.AddParcelData;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.filters.HomeFilter;
import com.app.mcb.filters.TransporterFilter;
import com.app.mcb.model.HomeTripModel;
import com.app.mcb.retrointerface.TryAgainInterface;
import com.app.mcb.sharedPreferences.Config;

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
    private RelativeLayout rlTripDetailsContainer;
    private TextView txtAddTripParcel;
    int tripCount;
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
            tripCount = tripTransporterData.response.size();
            if (tripTransporterData.response.size() <= 0) {
                rlTripDetailsContainer.addView(Util.getViewDataNotFound(this, rlTripDetailsContainer, getString(R.string.trip_unavailable)));
            }
        }
        if (tripCount > 1)
            drawPageSelectionIndicators(0);
    }

    private void init() {
        llTripDetailsMain = (LinearLayout) findViewById(R.id.llTripDetailsMain);
        TransporterFilter.addFilterView(this, llTripDetailsMain, this);
        vpTripDetails = (ViewPager) findViewById(R.id.vpTripDetails);
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        rlTripDetailsContainer = (RelativeLayout) findViewById(R.id.rlTripDetailsContainer);
        txtAddTripParcel = (TextView) findViewById(R.id.txtAddTripParcel);
        appHeaderView.txtHeaderNamecenter.setText(getResources().getString(R.string.trip_details));
        txtAddTripParcel.setText(getString(R.string.add_parcels));
        txtAddTripParcel.setOnClickListener(this);
        viewPagerChangeListener();
    }

    private void addMainView() {
        rlTripDetailsContainer.removeAllViews();
        rlTripDetailsContainer.addView(vpTripDetails);
        rlTripDetailsContainer.addView(llCountDotsMain);
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
            }
            if (data != null && data instanceof RetrofitError) {
                rlTripDetailsContainer.addView(Util.getViewServerNotResponding(this, rlTripDetailsContainer, new TryAgainInterface() {
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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.llBookNoewTripDetailsRow1 || id == R.id.llBookNoewTripDetailsRow2) {
            Intent intent = null;
            if (Config.getLoginStatus()) {
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
            intent.putExtra("data", ((TripTransporterData) v.getTag()));
            startActivityForResult(intent, 501);
        } else if (id == R.id.txtAddTripParcel) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 502);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (501): {
                if (resultCode == Activity.RESULT_OK) {
                    TripTransporterData tripTransporterData = ((TripTransporterData) data.getSerializableExtra("data"));
                    if (Config.getLoginStatus()) {
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("data", tripTransporterData);
                        startActivity(intent);
                    }
                }
                break;
            }
            case (502): {
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(Constants.ADD_PARCELS_KEY, new AddParcelData());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            }
        }
    }

    private void drawPageSelectionIndicators(int mPosition) {
        if (llCountDotsMain != null) {
            llCountDotsMain.removeAllViews();
        }

        int count = (tripCount > 2) ? 3 : tripCount;
        llCountDotsMain = (LinearLayout) findViewById(R.id.llCountDotsMain);
        ImageView[] dots = new ImageView[3];

        if (mPosition >= count) {
            mPosition = (mPosition % count);
        }
        for (int i = 0; i < count; i++) {
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

    private void getTripByFilter(final FilterData filterData) {
        try {
            Intent intent = new Intent();
            intent.putExtra("FILTER_DATA", filterData);
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
