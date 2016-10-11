package com.app.mcb.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.TripData;
import com.app.mcb.dao.TripTransporterData;

import java.util.ArrayList;

/**
 * Created by u on 9/16/2016.
 */
public class TripDetailsVPAdapter extends PagerAdapter {

    private Context mContext;
    private View.OnClickListener onClickListener;
    private TextView txtTransporterId1TD;
    private TextView txtWeight1TD;
    private TextView txtFromLong1TD;
    private LinearLayout llSecondRawTripDetal;
    private LinearLayout llBookNoewTripDetailsRow1;
    private TextView txtFromShort1TD;
    private TextView txtToShort1TD;
    private TextView txtToLong1TD;
    private TextView txtDtaeTo1TD;
    private TextView txtTimeTo1TD;
    private TextView txtTransporterId2TD;
    private TextView txtWeight2TD;
    private TextView txtFromLong2TD;
    private LinearLayout llBookNoewTripDetailsRow2;
    private TextView txtFromShort2TD;
    private TextView txtToShort2TD;
    private TextView txtToLong2TD;
    private TextView txtDateFrom1TD;
    private TextView txtDateTo1TD;
    private TextView txtDateFrom2TD;
    private TextView txtDateTo2TD;
    private TextView txtTimeFrom2TD;
    private TextView txtTimeFrom1TD;
    private TextView txtTimeTo2TD;
    private boolean isRowEvenOrNot;
    private ArrayList<TripTransporterData> tripTransporterDataList;

