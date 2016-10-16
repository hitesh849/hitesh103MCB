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

import java.util.ArrayList;

/**
 * Created by Hitesh kumawat on 19-09-2016.
 */
public class CancelledTripsAdapter extends PagerAdapter {

    private Context mContext;
    private View.OnClickListener onClickListener;
    private ImageView imgViewParcelListRow;
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
    private ArrayList<MyTripsData> cancelledTripsList;

    public CancelledTripsAdapter(Context context, View.OnClickListener onClickListener, ArrayList<MyTripsData> tripsList) {
        mContext = context;
        this.onClickListener = onClickListener;
        this.cancelledTripsList = tripsList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        MyTripsData myTripsData = cancelledTripsList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.cancelled_trip_list_row, collection, false);
        init(layout, myTripsData);
        collection.addView(layout);
        return layout;
    }

    private void init(ViewGroup viewGroup, MyTripsData myTripsData) {
        imgViewParcelListRow = (ImageView) viewGroup.findViewById(R.id.imgViewParcelListRow);
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
        imgViewParcelListRow.setOnClickListener(onClickListener);
    }

    @Override
    public int getCount() {
        return cancelledTripsList.size();
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
