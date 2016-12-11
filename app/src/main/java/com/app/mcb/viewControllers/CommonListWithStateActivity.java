package com.app.mcb.viewControllers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.TripListStateWiseAdapter;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.AddParcelData;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.filters.TransporterFilter;
import com.app.mcb.model.HomeTripModel;
import com.app.mcb.retrointerface.TryAgainInterface;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by u on 9/15/2016.
 */
public class CommonListWithStateActivity extends AbstractFragmentActivity implements View.OnClickListener, CommonListener {

    private AppHeaderView appHeaderView;
    private RecyclerView rvTripHome;
    private LinearLayout llBecomeTransporter;
    private LinearLayout llTripListWithState;
    private RelativeLayout rlStateWiseContainerMain;
    private LinearLayout llFooter;
    private TextView txtTittleMessage;
    private TextView txtAddTripParcel;
    HomeTripModel homeTripModel = new HomeTripModel();
    private TripTransporterData tripTransporterData;
    private Bundle bundle;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.trip_list_specific_state);

        init();
        bundle = getIntent().getBundleExtra("KEY_BUNDLE");
        if (bundle != null) {
            tripTransporterData = (TripTransporterData) bundle.getSerializable("KEY_DATA");
            String flag = bundle.getString("FLAG");
            FilterData filterData = new FilterData();
            filterData.type = Constants.TRANSPORTER;
            if ("All".equals(flag)) {
                filterData.city = tripTransporterData.city;
            } else {
                filterData.fromLocation = tripTransporterData.source;
                filterData.toLocation = tripTransporterData.destination;
                filterData.fromDate = tripTransporterData.arrival_time;
                filterData.toDate = tripTransporterData.dep_time;
            }
            getTripByFilter(filterData);
        }
    }

    private void addMainView() {
        rlStateWiseContainerMain.removeAllViews();
        rlStateWiseContainerMain.addView(rvTripHome);
        rlStateWiseContainerMain.addView(llFooter);
    }

    private void init() {
        llTripListWithState = (LinearLayout) findViewById(R.id.llTripListWithState);
        rlStateWiseContainerMain = (RelativeLayout) findViewById(R.id.rlStateWiseContainerMain);
        llFooter = (LinearLayout) findViewById(R.id.llFooter);
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        TransporterFilter.addFilterView(this, llTripListWithState, this);

        appHeaderView.imgBackHeaderArrow.setImageResource(R.mipmap.back);
        rvTripHome = (RecyclerView) findViewById(R.id.rvTripHome);
        txtTittleMessage = (TextView) findViewById(R.id.txtTittleMessage);
        txtAddTripParcel = (TextView) findViewById(R.id.txtAddTripParcel);
        llBecomeTransporter = (LinearLayout) findViewById(R.id.llBecomeTransporter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvTripHome.setLayoutManager(llm);
        llBecomeTransporter.setOnClickListener(this);
        txtAddTripParcel.setOnClickListener(this);
        appHeaderView.txtHeaderNamecenter.setText("Welcome");
        txtTittleMessage.setText(getString(R.string.where_send_your_parcel));
        txtAddTripParcel.setText(getString(R.string.add_parcels));
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
                tripTransporterData = (TripTransporterData) data;
                if (tripTransporterData.status.equals("success")) {
                    if (tripTransporterData.response != null)
                        rvTripHome.setAdapter(new TripListStateWiseAdapter(this, this, tripTransporterData.response));
                    if (tripTransporterData.response.size() <= 0) {
                        rlStateWiseContainerMain.addView(Util.getViewDataNotFound(this, rlStateWiseContainerMain, getString(R.string.trip_unavailable)));
                    }
                }
            } else if (data != null && data instanceof RetrofitError) {
                rlStateWiseContainerMain.addView(Util.getViewServerNotResponding(this, rlStateWiseContainerMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        startActivity(getIntent());
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
        } else if (id == R.id.llHomeRowMain) {
            Intent intent = new Intent(this, CommonDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("KEY_DATA", tripTransporterData);
            intent.putExtra("KEY_BUNDLE", bundle);
            startActivityForResult(intent, Constants.OnBackFilter);
        } else if (id == R.id.llBecomeTransporter) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        } else if (id == R.id.txtAddTripParcel) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 502);
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

    private void getTripByFilter(final FilterData filterData) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                homeTripModel.getTripListByFilter(filterData);
            } else {
                rlStateWiseContainerMain.addView(Util.getViewInternetNotFound(this, rlStateWiseContainerMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addMainView();
                        getTripByFilter(filterData);
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void filterData(FilterData filterData) {
        filterData.type = Constants.KEY_TRANSPORTER;
        getTripByFilter(filterData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (Constants.OnBackFilter): {
                if (resultCode == Activity.RESULT_OK) {
                    FilterData filterData = ((FilterData) data.getSerializableExtra("FILTER_DATA"));
                    filterData(filterData);
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
}
