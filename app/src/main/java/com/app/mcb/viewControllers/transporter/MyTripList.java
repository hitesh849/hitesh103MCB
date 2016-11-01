package com.app.mcb.viewControllers.transporter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.MyTripListVPAdapter;
import com.app.mcb.dao.CommonResponseData;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.filters.TripFilter;
import com.app.mcb.model.MyTripsModel;
import com.app.mcb.sharedPreferences.Config;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh kumawat on 19-09-2016.
 */
public class MyTripList extends AbstractFragment implements View.OnClickListener, CommonListener {

    private ViewPager vpMyList;
    private LinearLayout llCountDotsMain;
    private MyTripsModel myTripsModel = new MyTripsModel();
    private MyTripListVPAdapter myTripListVPAdapter;
    private RelativeLayout rlMyTripMain;
    ArrayList<MyTripsData> tripListMain = new ArrayList<MyTripsData>();

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_trip_list_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.my_trip));
        rlMyTripMain = (RelativeLayout) view.findViewById(R.id.rlMyTripMain);
        vpMyList = (ViewPager) view.findViewById(R.id.vpMyList);
        llCountDotsMain = (LinearLayout) view.findViewById(R.id.llCountDotsMain);
        TripFilter.addFilterView(getActivity(), view, this);
        viewPagerChangeListener();
        drawPageSelectionIndicators(0);
        getMyTripsList();
    }

    @Override
    protected BasicModel getModel() {
        return myTripsModel;
    }

    @Override
    public void update(Observable observable, Object o) {
        Util.dimissProDialog();
        if (o instanceof MyTripsData) {
            MyTripsData myTripsData = ((MyTripsData) o);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(myTripsData.status)) {
                tripListMain = myTripsData.response;
                myTripListVPAdapter = new MyTripListVPAdapter(getActivity(), this, tripListMain);
                vpMyList.setAdapter(myTripListVPAdapter);
            }
        } else if (o instanceof CommonResponseData) {
            CommonResponseData responseData = ((CommonResponseData) o);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(responseData.status)) {
                getMyTripsList();
            }
        } else if (o instanceof RetrofitError) {
            Util.showOKSnakBar(rlMyTripMain, getResources().getString(R.string.pls_try_again));
        }

    }

    private void getMyTripsList() {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(getActivity());
            myTripsModel.getUserTripList();
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgCancelTrip) {
            MyTripsData myTripsData = ((MyTripsData) view.getTag());
            if (myTripsData != null) {
                HashMap<String, Object> requestData = new HashMap<>();
                requestData.put("id", myTripsData.id);
                requestData.put("status", "4");
                requestData.put("process_by", Config.getUserId());
                requestData.put("reason", "");
                cancelTrip(requestData);
            }
        } else if (id == R.id.imgEditTrip) {
            MyTripsData myTripsData = (MyTripsData) view.getTag();
            AddTripFragment addTripFragment = new AddTripFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("tripData", myTripsData);
            addTripFragment.setArguments(bundle);

            Util.replaceFragment(getActivity(), R.id.fmContainerTransporterHomeMain, addTripFragment);
        } else if (id == R.id.llBookedParcelsMyTripList || id == R.id.llFindParcelsMyTripList) {
            MyTripsData myTripsData = (MyTripsData) view.getTag();
            Intent intent = new Intent(getActivity(), MyTripParcelActivity.class);
            Bundle bundle = new Bundle();
            myTripsData.myClickOn=(id == R.id.llBookedParcelsMyTripList?"Booked":"Find");
            bundle.putSerializable("KEY_DATA", myTripsData);
            intent.putExtra("KEY_BUNDLE", bundle);
            startActivity(intent);
        }
    }

    private void cancelTrip(HashMap<String, Object> requestData) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(getActivity());
            myTripsModel.cancelTrip(requestData);
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
        }
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

    private void drawPageSelectionIndicators(int mPosition) {
        if (llCountDotsMain != null) {
            llCountDotsMain.removeAllViews();
        }

        ImageView[] dots = new ImageView[3];

        if (mPosition > 2) {
            mPosition = (mPosition % 3);
        }
        for (int i = 0; i < 3; i++) {


            dots[i] = new ImageView(getActivity());
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
        ArrayList<MyTripsData> filterList = new ArrayList<MyTripsData>();
        for (MyTripsData myTripsData : tripListMain) {
            if (myTripsData.TripID.equalsIgnoreCase(filterData.tripId) || Util.getDateFromDateTimeFormat(myTripsData.dep_time).equalsIgnoreCase(filterData.departure_date) || myTripsData.status.equalsIgnoreCase(filterData.tripStatus)) {
                filterList.add(myTripsData);
            }
        }
        vpMyList.setAdapter(new MyTripListVPAdapter(getActivity(), this, filterList));
    }
}
