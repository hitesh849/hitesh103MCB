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
import com.app.mcb.adapters.TripListWithAllStateAdapter;
import com.app.mcb.dao.AirportData;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.database.DatabaseMgr;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.filters.TransporterFilter;
import com.app.mcb.model.HomeTripModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh kumawat on 14-09-2016.
 */
public class CommonListWithAllStateFragment extends AbstractFragment implements View.OnClickListener, CommonListener {

    private RecyclerView rvTripHome;
    private LinearLayout llBecomeTransporter;
    private HomeTripModel homeTripModel = new HomeTripModel();
    private TripTransporterData tripTransporterData;
    private LinearLayout llHomeFragmentMain;
    private LinearLayout llFindSenderTransporter;
    private TransporterFilter transporterFilter;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            View rootView = inflater.inflate(R.layout.home_fragment, container, false);
            init(rootView);
            if (DatabaseMgr.getInstance(getActivity()).getNoOfRecords(AirportData.TABLE_NAME) <= 0)
                getAirportList();
            else {
                getTopForCityInHome();
            }
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
        llFindSenderTransporter = (LinearLayout) rootView.findViewById(R.id.llFindSenderTransporter);
        llBecomeTransporter.setOnClickListener(this);
        llFindSenderTransporter.setOnClickListener(this);
        transporterFilter = TransporterFilter.addFilterView(getActivity(), rootView, this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvTripHome.setLayoutManager(llm);
        rvTripHome.setAdapter(new TripListCommonAdapter(getActivity(), this, true));
        ((MainActivity) getActivity()).setHeader("Welcome");
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
                if (Constants.RESPONSE_SUCCESS_MSG.equals(tripTransporterData.status)) {
                    if (tripTransporterData.response != null)
                        rvTripHome.setAdapter(new TripListWithAllStateAdapter(getActivity(), this, tripTransporterData.response));

                    if (tripTransporterData.response.size() <= 0)
                        Util.showOKSnakBar(llHomeFragmentMain, getResources().getString(R.string.trip_unavailable));
                }

            } else if (data != null && data instanceof AirportData) {
                getTopForCityInHome();
                transporterFilter.setAdapter();
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llHomeFragmentMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager
                .findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txtViewAllStateRow || id == R.id.llHomeRowMain) {
            Intent intent = new Intent(getActivity(), CommonListWithStateActivity.class);
            Bundle bundle = new Bundle();
            TripTransporterData tripTransporterData = (TripTransporterData) v.getTag();
            bundle.putSerializable("KEY_DATA", tripTransporterData);
            bundle.putString("FLAG", "All");
            intent.putExtra("KEY_BUNDLE", bundle);
            startActivity(intent);
        } else if (id == R.id.llBecomeTransporter) {
            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            startActivity(intent);
        } else if (id == R.id.llFindSenderTransporter) {
            Intent intent = new Intent(getActivity(), ParcelsForSenderActivity.class);
            startActivity(intent);
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
                homeTripModel.getTripListByFilter(filterData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getAirportList() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                homeTripModel.getAirportData(getActivity());
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getTopForCityInHome() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                homeTripModel.getTopForCityInHome();
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
