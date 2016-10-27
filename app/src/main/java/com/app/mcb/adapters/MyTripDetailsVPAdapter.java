package com.app.mcb.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mcb.R;
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
        collection.addView(layout);
        return layout;
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

        txtBookingId.setText(parcelDetailsData.BookingID);
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
