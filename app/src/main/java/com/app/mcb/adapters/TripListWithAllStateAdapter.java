package com.app.mcb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.sharedPreferences.Config;

import java.util.ArrayList;

/**
 * Created by u on 10/26/2016.
 */
public class TripListWithAllStateAdapter extends RecyclerView.Adapter<TripListWithAllStateAdapter.ViewHolder> {
    private View.OnClickListener onClickListener;
    private ArrayList<TripTransporterData> tripDatas;
    private Context context;

    public TripListWithAllStateAdapter(Context context, View.OnClickListener onClickListener, ArrayList<TripTransporterData> tripDatas) {
        this.onClickListener = onClickListener;
        this.tripDatas = tripDatas;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_row, null);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLayoutView.setLayoutParams(lp);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TripTransporterData tripDataTransporter = (TripTransporterData) tripDatas.get(position);
        holder.llHomeRowMain.setOnClickListener(onClickListener);
        holder.txtViewAllStateRow.setOnClickListener(onClickListener);
        holder.llHomeRowMain.setTag(tripDataTransporter);
        holder.txtViewAllStateRow.setTag(tripDataTransporter);
        holder.llRowHeaderMain.setVisibility(View.VISIBLE);
        holder.txtTripIDTripList.setText(Constants.BEGIN_WITH_TRANSPORTER_ID + tripDataTransporter.id);
        holder.txtUserIdTripList.setText(Constants.BEGIN_WITH_USER_ID + tripDataTransporter.t_id);
        holder.txtFlightIdTripList.setText(tripDataTransporter.flight_no);
        holder.txtArrivalTripList.setText(tripDataTransporter.flight_no);
        holder.txtFlightDateTripList.setText(Util.getDateFromDateTimeFormat(tripDataTransporter.arrival_time));
        holder.txtArrivalTimeTripList.setText(Util.getTimeFromDateTimeFormat(tripDataTransporter.arrival_time));
        holder.txtWeightTripList.setText(tripDataTransporter.capacity);
        holder.txtArrivalCityTripList.setText(tripDataTransporter.destination);
        holder.txtArrivalTripList.setText(Util.getFirstName(tripDataTransporter.destination));
        holder.totalCount.setText(String.format(context.getString(R.string.totalTrips),tripDataTransporter.totaltrips));
        if (!TextUtils.isEmpty(tripDataTransporter.city)) {
            holder.txtFromStateRowTripList.setText(tripDataTransporter.city);
            holder.txtFromRow.setVisibility(View.VISIBLE);
        } else {
            holder.txtFromRow.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tripDatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llHomeRowMain;
        public TextView txtViewAllStateRow;
        public LinearLayout llRowHeaderMain;
        public TextView txtFromStateRowTripList;
        public TextView txtTripIDTripList;
        public TextView txtUserIdTripList;
        public TextView txtFlightIdTripList;
        public TextView txtFlightDateTripList;
        public TextView txtArrivalTripList;
        public TextView txtArrivalTimeTripList;
        public TextView txtWeightTripList;
        public TextView txtArrivalCityTripList;
        public TextView totalCount;
        public TextView txtFromRow;

        public ViewHolder(View itemView) {
            super(itemView);
            llHomeRowMain = (LinearLayout) itemView.findViewById(R.id.llHomeRowMain);
            llRowHeaderMain = (LinearLayout) itemView.findViewById(R.id.llRowHeaderMain);
            txtViewAllStateRow = (TextView) itemView.findViewById(R.id.txtViewAllStateRow);
            txtFromStateRowTripList = (TextView) itemView.findViewById(R.id.txtFromStateRowTripList);
            txtTripIDTripList = (TextView) itemView.findViewById(R.id.txtTripIDTripList);
            txtUserIdTripList = (TextView) itemView.findViewById(R.id.txtUserIdTripList);
            txtFlightIdTripList = (TextView) itemView.findViewById(R.id.txtFlightIdTripList);
            txtFlightDateTripList = (TextView) itemView.findViewById(R.id.txtFlightDateTripList);
            txtArrivalTripList = (TextView) itemView.findViewById(R.id.txtArrivalTripList);
            txtArrivalTimeTripList = (TextView) itemView.findViewById(R.id.txtArrivalTimeTripList);
            txtWeightTripList = (TextView) itemView.findViewById(R.id.txtWeightTripList);
            txtArrivalCityTripList = (TextView) itemView.findViewById(R.id.txtArrivalCityTripList);
            totalCount = (TextView) itemView.findViewById(R.id.totalCount);
            txtFromRow = (TextView) itemView.findViewById(R.id.txtFromRow);
        }
    }
}