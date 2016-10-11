package com.app.mcb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
 * Created by u on 9/15/2016.
 */
public class TripListStateWiseAdapter extends RecyclerView.Adapter<TripListStateWiseAdapter.ViewHolder> {
    private View.OnClickListener onClickListener;
    private ArrayList<TripTransporterData> tripDatas;

    public TripListStateWiseAdapter(Context context, View.OnClickListener onClickListener, ArrayList<TripTransporterData> tripDatas) {
        this.onClickListener = onClickListener;
        this.tripDatas = tripDatas;
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
        if (position == 0) {
            holder.llRowHeaderMain.setVisibility(View.VISIBLE);
            holder.txtViewAllStateRow.setVisibility(View.GONE);
        } else {
            holder.llRowHeaderMain.setVisibility(View.GONE);
        }
        if (tripDataTransporter.id != null)
            holder.txtTripIDTripList.setText(Constants.BEGIN_WITH_TRANSPORTER_ID + tripDataTransporter.id);
        else
            holder.txtTripIDTripList.setText("");

        if (tripDataTransporter.t_id != null)
            holder.txtUserIdTripList.setText(Constants.BEGIN_WITH_USER_ID + tripDataTransporter.t_id);
        else
            holder.txtUserIdTripList.setText("");

        if (tripDataTransporter.flight_no != null)
            holder.txtFlightIdTripList.setText(tripDataTransporter.flight_no);
        else
            holder.txtFlightIdTripList.setText("");

        if (tripDataTransporter.flight_no != null)
            holder.txtArrivalTripList.setText(tripDataTransporter.flight_no);
        else
            holder.txtArrivalTripList.setText("");

        if (tripDataTransporter.arrival_time != null)
            holder.txtFlightDateTripList.setText(Util.getDateFromDateTimeFormat(tripDataTransporter.arrival_time));
        else
            holder.txtFlightIdTripList.setText("");

        if(tripDataTransporter.arrival_time != null)
            holder.txtArrivalTimeTripList.setText(Util.getTimeFromDateTimeFormat(tripDataTransporter.arrival_time));
        else
            holder.txtArrivalTimeTripList.setText("");

        if(tripDataTransporter.capacity != null)
            holder.txtWeightTripList.setText(tripDataTransporter.capacity);
        else
            holder.txtWeightTripList.setText("");

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
        }
    }
}