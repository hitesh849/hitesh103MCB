package com.app.mcb.viewControllers.transporter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.MyTripListVPAdapter;
import com.app.mcb.dao.CommonResponseData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.model.MyTripsModel;
import com.app.mcb.sharedPreferences.Config;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.HashMap;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh kumawat on 19-09-2016.
 */
public class MyTripList extends AbstractFragment implements View.OnClickListener {

    private ViewPager vpMyList;
    private LinearLayout llCountDotsMain;
    private MyTripsModel myTripsModel = new MyTripsModel();
    private MyTripListVPAdapter myTripListVPAdapter;


    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_trip_list_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.my_trip));
        vpMyList = (ViewPager) view.findViewById(R.id.vpMyList);
        llCountDotsMain = (LinearLayout) view.findViewById(R.id.llCountDotsMain);
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
                myTripListVPAdapter = new MyTripListVPAdapter(getActivity(), this, myTripsData.response);
                vpMyList.setAdapter(myTripListVPAdapter);
            }
        } else if (o instanceof CommonResponseData) {
            CommonResponseData responseData = ((CommonResponseData) o);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(responseData.status)) {
                getMyTripsList();
            }
        } else if (o instanceof RetrofitError) {

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
}
