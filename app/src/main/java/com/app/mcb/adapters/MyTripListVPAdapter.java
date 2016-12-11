package com.app.mcb.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
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

import java.util.ArrayList;

/**
 * Created by Hitesh kumawat on 19-09-2016.
 */
public class MyTripListVPAdapter extends PagerAdapter {

    private Context mContext;
    private View.OnClickListener onClickListener;
    private LinearLayout llFindBookedParcel;
    //private ImageView imgViewParcelListRow;
    private TextView txtCapacityMyTripListRow;
    private TextView txtPnrMyTripListRow;
    private TextView txtTripIdMyTripListRow;
    private TextView txtFlightDetailsMyTripListRow;
    private TextView txtDestinationMyTripsListRow;
    private TextView txtSourceMyTripsListRow;
    private TextView txtSourceDateMyTripsRow;
    private TextView txtDestinationDateMyTripsRow;
    private TextView txtSourceTimeMyTripsListRow;
    private TextView txtDestinationTimeMyTripsRow;
    private TextView txtDestinationCityMyTripsListRow;
    private TextView txtSourceCityMyTripsListRow;
    private TextView txtRemainingCapacityMyTripListRow;
    private TextView txtStatusMyTripListRow;
    private LinearLayout llFindParcelsMyTripList;
    private LinearLayout llBookedParcelsMyTripList;
    private ImageView imgCancelTrip;
    private ImageView imgEditTrip;
    private ImageView imgViewTicketMyTripListRow;
    private ArrayList<MyTripsData> myTripsList;

