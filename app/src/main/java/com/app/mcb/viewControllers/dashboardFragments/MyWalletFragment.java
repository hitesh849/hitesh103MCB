package com.app.mcb.viewControllers.dashboardFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.MyWalletAdapter;
import com.app.mcb.adapters.ParcelsListVPAdapter;
import com.app.mcb.dao.MyWalletData;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.model.MyWalletModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by u on 9/15/2016.
 */
public class MyWalletFragment extends AbstractFragment implements View.OnClickListener {

    private RecyclerView rvTripListMyWallet;
    private TextView txtCurrentBalanceWallet;
    private LinearLayout llMyWalletMain;
    private MyWalletModel myWalletModel = new MyWalletModel();

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            View rootView = inflater.inflate(R.layout.my_wallet, container, false);
            init(rootView);
            getMyWalletDetails();
            return rootView;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void init(View rootView) {
        llMyWalletMain = (LinearLayout) rootView.findViewById(R.id.llMyWalletMain);
        txtCurrentBalanceWallet = (TextView) rootView.findViewById(R.id.txtCurrentBalanceWallet);
        rvTripListMyWallet = (RecyclerView) rootView.findViewById(R.id.rvTripListMyWallet);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvTripListMyWallet.setLayoutManager(llm);
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.my_wallet));
    }

    @Override
    protected BasicModel getModel() {
        return myWalletModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof MyWalletData) {
                MyWalletData myWalletData = (MyWalletData) data;
                if ("success".equals(myWalletData.status)) {
                    if (myWalletData.response != null) {
                        rvTripListMyWallet.setAdapter(new MyWalletAdapter(getActivity(), myWalletData.response));
                        if(myWalletData.response.size()==0)
                        {
                            //Util.showOKSnakBar(llMyWalletMain,);
                        }
                    }
                } else if ("Error".equals(myWalletData.status)) {
                    Util.showOKSnakBar(llMyWalletMain, myWalletData.errorMessage);
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llMyWalletMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void getMyWalletDetails() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                myWalletModel.getMyWalletDetails();
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

    }
}
