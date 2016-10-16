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
import com.app.mcb.adapters.CancelledTripsAdapter;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.model.CancelledTripsModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh kumawat on 19-09-2016.
 */
public class CancelledTrips extends AbstractFragment implements View.OnClickListener {
    private ViewPager vpCancelledTripsList;
    private LinearLayout llCountDotsMain;
    private CancelledTripsModel cancelledTripsModel = new CancelledTripsModel();
    private CancelledTripsAdapter cancelledTripsAdapter;


    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cancelled_trips_layout, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.my_trip));
        vpCancelledTripsList = (ViewPager) view.findViewById(R.id.vpCancelledTripsList);
        llCountDotsMain = (LinearLayout) view.findViewById(R.id.llCountDotsMain);
        viewPagerChangeListener();
        drawPageSelectionIndicators(0);
        getCancelledTripsList();
    }

    @Override
    protected BasicModel getModel() {
        return cancelledTripsModel;
    }

    @Override
    public void update(Observable observable, Object o) {
        Util.dimissProDialog();
        if (o instanceof MyTripsData) {
            MyTripsData myTripsData = ((MyTripsData) o);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(myTripsData.status)) {
                cancelledTripsAdapter = new CancelledTripsAdapter(getActivity(), this, myTripsData.response);
                vpCancelledTripsList.setAdapter(cancelledTripsAdapter);
            }
        } else if (o instanceof RetrofitError) {
        }

    }

    private void getCancelledTripsList() {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(getActivity());
            cancelledTripsModel.getCancelledTrip();
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    private void viewPagerChangeListener() {
        vpCancelledTripsList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