    public MyTripListVPAdapter(Context context, View.OnClickListener onClickListener, ArrayList<MyTripsData> tripsList) {
        mContext = context;
        this.onClickListener = onClickListener;
        this.myTripsList = tripsList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        MyTripsData myTripsData = myTripsList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.my_trip_list_row, collection, false);
        init(layout, myTripsData);
        setCondition(myTripsData);
        collection.addView(layout);
        return layout;
    }

    private void setCondition(MyTripsData myTripsData) {

        if (!(Constants.ParcelIdCreated.equals(myTripsData.status) || Constants.ParcelPaymentDue.equals(myTripsData.status)))
            llBookedParcelsMyTripList.setVisibility(View.VISIBLE);
        else
            llBookedParcelsMyTripList.setVisibility(View.GONE);

        allViewVisible();
        switch (myTripsData.status) {
            case Constants.TripPending:
                llFindBookedParcel.setVisibility(View.GONE);
                break;
            case Constants.TripBooked:
                llFindParcelsMyTripList.setVisibility(View.GONE);
                break;
            case Constants.TripComplete:
                llFindBookedParcel.setVisibility(View.GONE);
                break;
            case Constants.TripDelivered:
                llFindParcelsMyTripList.setVisibility(View.GONE);
                break;
        }
    }

    private void allViewVisible() {
        llFindParcelsMyTripList.setVisibility(View.VISIBLE);
    }

    private void init(ViewGroup viewGroup, MyTripsData myTripsData) {
        // imgViewParcelListRow = (ImageView) viewGroup.findViewById(R.id.imgViewParcelListRow);
        llFindBookedParcel = (LinearLayout) viewGroup.findViewById(R.id.llFindBookedParcel);
        txtCapacityMyTripListRow = (TextView) viewGroup.findViewById(R.id.txtCapacityMyTripListRow);
        txtPnrMyTripListRow = (TextView) viewGroup.findViewById(R.id.txtPnrMyTripListRow);
        txtTripIdMyTripListRow = (TextView) viewGroup.findViewById(R.id.txtTripIdMyTripListRow);
        txtFlightDetailsMyTripListRow = (TextView) viewGroup.findViewById(R.id.txtFlightDetailsMyTripListRow);
        txtDestinationMyTripsListRow = (TextView) viewGroup.findViewById(R.id.txtDestinationMyTripsListRow);
        txtSourceMyTripsListRow = (TextView) viewGroup.findViewById(R.id.txtSourceMyTripsListRow);
        txtSourceDateMyTripsRow = (TextView) viewGroup.findViewById(R.id.txtSourceDateMyTripsRow);
        txtDestinationDateMyTripsRow = (TextView) viewGroup.findViewById(R.id.txtDestinationDateMyTripsRow);
        txtSourceTimeMyTripsListRow = (TextView) viewGroup.findViewById(R.id.txtSourceTimeMyTripsListRow);
        txtDestinationTimeMyTripsRow = (TextView) viewGroup.findViewById(R.id.txtDestinationTimeMyTripsRow);
        txtDestinationCityMyTripsListRow = (TextView) viewGroup.findViewById(R.id.txtDestinationCityMyTripsListRow);
        txtSourceCityMyTripsListRow = (TextView) viewGroup.findViewById(R.id.txtSourceCityMyTripsListRow);
        txtStatusMyTripListRow = (TextView) viewGroup.findViewById(R.id.txtStatusMyTripListRow);
        txtRemainingCapacityMyTripListRow = (TextView) viewGroup.findViewById(R.id.txtRemainingCapacityMyTripListRow);
        llFindParcelsMyTripList = (LinearLayout) viewGroup.findViewById(R.id.llFindParcelsMyTripList);
        llBookedParcelsMyTripList = (LinearLayout) viewGroup.findViewById(R.id.llBookedParcelsMyTripList);
        imgCancelTrip = (ImageView) viewGroup.findViewById(R.id.imgCancelTrip);
        imgViewTicketMyTripListRow = (ImageView) viewGroup.findViewById(R.id.imgViewTicketMyTripListRow);
        imgEditTrip = (ImageView) viewGroup.findViewById(R.id.imgEditTrip);
        imgCancelTrip.setTag(myTripsData);
        imgViewTicketMyTripListRow.setTag(myTripsData);
        imgEditTrip.setTag(myTripsData);
        imgEditTrip.setTag(myTripsData);
        llFindParcelsMyTripList.setTag(myTripsData);
        llBookedParcelsMyTripList.setTag(myTripsData);
        imgCancelTrip.setOnClickListener(onClickListener);
        imgEditTrip.setOnClickListener(onClickListener);
        llFindParcelsMyTripList.setOnClickListener(onClickListener);
        llBookedParcelsMyTripList.setOnClickListener(onClickListener);
        imgViewTicketMyTripListRow.setOnClickListener(onClickListener);
        txtCapacityMyTripListRow.setText("Total " + myTripsData.capacity + " Kg");
        txtPnrMyTripListRow.setText(myTripsData.pnr);
        txtTripIdMyTripListRow.setText(myTripsData.TripID);
        txtFlightDetailsMyTripListRow.setText(myTripsData.flight_no);
        txtDestinationMyTripsListRow.setText("(" + myTripsData.destination + ")");
        txtDestinationCityMyTripsListRow.setText(Util.getFirstName(myTripsData.destination));
        txtSourceCityMyTripsListRow.setText(Util.getFirstName(myTripsData.source));
        txtSourceMyTripsListRow.setText("(" + myTripsData.source + ")");
        txtSourceDateMyTripsRow.setText(Util.getDDMMYYYYFormat(myTripsData.dep_time, "yyyy-MM-dd HH:mm:ss"));
        txtDestinationDateMyTripsRow.setText(Util.getDDMMYYYYFormat(myTripsData.arrival_time, "yyyy-MM-dd HH:mm:ss"));
        txtSourceTimeMyTripsListRow.setText(Util.getTimeFromDateTimeFormat(myTripsData.dep_time));
        txtDestinationTimeMyTripsRow.setText(Util.getTimeFromDateTimeFormat(myTripsData.arrival_time));
        txtStatusMyTripListRow.setText(myTripsData.statusdescription);
        if (!TextUtils.isEmpty(myTripsData.awailableweight))
            txtRemainingCapacityMyTripListRow.setText(String.format(mContext.getString(R.string.remaining_capacity), myTripsData.awailableweight));

        if(TextUtils.isEmpty(myTripsData.image))
        {
            imgViewTicketMyTripListRow.setVisibility(View.GONE);
        }
    }


    @Override
    public int getCount() {
        return myTripsList.size();
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
