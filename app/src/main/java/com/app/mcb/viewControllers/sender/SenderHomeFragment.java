package com.app.mcb.viewControllers.sender;

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
import com.app.mcb.dao.TripTransporterData;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by Hitesh kumawat on 15-09-2016.
 */
public class SenderHomeFragment extends AbstractFragment implements View.OnClickListener {

    private LinearLayout llActiveParcelSenderHome;
    private LinearLayout llAddParcelSenderHome;
    private LinearLayout llAllParcelSenderHome;
    private LinearLayout llLastSelectedSenderHome;
    private FrameLayout fmContainerSenderHomeMain;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sender_home_fragment, container, false);
        init(view);
        Bundle bundle = getArguments();
        AbstractFragment abstractFragment = new ParcelsListFragment();
        if (bundle != null) {
            abstractFragment = new AddParcelFragment();
            abstractFragment.setArguments(bundle);
            backgroundChange(llAddParcelSenderHome);
        }
        Util.addFragment(getActivity(), R.id.fmContainerSenderHomeMain, abstractFragment);
        return view;
    }

    private void init(View view) {
        llActiveParcelSenderHome = (LinearLayout) view.findViewById(R.id.llActiveParcelSenderHome);
        llAddParcelSenderHome = (LinearLayout) view.findViewById(R.id.llAddParcelSenderHome);
        llAllParcelSenderHome = (LinearLayout) view.findViewById(R.id.llAllParcelSenderHome);
        fmContainerSenderHomeMain = (FrameLayout) view.findViewById(R.id.fmContainerSenderHomeMain);
        llActiveParcelSenderHome.setOnClickListener(this);
        llAddParcelSenderHome.setOnClickListener(this);
        llAllParcelSenderHome.setOnClickListener(this);
        backgroundChange(llActiveParcelSenderHome);
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
        if (id == R.id.llActiveParcelSenderHome || id == R.id.llAllParcelSenderHome) {
            Bundle bundle = new Bundle();
            ParcelsListFragment parcelsListFragment = new ParcelsListFragment();
            if (id == R.id.llActiveParcelSenderHome) {
                backgroundChange(llActiveParcelSenderHome);
                bundle.putString("DATA", "Active");
            } else if (id == R.id.llAllParcelSenderHome) {
                backgroundChange(llAllParcelSenderHome);
                bundle.putString("DATA", "All");
            }
            parcelsListFragment.setArguments(bundle);
            Util.replaceFragment(getActivity(), R.id.fmContainerSenderHomeMain, parcelsListFragment);

        } else if (id == R.id.llAddParcelSenderHome) {
            Util.replaceFragment(getActivity(), R.id.fmContainerSenderHomeMain, new AddParcelFragment());
            backgroundChange(llAddParcelSenderHome);
        }
    }

    public void backgroundChange(LinearLayout currentLayout) {
        if (llLastSelectedSenderHome != null) {
            for (int i = 0; i < llLastSelectedSenderHome.getChildCount(); i++) {
                View view = llLastSelectedSenderHome.getChildAt(i);
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
        llLastSelectedSenderHome = currentLayout;
    }
}
