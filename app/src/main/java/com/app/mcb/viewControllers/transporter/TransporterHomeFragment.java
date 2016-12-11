package com.app.mcb.viewControllers.transporter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Util;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by Hitesh kumawat on 19-09-2016.
 */
public class TransporterHomeFragment extends AbstractFragment implements View.OnClickListener {

    public LinearLayout llActiveTripTransporterHome;
    private LinearLayout llAddTripTransporterHome;
    public LinearLayout llCancelledTripTransporterHome;
    private LinearLayout llLastSelectedTransPorterHome;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transporter_home_screen, container, false);
        init(view);

        return view;
    }

    private void init(View view) {
        llActiveTripTransporterHome = (LinearLayout) view.findViewById(R.id.llActiveTripTransporterHome);
        llAddTripTransporterHome = (LinearLayout) view.findViewById(R.id.llAddTripTransporterHome);
        llCancelledTripTransporterHome = (LinearLayout) view.findViewById(R.id.llCancelledTripTransporterHome);
        llActiveTripTransporterHome.setOnClickListener(this);
        llAddTripTransporterHome.setOnClickListener(this);
        llCancelledTripTransporterHome.setOnClickListener(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            AbstractFragment abstractFragment = new AddTripFragment();
            abstractFragment.setArguments(bundle);
            backgroundChange(llAddTripTransporterHome);
            Util.addFragment(getActivity(), R.id.fmContainerTransporterHomeMain, abstractFragment);
        }
        else
        {
            onClick(llActiveTripTransporterHome);
        }

    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.llActiveTripTransporterHome) {
            Util.replaceFragment(getActivity(), R.id.fmContainerTransporterHomeMain, new MyTripListFragment());
            backgroundChange(llActiveTripTransporterHome);
        } else if (id == R.id.llAddTripTransporterHome) {
            Util.replaceFragment(getActivity(), R.id.fmContainerTransporterHomeMain, new AddTripFragment());
            backgroundChange(llAddTripTransporterHome);
        } else if (id == R.id.llCancelledTripTransporterHome) {
            Util.replaceFragment(getActivity(), R.id.fmContainerTransporterHomeMain, new CancelledTrips());
            backgroundChange(llCancelledTripTransporterHome);
        }
    }

    public void backgroundChange(LinearLayout currentLayout) {
        if (llLastSelectedTransPorterHome != null) {
            llLastSelectedTransPorterHome.setSelected(false);
            for (int i = 0; i < llLastSelectedTransPorterHome.getChildCount(); i++) {
                View view = llLastSelectedTransPorterHome.getChildAt(i);
                if (view instanceof ImageView) {
                    ((ImageView) view).setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
                } else if (view instanceof TextView) {
                    ((TextView) view).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                }
            }
        }

        for (int i = 0; i < currentLayout.getChildCount(); i++) {
            View view = currentLayout.getChildAt(i);
            if (view instanceof ImageView) {
                ((ImageView) view).setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            } else if (view instanceof TextView) {
                ((TextView) view).setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            }
        }
        llLastSelectedTransPorterHome = currentLayout;
        llLastSelectedTransPorterHome.setSelected(true);
    }
}
