package com.app.mcb.viewControllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.TripListStateWiseAdapter;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.AirportData;
import com.app.mcb.dao.FilterData;
import com.app.mcb.database.DatabaseMgr;
import com.app.mcb.filters.TripFilter;
import com.app.mcb.filters.TripListener;
import com.app.mcb.model.TripModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

/**
 * Created by u on 9/15/2016.
 */
public class TripListWithStateActivity extends AbstractFragmentActivity implements View.OnClickListener,TripListener {

    private AppHeaderView appHeaderView;
    private RecyclerView rvTripHome;
    private LinearLayout llBecomeTransporter;
    private LinearLayout llTripListWithState;
    TripModel tripModel = new TripModel();

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.trip_list_specific_state);
        init();
    }

    private void init() {
        llTripListWithState = (LinearLayout) findViewById(R.id.llTripListWithState);
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        TripFilter.addFilterView(this,llTripListWithState,this);
        appHeaderView.imgBackHeaderArrow.setImageResource(R.mipmap.back);
        rvTripHome = (RecyclerView) findViewById(R.id.rvTripHome);
        llBecomeTransporter = (LinearLayout) findViewById(R.id.llBecomeTransporter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvTripHome.setLayoutManager(llm);
        rvTripHome.setAdapter(new TripListStateWiseAdapter(this, this));
        llBecomeTransporter.setOnClickListener(this);
        appHeaderView.txtHeaderNamecenter.setText("Welcome");
        getAirportList();
    }

    @Override
    protected BasicModel getModel() {
        return tripModel;
    }

    @Override
    public void update(Observable o, Object arg) {
        Util.dimissProDialog();

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.llBackHeader) {
            onBackPressed();
        } else if (id == R.id.llHomeRowMain) {

            startActivity(new Intent(this, TripDetailsActivity.class));
        } else if (id == R.id.llBecomeTransporter) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    private void getAirportList() {
        try {
            if (Util.isDeviceOnline() && DatabaseMgr.getInstance(this).getNoOfRecords(AirportData.TABLE_NAME) <= 0) {
                Util.showProDialog(this);
                tripModel.getAirportData(this);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void filterData(FilterData filterData) {

    }
}
