package com.app.mcb.viewControllers.dashboardFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.mcb.R;

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
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
            addFragment(new MyProfileFragment());
        } else if (id == R.id.llMyWalletDashBoard) {
            addFragment(new MyWalletFragment());
        } else if (id == R.id.llSenderDetailsDashBoard) {

        } else if (id == R.id.llDetailsOfTransDashBoard) {

        } else if (id == R.id.llTotalReceiverDashBoard) {

        } else if (id == R.id.llTotalWithDrawerDashBoard) {
            addFragment(new WithDrawFragment());
        }
    }
    public void addFragment(Fragment fragment)
    {
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fmHomeContainer, fragment).commit();
    }
}
