package com.app.mcb.viewControllers.sender;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.AddParcelData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.model.AddParcelModel;
import com.app.mcb.viewControllers.dashboardFragments.DashBoardFragment;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by u on 9/20/2016.
 */
public class ConfirmParcelFragment extends AbstractFragment implements View.OnClickListener {

    private LinearLayout llconfirmParcelMain;
    private LinearLayout llConfirmParcel;
    private ParcelDetailsData parcelDetailsData;
    private TextView txtFromShortConfirmParcel;
    private TextView txtFromLongConfirmParcel;
    private TextView txtToShortConfirmParcel;
    private TextView txtToLongConfirmParcel;
    private TextView txtParcelTypeConfirmParcel;
    private TextView txtStatusConfirmParcel;
    private TextView txtReceiverNameConfirmParcel;
    private TextView txtReceiverMobileConfirmParcel;
    private TextView txtReceiverEmailConfirmParcel;
    private TextView txtReceiverIdConfirmParcel;
    private TextView txtPaymentConfirmParcel;
    private String parcelMode;
    private AddParcelModel addParcelModel = new AddParcelModel();

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_parcel, container, false);
        init(view);
        Bundle bundle = getArguments();
        if (bundle != null)
            parcelDetailsData = (ParcelDetailsData) bundle.getSerializable("data");
        parcelMode=(String)bundle.getString("mode");
        setValues();
        return view;
    }

    private void setValues() {
        txtFromShortConfirmParcel.setText(Util.getFirstName(parcelDetailsData.source));
        txtToShortConfirmParcel.setText(Util.getFirstName(parcelDetailsData.destination));
        txtParcelTypeConfirmParcel.setText(Util.getParcelType(parcelDetailsData.type));
        txtStatusConfirmParcel.setText(Util.getParcelStatus(Integer.parseInt(parcelDetailsData.status), getActivity()));
        txtReceiverEmailConfirmParcel.setText(parcelDetailsData.receiverInfoData.username);
        txtReceiverMobileConfirmParcel.setText(parcelDetailsData.receiverInfoData.mobile);
        txtReceiverIdConfirmParcel.setText(parcelDetailsData.receiverInfoData.UserID);
        txtPaymentConfirmParcel.setText(parcelDetailsData.price+" \u20B9");
        txtReceiverNameConfirmParcel.setText(parcelDetailsData.receiverInfoData.name + " " + parcelDetailsData.receiverInfoData.l_name);
        txtFromLongConfirmParcel.setText("(" + parcelDetailsData.source + ")");
        txtToLongConfirmParcel.setText("(" + parcelDetailsData.destination + ")");
    }

    private void init(View view) {
        llconfirmParcelMain = (LinearLayout) view.findViewById(R.id.llconfirmParcelMain);
        llConfirmParcel = (LinearLayout) view.findViewById(R.id.llConfirmParcel);
        txtFromShortConfirmParcel = (TextView) view.findViewById(R.id.txtFromShortConfirmParcel);
        txtFromLongConfirmParcel = (TextView) view.findViewById(R.id.txtFromLongConfirmParcel);
        txtToShortConfirmParcel = (TextView) view.findViewById(R.id.txtToShortConfirmParcel);
        txtToLongConfirmParcel = (TextView) view.findViewById(R.id.txtToLongConfirmParcel);
        txtParcelTypeConfirmParcel = (TextView) view.findViewById(R.id.txtParcelTypeConfirmParcel);
        txtStatusConfirmParcel = (TextView) view.findViewById(R.id.txtStatusConfirmParcel);
        txtReceiverNameConfirmParcel = (TextView) view.findViewById(R.id.txtReceiverNameConfirmParcel);
        txtReceiverMobileConfirmParcel = (TextView) view.findViewById(R.id.txtReceiverMobileConfirmParcel);
        txtReceiverEmailConfirmParcel = (TextView) view.findViewById(R.id.txtReceiverEmailConfirmParcel);
        txtReceiverIdConfirmParcel = (TextView) view.findViewById(R.id.txtReceiverIdConfirmParcel);
        txtPaymentConfirmParcel = (TextView) view.findViewById(R.id.txtPaymentConfirmParcel);
        llConfirmParcel.setOnClickListener(this);
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.confirm_parcel));
    }

    @Override
    protected BasicModel getModel() {
        return addParcelModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof AddParcelData) {
                AddParcelData addParcelData = (AddParcelData) data;
                if ("success".equals(addParcelData.status)) {

                    if(parcelMode.equals("book_now"))
                    {
                        Intent intent = new Intent(getActivity(), ParcelPayNowActivity.class);
                        intent.putExtra("parcelId", parcelDetailsData.trans_id);
                        startActivity(intent);

                    }
                    else
                    {
                        Util.showAlertDialog(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Util.replaceFragment(getActivity(), R.id.fmHomeContainer, new SenderHomeFragment());
                            }
                        }, getString(R.string.parcel_create_successfully));
                    }


                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llconfirmParcelMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.llConfirmParcel) {
            addParcel();
        }
    }

    private void addParcel() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                addParcelModel.addParcel(parcelDetailsData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}