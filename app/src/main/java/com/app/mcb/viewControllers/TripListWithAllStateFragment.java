package com.app.mcb.viewControllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.TripListCommonAdapter;
import com.app.mcb.adapters.TripListStateWiseAdapter;
import com.app.mcb.custom.ProgressDialog;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.filters.TripFilter;
import com.app.mcb.filters.TripListener;
import com.app.mcb.model.TripModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh kumawat on 14-09-2016.
 */
public  class TripListWithAllStateFragment extends AbstractFragment implements View.OnClickListener,TripListener {

    private RecyclerView rvTripHome;
    private LinearLayout llBecomeTransporter;
    private TripModel tripModel = new TripModel();
    private TripTransporterData tripTransporterData;
    private LinearLayout llHomeFragmentMain;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            View rootView = inflater.inflate(R.layout.home_fragment, container, false);
            init(rootView);
            return rootView;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void init(View rootView) {
        rvTripHome = (RecyclerView) rootView.findViewById(R.id.rvTripHome);
        llBecomeTransporter = (LinearLayout) rootView.findViewById(R.id.llBecomeTransporter);
        llHomeFragmentMain = (LinearLayout) rootView.findViewById(R.id.llHomeFragmentMain);
        llBecomeTransporter.setOnClickListener(this);
        TripFilter.addFilterView(getActivity(),rootView,this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvTripHome.setLayoutManager(llm);
        rvTripHome.setAdapter(new TripListCommonAdapter(getActivity(), this, true));
        ((MainActivity) getActivity()).setHeader("Welcome");
    }

    @Override
    protected BasicModel getModel() {
        return tripModel;
    }


    @Override
    public void update(Observable o, Object data) {
        Util.dimissProDialog();

        try {
            if (data != null && data instanceof TripTransporterData) {
                tripTransporterData = (TripTransporterData) data;
                if (tripTransporterData.status.equals("success")) {
                    if (tripTransporterData.response != null)
                        rvTripHome.setAdapter(new TripListStateWiseAdapter(getActivity(), this, tripTransporterData.response));

                    if (tripTransporterData.response.size() <= 0)
                        Util.showOKSnakBar(llHomeFragmentMain, getResources().getString(R.string.trip_unavailable));
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llHomeFragmentMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
    private Fragment getCurrentFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager
                .findFragmentByTag(fragmentTag);
        return currentFragment;
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.txtViewAllStateRow) {
            Intent intent = new Intent(getActivity(), TripListWithStateActivity.class);
            startActivity(intent);
        } else if (id == R.id.llBecomeTransporter) {
            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.llHomeRowMain) {

            startActivity(new Intent(getActivity(), TripDetailsActivity.class));
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
                Util.showProDialog(getActivity());
                tripModel.getTripListByFilter(filterData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
