package com.app.mcb.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelDetailsData;

import java.util.ArrayList;

/**
 * Created by Hitesh on 28-10-2016.
 */
public class MyTripDetailsVPAdapter extends PagerAdapter {

    private Context mContext;
    private View.OnClickListener onClickListener;

    private ArrayList<ParcelDetailsData> parcelList;
    private TextView txtBookingId;
    private TextView txtSenderEmailId;
    private TextView txtParcelIdMyTripParcel;
    private TextView txtPercelMyTripParcel;
    private TextView txtPrcelWeightMyTripParcel;
    private TextView txtTransporterIdMyTripList;
    private TextView txtReceiverIdMyTripParcel;
    private TextView txtStatusMyTripParcel;
    private TextView txtHeightMyTripParcel;
    private TextView txtWidthMyTripParcel;
    private TextView txLengthMyTripParcel;
    private ImageView imgChatMyTripParcel;
    private LinearLayout llBoxMyTripList;
    private TextView txtFromCityShortMyTripParcel;
    private TextView txtFromCityLongMyTripParcel;
    private TextView txtToCityShortMyTripParcel;
    private TextView txtToCityLongMyTripParcel;
    private TextView txtToDateMyTripParcel;
    private TextView txtFromDateMyTripParcel;
    private TextView txtCollectedMyTripParcel;
    private TextView txtDeliveredMyTripParcel;
    private ImageView imgBookNowMyTripParcel;
    private LinearLayout llBookingInfoMyTripParcel;
    private LinearLayout llActionMyTripParcel;
    private String currentParcelStatus;

