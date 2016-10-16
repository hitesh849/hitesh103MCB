package com.app.mcb.viewControllers.dashboardFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.viewControllers.sender.SenderHomeFragment;
import com.app.mcb.viewControllers.transporter.TransporterHomeFragment;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by Hitesh kumawat on 14-09-2016.
 */
public class DashBoardFragment extends AbstractFragment implements View.OnClickListener {

    private LinearLayout llAboutProfileDashBoard;
    private LinearLayout llMyWalletDashBoard;
    private LinearLayout llSenderDetailsDashBoard;
    private LinearLayout llDetailsOfTransDashBoard;
    private LinearLayout llTotalReceiverDashBoard;
    private LinearLayout llTotalWithDrawerDashBoard;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        llAboutProfileDashBoard = (LinearLayout) view.findViewById(R.id.llAboutProfileDashBoard);
        llMyWalletDashBoard = (LinearLayout) view.findViewById(R.id.llMyWalletDashBoard);
        llSenderDetailsDashBoard = (LinearLayout) view.findViewById(R.id.llSenderDetailsDashBoard);
        llDetailsOfTransDashBoard = (LinearLayout) view.findViewById(R.id.llDetailsOfTransDashBoard);
        llTotalReceiverDashBoard = (LinearLayout) view.findViewById(R.id.llTotalReceiverDashBoard);
        llTotalWithDrawerDashBoard = (LinearLayout) view.findViewById(R.id.llTotalWithDrawerDashBoard);
        llAboutProfileDashBoard.setOnClickListener(this);
        llMyWalletDashBoard.setOnClickListener(this);
        llSenderDetailsDashBoard.setOnClickListener(this);
        llDetailsOfTransDashBoard.setOnClickListener(this);
        llTotalReceiverDashBoard.setOnClickListener(this);
        llTotalWithDrawerDashBoard.setOnClickListener(this);
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.dashboard));
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
        if (id == R.id.llAboutProfileDashBoard) {
            Util.replaceFragment(getActivity(), R.id.fmHomeContainer, new MyProfileFragment());
        } else if (id == R.id.llMyWalletDashBoard) {
            Util.replaceFragment(getActivity(), R.id.fmHomeContainer, new MyWalletFragment());
        } else if (id == R.id.llSenderDetailsDashBoard) {
            Util.replaceFragment(getActivity(), R.id.fmHomeContainer, new SenderHomeFragment());
        } else if (id == R.id.llDetailsOfTransDashBoard) {
            Util.replaceFragment(getActivity(), R.id.fmHomeContainer, new TransporterHomeFragment());
        } else if (id == R.id.llTotalReceiverDashBoard) {
            Util.replaceFragment(getActivity(), R.id.fmHomeContainer, new ReceiverListFragment());
        } else if (id == R.id.llTotalWithDrawerDashBoard) {
            Util.replaceFragment(getActivity(), R.id.fmHomeContainer, new WithDrawFragment());
        }
    }
}
