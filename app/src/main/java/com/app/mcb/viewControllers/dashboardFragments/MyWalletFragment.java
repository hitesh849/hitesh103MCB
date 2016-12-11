package com.app.mcb.viewControllers.dashboardFragments;

import android.app.AlertDialog;
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
import com.app.mcb.adapters.WithDrawVPAdapter;
import com.app.mcb.dao.MyWalletData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.dao.WithDrawData;
import com.app.mcb.model.MyWalletModel;
import com.app.mcb.retrointerface.TryAgainInterface;
import com.app.mcb.sharedPreferences.Config;
import com.app.mcb.viewControllers.LoginActivity;

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
    private String dialogHeader;
    private String withDrawId;
    private MyWalletModel myWalletModel = new MyWalletModel();

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            View rootView = inflater.inflate(R.layout.my_wallet, container, false);
            init(rootView);
            getMyWalletDetails();
            getUserDetails();
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

    private void getUserDetails() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                myWalletModel.getUserDetails();
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
                        rvTripListMyWallet.setAdapter(new MyWalletAdapter(getActivity(), myWalletData.response, this));


                        if (myWalletData.response.size() == 0) {
                            //Util.showOKSnakBar(llMyWalletMain,);
                        }
                    }
                } else if ("Error".equals(myWalletData.status)) {
                    Util.showOKSnakBar(llMyWalletMain, myWalletData.errorMessage);
                }
            } else if (data != null && data instanceof WithDrawData) {
                WithDrawData withDrawData = (WithDrawData) data;
                if ("success".equals(withDrawData.status)) {

                    for (WithDrawData drawData : withDrawData.response) {
                        if (withDrawId != null && withDrawId.equals(drawData.withdrawID)) {
                            walletDetails(drawData);
                        }
                    }

                } else if ("Error".equals(withDrawData.status)) {
                    Util.showOKSnakBar(llMyWalletMain, getResources().getString(R.string.pls_try_again));
                }
            } else if (data != null && data instanceof ParcelDetailsData) {
                ParcelDetailsData parcelDetailsData = (ParcelDetailsData) data;
                ParcelDetailsData parcelDetailsDataa = parcelDetailsData.response.get(0);
                if ("success".equals(parcelDetailsData.status)) {
                    parcelTripDetails(parcelDetailsDataa);
                }
            } else if (data != null && data instanceof UserInfoData) {
                UserInfoData userInfoData = (UserInfoData) data;
                if (userInfoData.status.equals("success")) {
                    if (userInfoData.response != null) {
                        userInfoData = userInfoData.response.get(0);
                        LoginActivity.saveUserData(userInfoData);
                        txtCurrentBalanceWallet.setText(Config.getUserWallet());
                    }
                } else if (userInfoData.status.equals("Error")) {
                    Util.showOKSnakBar(llMyWalletMain, userInfoData.errorMessage);
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
                llMyWalletMain.addView(Util.getViewInternetNotFound(getActivity(), llMyWalletMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addMainView();
                        getMyWalletDetails();
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.imgParcelIdDetailMyWalletRow) {
            MyWalletData myWalletData = (MyWalletData) view.getTag();
            dialogHeader = myWalletData.MParcelID;
            getParcelDetails(myWalletData.parcelid);
        } else if (id == R.id.imgTripIdDetailMyWalletRow) {
            MyWalletData myWalletData = (MyWalletData) view.getTag();
            dialogHeader = myWalletData.MTripID;
            getParcelDetails(myWalletData.parcelid);
        } else if (id == R.id.imgWithDrawMyDetailWalletRow) {
            MyWalletData myWalletData = (MyWalletData) view.getTag();
            dialogHeader = myWalletData.withdrawID;
            withDrawId = myWalletData.withdrawID;
            withDrawStatusList();
        }
    }

    public void parcelTripDetails(ParcelDetailsData parcelDetailsData) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (getActivity()).getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.parcel_details_dialog, null);
        dialogBuilder.setView(dialog);
        TextView fromDialog = (TextView) dialog.findViewById(R.id.fromDialog);
        TextView txtToDialog = (TextView) dialog.findViewById(R.id.txtToDialog);
        TextView toWeight = (TextView) dialog.findViewById(R.id.toWeight);
        TextView txtParcelId = (TextView) dialog.findViewById(R.id.txtParcelId);
        fromDialog.setText(parcelDetailsData.source);
        txtToDialog.setText(parcelDetailsData.destination);
        toWeight.setText(parcelDetailsData.weight);
        txtParcelId.setText(dialogHeader);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


    public void walletDetails(WithDrawData withDrawData) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (getActivity()).getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.wallet_detail_dialog, null);
        dialogBuilder.setView(dialog);
        TextView txtBankName = (TextView) dialog.findViewById(R.id.txtBankName);
        TextView txtWithDrawId = (TextView) dialog.findViewById(R.id.txtWithDrawId);
        TextView txtBankAccNoDialog = (TextView) dialog.findViewById(R.id.txtBankAccNoDialog);
        TextView txtTransNoDialog = (TextView) dialog.findViewById(R.id.txtTransNoDialog);
        txtBankName.setText(withDrawData.bank_name);
        txtBankAccNoDialog.setText(withDrawData.acct_no);
        txtTransNoDialog.setText((withDrawData.id));
        txtWithDrawId.setText(withDrawData.withdrawID);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void withDrawStatusList() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                myWalletModel.withDrawStatusList();
            } else {
                Util.showAlertDialog(null, getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void getParcelDetails(final String parcelId) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(getActivity());
            myWalletModel.getParcelDetails(parcelId);
        } else {
            llMyWalletMain.addView(Util.getViewInternetNotFound(getActivity(), llMyWalletMain, new TryAgainInterface() {
                @Override
                public void callBack() {
                    addMainView();
                    getParcelDetails(parcelId);
                }
            }));

        }
    }

    private void addMainView() {
        llMyWalletMain.removeAllViews();
        llMyWalletMain.addView(rvTripListMyWallet);
    }
}
