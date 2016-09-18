package com.app.mcb.viewControllers.transporter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.viewControllers.sender.AddParcelFragment;
import com.app.mcb.viewControllers.sender.ParcelsListFragment;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by Hitesh kumawat on 19-09-2016.
 */
public class TransporterHomeFragment extends AbstractFragment implements View.OnClickListener {

    private LinearLayout llActiveTripTransporterHome;
    private LinearLayout llAddTripTransporterHome;
    private LinearLayout llAllTripTransporterHome;
    private LinearLayout llLastSelectedTransPorterHome;
    private FrameLayout fmContainerTransporterHomeMain;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transporter_home_screen, container, false);
        init(view);
        Util.addFragment(getActivity(), R.id.fmContainerTransporterHomeMain, new MyTripList());
        return view;
    }

    private void init(View view) {
        llActiveTripTransporterHome = (LinearLayout) view.findViewById(R.id.llActiveTripTransporterHome);
        llAddTripTransporterHome = (LinearLayout) view.findViewById(R.id.llAddTripTransporterHome);
        llAllTripTransporterHome = (LinearLayout) view.findViewById(R.id.llAllTripTransporterHome);
        fmContainerTransporterHomeMain = (FrameLayout) view.findViewById(R.id.fmContainerTransporterHomeMain);
        llActiveTripTransporterHome.setOnClickListener(this);
        llAddTripTransporterHome.setOnClickListener(this);
        llAllTripTransporterHome.setOnClickListener(this);
        backgroundChange(llActiveTripTransporterHome);
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
            Util.replaceFragment(getActivity(),R.id.fmContainerTransporterHomeMain,new MyTripList());
            backgroundChange(llActiveTripTransporterHome);
        } else if (id == R.id.llAddTripTransporterHome) {
            Util.replaceFragment(getActivity(),R.id.fmContainerTransporterHomeMain,new AddTripFragment());
            backgroundChange(llAddTripTransporterHome);
        } else if (id == R.id.llAllTripTransporterHome) {
            Util.replaceFragment(getActivity(),R.id.fmContainerTransporterHomeMain,new MyTripList());
            backgroundChange(llAllTripTransporterHome);
        }
    }

    public void backgroundChange(LinearLayout currentLayout) {
        if (llLastSelectedTransPorterHome != null) {
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
    }
}
