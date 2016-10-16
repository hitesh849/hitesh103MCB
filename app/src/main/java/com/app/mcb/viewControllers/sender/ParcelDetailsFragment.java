package com.app.mcb.viewControllers.sender;

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
import com.app.mcb.dao.ParcelDetailsData;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by Hitesh kumawat on 18-09-2016.
 */
public class ParcelDetailsFragment extends AbstractFragment implements View.OnClickListener {

    private ViewPager vpParcelList;
    private TextView txtFromShortParcelDetails;
    private TextView txtToShortParcelDetails;
    private TextView txtFromLongParcelDetails;
    private TextView txtToLongParcelDetails;
    private TextView txtFromDateParcelDetails;
    private TextView txtToDateParcelDetails;
    private TextView txtParcelIdParcelDetails;
    private TextView txtParcelWeightParcelDetails;
    private TextView txtParcelTypeParcelDetails;
    private TextView txtSelectorTransDetails;
    private TextView txtSelectorReceiverDetails;
    private ParcelDetailsData parcelDetailsData;
    private LinearLayout llTranOrReceiverContainer;
    private TextView txtReceiverIdParcelDetails;
    private TextView txtReceiverMobileParcelDetails;
    private TextView txtReceiverNameParcelDetails;
    private TextView txtReceiverEmailParcelDetails;
    private TextView txtTransporterNameParcelDetails;
    private TextView txtTransporterEmailParcelDetails;
    private TextView txtFlightNumberParcelDetails;
    private TextView txtDepartureDateParcelDetails;
    private TextView txtDepartureTimeParcelDetails;
    private TextView txtArrivalDateParcelDetails;
    private TextView txtArrivalTimeParcelDetails;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parcel_details, container, false);
        init(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            parcelDetailsData = (ParcelDetailsData) bundle.getSerializable("data");
        }
        addTransPorterView();
        setValues();
        return view;
    }

    private void setValues() {
        txtFromShortParcelDetails.setText(Util.getFirstName(parcelDetailsData.source));
        txtToShortParcelDetails.setText(Util.getFirstName(parcelDetailsData.destination));
        txtFromLongParcelDetails.setText(parcelDetailsData.source);
        txtToLongParcelDetails.setText(parcelDetailsData.destination);
        txtFromDateParcelDetails.setText(Util.getDateFromDateTimeFormat(parcelDetailsData.created));
        txtToDateParcelDetails.setText(Util.getDDMMYYYYFormat(parcelDetailsData.till_date, "yyyy-MM-dd"));
        txtParcelIdParcelDetails.setText(parcelDetailsData.ParcelID);
        txtParcelWeightParcelDetails.setText(parcelDetailsData.weight);
        txtParcelTypeParcelDetails.setText(Util.getParcelType(parcelDetailsData.type));
    }

    private void init(View view) {
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.parcel_details));
        txtFromShortParcelDetails = (TextView) view.findViewById(R.id.txtFromShortParcelDetails);
        txtToShortParcelDetails = (TextView) view.findViewById(R.id.txtToShortParcelDetails);
        txtFromLongParcelDetails = (TextView) view.findViewById(R.id.txtFromLongParcelDetails);
        txtToLongParcelDetails = (TextView) view.findViewById(R.id.txtToLongParcelDetails);
        txtFromDateParcelDetails = (TextView) view.findViewById(R.id.txtFromDateParcelDetails);
        txtToDateParcelDetails = (TextView) view.findViewById(R.id.txtToDateParcelDetails);
        txtParcelIdParcelDetails = (TextView) view.findViewById(R.id.txtParcelIdParcelDetails);
        txtParcelWeightParcelDetails = (TextView) view.findViewById(R.id.txtParcelWeightParcelDetails);
        txtParcelTypeParcelDetails = (TextView) view.findViewById(R.id.txtParcelTypeParcelDetails);
        txtSelectorTransDetails = (TextView) view.findViewById(R.id.txtSelectorTransDetails);
        txtSelectorReceiverDetails = (TextView) view.findViewById(R.id.txtSelectorReceiverDetails);
        llTranOrReceiverContainer = (LinearLayout) view.findViewById(R.id.llTranOrReceiverContainer);
        txtSelectorTransDetails.setOnClickListener(this);
        txtSelectorReceiverDetails.setOnClickListener(this);

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
        if (id == R.id.txtSelectorTransDetails) {
            txtSelectorTransDetails.setTextColor(getResources().getColor(R.color.white));
            txtSelectorReceiverDetails.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtSelectorTransDetails.setBackgroundResource(R.drawable.rect_left_corners_pink_bg);
            txtSelectorReceiverDetails.setBackgroundResource(R.drawable.rect_right_corners_pink_border_grey_bg);
            addTransPorterView();

        } else if (id == R.id.txtSelectorReceiverDetails) {
            txtSelectorTransDetails.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtSelectorReceiverDetails.setTextColor(getResources().getColor(R.color.white));
            txtSelectorTransDetails.setBackgroundResource(R.drawable.rect_left_corners_pink_border_grey_bg);
            txtSelectorReceiverDetails.setBackgroundResource(R.drawable.rect_right_corners_pink_bg);
            initReceiverInfoParcelDetails(addViewInRelayout(R.layout.receiver_info_parcel_details));
        }
    }

    private LinearLayout addViewInRelayout(int layout) {
        llTranOrReceiverContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout llLayout = (LinearLayout) inflater.inflate(layout, null, false);
        llTranOrReceiverContainer.addView(llLayout);
        return llTranOrReceiverContainer;
    }

    private void initReceiverInfoParcelDetails(View view) {
        txtReceiverIdParcelDetails = (TextView) view.findViewById(R.id.txtReceiverIdParcelDetails);
        txtReceiverMobileParcelDetails = (TextView) view.findViewById(R.id.txtReceiverMobileParcelDetails);
        txtReceiverNameParcelDetails = (TextView) view.findViewById(R.id.txtReceiverNameParcelDetails);
        txtReceiverEmailParcelDetails = (TextView) view.findViewById(R.id.txtReceiverEmailParcelDetails);

        txtReceiverIdParcelDetails.setText(parcelDetailsData.MCBreceiverID);
        txtReceiverMobileParcelDetails.setText(parcelDetailsData.recv_mobile);
        txtReceiverNameParcelDetails.setText(parcelDetailsData.recv_name);
        txtReceiverEmailParcelDetails.setText(parcelDetailsData.receiveremail);
    }
    private void addTransPorterView()
    {
        if(parcelDetailsData.status !=null && !parcelDetailsData.status.equals("0"))
            initTransporterInfoParcelDetails(addViewInRelayout(R.layout.transporter_info_parcel_details));
        else
            addViewInRelayout(R.layout.no_transformer_found);
    }

    private void initTransporterInfoParcelDetails(View view) {
        txtTransporterNameParcelDetails = (TextView) view.findViewById(R.id.txtTransporterNameParcelDetails);
        txtTransporterEmailParcelDetails = (TextView) view.findViewById(R.id.txtTransporterEmailParcelDetails);
        txtFlightNumberParcelDetails = (TextView) view.findViewById(R.id.txtFlightNumberParcelDetails);
        txtDepartureDateParcelDetails = (TextView) view.findViewById(R.id.txtDepartureDateParcelDetails);
        txtDepartureTimeParcelDetails = (TextView) view.findViewById(R.id.txtDepartureTimeParcelDetails);
        txtArrivalDateParcelDetails = (TextView) view.findViewById(R.id.txtArrivalDateParcelDetails);
        txtArrivalTimeParcelDetails = (TextView) view.findViewById(R.id.txtArrivalTimeParcelDetails);
       // txtTransporterNameParcelDetails.setText(parcelDetailsData.tra);
        txtTransporterEmailParcelDetails.setText(parcelDetailsData.transemail);

    }
}