    public TripDetailsVPAdapter(Context context, ArrayList<TripTransporterData> tripTransporterDataList, View.OnClickListener onClickListener) {
        mContext = context;
        this.onClickListener = onClickListener;
        this.tripTransporterDataList = tripTransporterDataList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.trip_details_row, collection, false);
        init(layout, position);
        collection.addView(layout);
        return layout;
    }

    private void init(ViewGroup viewGroup, int position) {
        llSecondRawTripDetal = (LinearLayout) viewGroup.findViewById(R.id.llSecondRawTripDetal);
        llBookNoewTripDetailsRow1 = (LinearLayout) viewGroup.findViewById(R.id.llBookNoewTripDetailsRow1);
        llBookNoewTripDetailsRow2 = (LinearLayout) viewGroup.findViewById(R.id.llBookNoewTripDetailsRow2);
        txtTransporterId1TD = (TextView) viewGroup.findViewById(R.id.txtTransporterId1TD);
        txtWeight1TD = (TextView) viewGroup.findViewById(R.id.txtWeight1TD);
        txtFromLong1TD = (TextView) viewGroup.findViewById(R.id.txtFromLong1TD);
        txtToShort1TD = (TextView) viewGroup.findViewById(R.id.txtToShort1TD);
        txtToLong1TD = (TextView) viewGroup.findViewById(R.id.txtToLong1TD);
        txtDtaeTo1TD = (TextView) viewGroup.findViewById(R.id.txtDateTo1TD);
        txtTransporterId2TD = (TextView) viewGroup.findViewById(R.id.txtTransporterId2TD);
        txtWeight2TD = (TextView) viewGroup.findViewById(R.id.txtWeight2TD);
        txtFromLong2TD = (TextView) viewGroup.findViewById(R.id.txtFromLong2TD);
        txtToShort2TD = (TextView) viewGroup.findViewById(R.id.txtToShort2TD);
        txtToLong2TD = (TextView) viewGroup.findViewById(R.id.txtToLong2TD);
        txtDateFrom1TD = (TextView) viewGroup.findViewById(R.id.txtDateFrom1TD);
        txtDateTo1TD = (TextView) viewGroup.findViewById(R.id.txtDateTo1TD);
        txtDateFrom2TD = (TextView) viewGroup.findViewById(R.id.txtDateFrom2TD);
        txtDateTo2TD = (TextView) viewGroup.findViewById(R.id.txtDateTo2TD);
        txtTimeFrom2TD = (TextView) viewGroup.findViewById(R.id.txtTimeFrom2TD);
        txtTimeFrom1TD = (TextView) viewGroup.findViewById(R.id.txtTimeFrom1TD);
        txtFromShort1TD = (TextView) viewGroup.findViewById(R.id.txtFromShort1TD);
        txtTimeTo2TD = (TextView) viewGroup.findViewById(R.id.txtTimeTo2TD);
        txtTimeTo1TD = (TextView) viewGroup.findViewById(R.id.txtTimeTo1TD);
        txtFromShort2TD = (TextView) viewGroup.findViewById(R.id.txtFromShort2TD);
        llBookNoewTripDetailsRow1.setOnClickListener(onClickListener);
        llBookNoewTripDetailsRow2.setOnClickListener(onClickListener);

        if (tripTransporterDataList.size() / 2 == position) {
            if (!isRowEvenOrNot) {
                llSecondRawTripDetal.setVisibility(View.GONE);
            } else {
                llSecondRawTripDetal.setVisibility(View.VISIBLE);
            }
        }
        int listPosition = position * 2;
        TripTransporterData tripTransporterData1 = tripTransporterDataList.get(listPosition);

        if (tripTransporterData1.id != null)
            txtTransporterId1TD.setText(Constants.BEGIN_WITH_TRANSPORTER_ID + tripTransporterData1.id);
        else
            txtTransporterId1TD.setText("");

        if (tripTransporterData1.capacity != null)
            txtWeight1TD.setText(tripTransporterData1.capacity);
        else
            txtWeight1TD.setText("");

        if (tripTransporterData1.source != null)
            txtFromShort1TD.setText(Util.getFirstName(tripTransporterData1.source));
        else
            txtFromShort1TD.setText("");

        if (tripTransporterData1.source != null)
            txtFromLong1TD.setText("(" + tripTransporterData1.source + ")");
        else
            txtFromLong1TD.setText("");

        if (tripTransporterData1.destination != null)
            txtToShort1TD.setText(Util.getFirstName(tripTransporterData1.destination));
        else
            txtToShort1TD.setText("");

        if (tripTransporterData1.destination != null)
            txtToLong1TD.setText("(" + tripTransporterData1.destination + ")");
        else
            txtToLong1TD.setText("");

        if (tripTransporterData1.arrival_time != null)
            txtDateFrom1TD.setText(Util.getDateFromDateTimeFormat(tripTransporterData1.arrival_time));
        else
            txtDateFrom1TD.setText("");

        if (tripTransporterData1.dep_time != null)
            txtDateTo1TD.setText(Util.getDateFromDateTimeFormat(tripTransporterData1.dep_time));
        else
            txtDateTo1TD.setText("");


        if (tripTransporterData1.arrival_time != null)
            txtTimeFrom1TD.setText(Util.getTimeFromDateTimeFormat(tripTransporterData1.arrival_time));
        else
            txtTimeFrom1TD.setText("");


        if (tripTransporterData1.dep_time != null)
            txtTimeTo1TD.setText(Util.getTimeFromDateTimeFormat(tripTransporterData1.dep_time));
        else
            txtTimeTo1TD.setText("");


        if (llSecondRawTripDetal.getVisibility() == View.VISIBLE) {
            TripTransporterData tripTransporterData2 = tripTransporterDataList.get(listPosition + 1);
            if (tripTransporterData1.id != null)
                txtTransporterId2TD.setText(Constants.BEGIN_WITH_TRANSPORTER_ID + tripTransporterData2.id);
            else
                txtTransporterId1TD.setText("");

            if (tripTransporterData1.id != null)
                txtTransporterId2TD.setText(Constants.BEGIN_WITH_TRANSPORTER_ID + tripTransporterData2.id);
            else
                txtTransporterId1TD.setText("");

            if (tripTransporterData2.source != null)
                txtFromShort2TD.setText(Util.getFirstName(tripTransporterData2.source));
            else
                txtFromShort2TD.setText("");

            if (tripTransporterData2.source != null)
                txtFromLong2TD.setText("(" + tripTransporterData2.source + ")");
            else
                txtFromLong2TD.setText("");

            if (tripTransporterData2.destination != null)
                txtToShort2TD.setText(Util.getFirstName(tripTransporterData2.destination));
            else
                txtToShort2TD.setText("");

            if (tripTransporterData2.destination != null)
                txtToLong2TD.setText("(" + tripTransporterData2.destination + ")");
            else
                txtToLong2TD.setText("");

            if (tripTransporterData2.arrival_time != null)
                txtDateFrom2TD.setText(Util.getDateFromDateTimeFormat(tripTransporterData2.arrival_time));
            else
                txtDateFrom2TD.setText("");

            if (tripTransporterData2.dep_time != null)
                txtDateTo2TD.setText(Util.getDateFromDateTimeFormat(tripTransporterData2.dep_time));
            else
                txtDateTo2TD.setText("");

            if (tripTransporterData2.arrival_time != null)
                txtTimeFrom2TD.setText(Util.getTimeFromDateTimeFormat(tripTransporterData2.arrival_time));
            else
                txtTimeFrom2TD.setText("");

            if (tripTransporterData2.dep_time != null)
                txtTimeTo2TD.setText(Util.getTimeFromDateTimeFormat(tripTransporterData2.dep_time));
            else
                txtTimeTo2TD.setText("");
        }

    }


    @Override
    public int getCount() {
        int totalTrip = tripTransporterDataList.size();

        if (totalTrip % 2 == 0) {
            isRowEvenOrNot = true;
            return totalTrip / 2;
        } else {
            isRowEvenOrNot = false;
            return (totalTrip / 2) + 1;
        }

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
