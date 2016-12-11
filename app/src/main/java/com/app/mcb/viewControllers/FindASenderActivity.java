package com.app.mcb.viewControllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.ParcelListHomeAdapter;
import com.app.mcb.adapters.TripListStateWiseAdapter;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.AddParcelData;
import com.app.mcb.dao.AddTripData;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.filters.TransporterFilter;
import com.app.mcb.model.HomeTripModel;
import com.app.mcb.retrointerface.TryAgainInterface;
import com.app.mcb.viewControllers.sender.ParcelDetailsSenderSearchActivity;
import com.app.mcb.viewControllers.transporter.TransporterHomeFragment;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;
import java.util.logging.Filter;

import retrofit.RetrofitError;

/**
 * Created by u on 10/27/2016.
 */
public class FindASenderActivity extends AbstractFragmentActivity implements View.OnClickListener, CommonListener {

    private AppHeaderView appHeaderView;
    private RecyclerView rvTripHome;
    private LinearLayout llBecomeTransporter;
    private RelativeLayout rlStateWiseContainerMain;
    private RelativeLayout rlStateWiseContainerMainSub;
    private LinearLayout llTripListWithState;
    private TextView txtAddTripParcel;
    HomeTripModel homeTripModel = new HomeTripModel();
    private ParcelListData parcelListDataa;
    FilterData filterData;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.trip_list_specific_state);
        init();
        filterData = (FilterData) getIntent().getSerializableExtra("KEY_DATA");
        if (filterData == null) {
            filterData = new FilterData();
            filterData.type = Constants.SENDER;
            filterData.fromDate = Util.getCurrentDate();
            filterData.toDate = Util.getNextDays(5);
        } else
            filterData.type = Constants.SENDER;
        getParcelsListByFilter(filterData);
    }

    private void init() {
        rlStateWiseContainerMain = (RelativeLayout) findViewById(R.id.rlStateWiseContainerMain);
        rlStateWiseContainerMainSub = (RelativeLayout) findViewById(R.id.rlStateWiseContainerMainSub);
        llTripListWithState = (LinearLayout) findViewById(R.id.llTripListWithState);
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        TransporterFilter.addFilterView(this, llTripListWithState, this);
        appHeaderView.imgBackHeaderArrow.setImageResource(R.mipmap.back);
        rvTripHome = (RecyclerView) findViewById(R.id.rvTripHome);
        txtAddTripParcel = (TextView) findViewById(R.id.txtAddTripParcel);
        llBecomeTransporter = (LinearLayout) findViewById(R.id.llBecomeTransporter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvTripHome.setLayoutManager(llm);
        llBecomeTransporter.setOnClickListener(this);
        txtAddTripParcel.setOnClickListener(this);
        appHeaderView.txtHeaderNamecenter.setText("Welcome");
    }

    private void addMainView() {
        rlStateWiseContainerMain.removeAllViews();
        rlStateWiseContainerMain.addView(rlStateWiseContainerMainSub);

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
                    if (parcelListData.response != null)
                        rvTripHome.setAdapter(new ParcelListHomeAdapter(this, this, parcelListData.response));
                    if (parcelListData.response.size() <= 0)
                        rlStateWiseContainerMain.addView(Util.getViewDataNotFound(this, rlStateWiseContainerMain, getString(R.string.parcel_unavailable)));
                }
            } else if (data != null && data instanceof RetrofitError) {
                rlStateWiseContainerMain.addView(Util.getViewServerNotResponding(this, rlStateWiseContainerMain, new TryAgainInterface() {
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
        if (id == R.id.llBackHeader) {
            onBackPressed();
        } else if (id == R.id.llSenderHomeRowMain) {
            ParcelDetailsData parcelDetailsData = ((ParcelDetailsData) v.getTag());
            Intent intent = new Intent(this, ParcelDetailsSenderSearchActivity.class);
            intent.putExtra("data", parcelDetailsData);
            startActivityForResult(intent, 504);
        } else if (id == R.id.llBecomeTransporter) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        } else if (id == R.id.txtAddTripParcel) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 502);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (101): {
                if (resultCode == Activity.RESULT_OK) {
                    ParcelListData parcelListData = ((ParcelListData) data.getSerializableExtra("data"));
                    if (parcelListData.response != null && parcelListData.response.size() > 0) {
                        rvTripHome.setAdapter(new ParcelListHomeAdapter(this, this, parcelListData.response));
                    }
                }
                break;
            }
            case (504):
            {
                if (resultCode == Activity.RESULT_OK) {
                    FilterData filterData = ((FilterData) data.getSerializableExtra("FILTER_DATA"));
                    filterData(filterData);
                }
                break;
            }

            case (502): {
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

    private void getAirportList() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                homeTripModel.getAirportData(this);
            } else {
                rlStateWiseContainerMain.addView(Util.getViewInternetNotFound(this, rlStateWiseContainerMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addMainView();
                        getAirportList();
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getParcelsListByFilter(final FilterData filterData) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                homeTripModel.getParcelsListByFilter(filterData);
            } else {
                rlStateWiseContainerMain.addView(Util.getViewInternetNotFound(this, rlStateWiseContainerMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addMainView();
                        getParcelsListByFilter(filterData);
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void filterData(FilterData filterData) {
        addMainView();
        filterData.type = Constants.SENDER;
        getParcelsListByFilter(filterData);
    }
}