    public MyTripDetailsVPAdapter(Context context, View.OnClickListener onClickListener, ArrayList<ParcelDetailsData> parcelList) {
        mContext = context;
        this.onClickListener = onClickListener;
        this.parcelList = parcelList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ParcelDetailsData parcelDetailsData = parcelList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.my_trip_list_parcel_row, collection, false);
        init(layout, parcelDetailsData);
        manageStatus(parcelDetailsData);
        if (Constants.BOX.equals(parcelDetailsData.type))
            llBoxMyTripList.setVisibility(View.VISIBLE);
        else
            llBoxMyTripList.setVisibility(View.GONE);
        collection.addView(layout);
        return layout;
    }

    private void manageStatus(ParcelDetailsData parcelDetailsData) {
        viewVisible();
        switch (parcelDetailsData.status) {
            case Constants.ParcelIdCreated:
                llBookingInfoMyTripParcel.setVisibility(View.GONE);
                txtCollectedMyTripParcel.setVisibility(View.GONE);
                txtDeliveredMyTripParcel.setVisibility(View.GONE);
                break;
            case Constants.ParcelPaymentDue:
                llActionMyTripParcel.setVisibility(View.GONE);
                imgChatMyTripParcel.setVisibility(View.GONE);
                break;
            case Constants.ParcelBookedWithTR:
                imgBookNowMyTripParcel.setImageResource(R.mipmap.reject);
                txtDeliveredMyTripParcel.setVisibility(View.GONE);
                break;
            case Constants.ParcelCollected:
                txtCollectedMyTripParcel.setVisibility(View.GONE);
                imgBookNowMyTripParcel.setVisibility(View.GONE);
                break;
            case Constants.ParcelDelivered:
                llActionMyTripParcel.setVisibility(View.GONE);
                imgChatMyTripParcel.setVisibility(View.GONE);
                break;
        }


    }

    private void viewVisible() {
        llBookingInfoMyTripParcel.setVisibility(View.VISIBLE);
        imgChatMyTripParcel.setVisibility(View.VISIBLE);
        llActionMyTripParcel.setVisibility(View.VISIBLE);
        llBookingInfoMyTripParcel.setVisibility(View.VISIBLE);
        imgBookNowMyTripParcel.setVisibility(View.VISIBLE);
        txtDeliveredMyTripParcel.setVisibility(View.VISIBLE);
    }

    private void init(ViewGroup viewGroup, ParcelDetailsData parcelDetailsData) {
        txtBookingId = (TextView) viewGroup.findViewById(R.id.txtBookingId);
        txtSenderEmailId = (TextView) viewGroup.findViewById(R.id.txtSenderEmailId);
        txtParcelIdMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtParcelIdMyTripParcel);
        txtPercelMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtPercelMyTripParcel);
        txtPrcelWeightMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtPrcelWeightMyTripParcel);
        txtTransporterIdMyTripList = (TextView) viewGroup.findViewById(R.id.txtTransporterIdMyTripList);
        txtReceiverIdMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtReceiverIdMyTripParcel);
        txtStatusMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtStatusMyTripParcel);
        txtHeightMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtHeightMyTripParcel);
        txtWidthMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtWidthMyTripParcel);
        txLengthMyTripParcel = (TextView) viewGroup.findViewById(R.id.txLengthMyTripParcel);
        imgChatMyTripParcel = (ImageView) viewGroup.findViewById(R.id.imgChatMyTripParcel);
        llBoxMyTripList = (LinearLayout) viewGroup.findViewById(R.id.llBoxMyTripList);
        txtFromCityShortMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtFromCityShortMyTripParcel);
        txtFromCityLongMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtFromCityLongMyTripParcel);
        txtToCityShortMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtToCityShortMyTripParcel);
        txtToCityLongMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtToCityLongMyTripParcel);
        txtToDateMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtToDateMyTripParcel);
        txtFromDateMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtFromDateMyTripParcel);
        llBookingInfoMyTripParcel = (LinearLayout) viewGroup.findViewById(R.id.llBookingInfoMyTripParcel);
        llActionMyTripParcel = (LinearLayout) viewGroup.findViewById(R.id.llActionMyTripParcel);
        imgBookNowMyTripParcel = (ImageView) viewGroup.findViewById(R.id.imgBookNowMyTripParcel);
        txtCollectedMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtCollectedMyTripParcel);
        txtDeliveredMyTripParcel = (TextView) viewGroup.findViewById(R.id.txtDeliveredMyTripParcel);
        imgChatMyTripParcel.setOnClickListener(onClickListener);
        imgBookNowMyTripParcel.setOnClickListener(onClickListener);
        txtCollectedMyTripParcel.setOnClickListener(onClickListener);
        txtDeliveredMyTripParcel.setOnClickListener(onClickListener);
        imgBookNowMyTripParcel.setTag(parcelDetailsData);
        txtCollectedMyTripParcel.setTag(parcelDetailsData);
        txtDeliveredMyTripParcel.setTag(parcelDetailsData);

        if (parcelDetailsData.BookingID != null && 0 != Integer.parseInt(parcelDetailsData.BookingID))
            txtBookingId.setText(parcelDetailsData.BookingID);
        else
            txtBookingId.setText("-");
        txtSenderEmailId.setText(parcelDetailsData.senderemail);
        txtParcelIdMyTripParcel.setText(parcelDetailsData.ParcelID);
        txtPercelMyTripParcel.setText(Util.getParcelType(parcelDetailsData.type));
        txtPrcelWeightMyTripParcel.setText(parcelDetailsData.weight);
        txtTransporterIdMyTripList.setText(parcelDetailsData.trans_id);
        txtReceiverIdMyTripParcel.setText(parcelDetailsData.recv_id);
        txtStatusMyTripParcel.setText(parcelDetailsData.statusdescription);
        txtHeightMyTripParcel.setText(parcelDetailsData.height);
        txtWidthMyTripParcel.setText(parcelDetailsData.width);
        txLengthMyTripParcel.setText(parcelDetailsData.length);
        txtFromCityShortMyTripParcel.setText(Util.getFirstName(parcelDetailsData.source));
        txtFromCityLongMyTripParcel.setText(parcelDetailsData.source);
        txtToCityShortMyTripParcel.setText(Util.getFirstName(parcelDetailsData.destination));
        txtToCityLongMyTripParcel.setText(parcelDetailsData.destination);
        txtFromDateMyTripParcel.setText(Util.getDDMMYYYYFormat(parcelDetailsData.created, "yyyy-MM-dd"));
        txtToDateMyTripParcel.setText(Util.getDDMMYYYYFormat(parcelDetailsData.till_date, "yyyy-MM-dd"));

    }

    @Override
    public int getCount() {
        return parcelList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

}
