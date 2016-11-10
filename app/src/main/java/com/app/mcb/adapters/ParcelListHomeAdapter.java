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
import com.app.mcb.dao.ParcelDetailsData;

import java.util.ArrayList;

/**
 * Created by u on 10/27/2016.
 */
public class ParcelListHomeAdapter extends RecyclerView.Adapter<ParcelListHomeAdapter.ViewHolder> {

    private ArrayList<ParcelDetailsData> parcelListDatas;
    private View.OnClickListener onClickListener;
    private Context context;

    public ParcelListHomeAdapter(Context context, View.OnClickListener onClickListener, ArrayList<ParcelDetailsData> parcelListDatas) {
        this.onClickListener = onClickListener;
        this.parcelListDatas = parcelListDatas;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_row_sender, null);
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

        ParcelDetailsData parcelListData = parcelListDatas.get(position);
        holder.txtParcelIDParcelList.setText(parcelListData.ParcelID);
        holder.txtFromParcelList.setText(Util.getFirstName(parcelListData.source));
        holder.txtFromCityLongParcelList.setText(parcelListData.source);
        holder.txtToCityLongParcelList.setText(parcelListData.destination);
        holder.txtToParcelListHome.setText(Util.getFirstName(parcelListData.destination));
        holder.txtFromDateParcelListHome.setText(Util.getTimeFromDateTimeFormat(parcelListData.created));
        holder.txtToDateParcelListHome.setText(Util.getTimeFromDateTimeFormat(parcelListData.till_date));
        holder.llSenderHomeRowMain.setTag(parcelListData);
        if (!TextUtils.isEmpty(parcelListData.weight))
            holder.txtWeightParcelListHome.setText(parcelListData.weight + " " + "KG");
        else
            holder.txtWeightParcelListHome.setText("");

        if (!TextUtils.isEmpty(parcelListData.usr_id))
            holder.txtUserIdParcelListHome.setText(Constants.BEGIN_WITH_USER_ID + parcelListData.usr_id);
        else
            holder.txtUserIdParcelListHome.setText("-");

    }

    @Override
    public int getItemCount() {
        return parcelListDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llSenderHomeRowMain;
        public TextView txtParcelIDParcelList;
        public TextView txtUserIdParcelListHome;
        public TextView txtFromParcelList;
        public TextView txtFromDateParcelListHome;
        public TextView txtToParcelListHome;
        public TextView txtToDateParcelListHome;
        public TextView txtToCityLongParcelList;
        public TextView txtFromCityLongParcelList;
        public TextView txtWeightParcelListHome;

        public ViewHolder(View itemView) {
            super(itemView);
            llSenderHomeRowMain = (LinearLayout) itemView.findViewById(R.id.llSenderHomeRowMain);
            txtParcelIDParcelList = (TextView) itemView.findViewById(R.id.txtParcelIDParcelList);
            txtUserIdParcelListHome = (TextView) itemView.findViewById(R.id.txtUserIdParcelListHome);
            txtFromParcelList = (TextView) itemView.findViewById(R.id.txtFromParcelList);
            txtFromDateParcelListHome = (TextView) itemView.findViewById(R.id.txtFromDateParcelListHome);
            txtToParcelListHome = (TextView) itemView.findViewById(R.id.txtToParcelListHome);
            txtToDateParcelListHome = (TextView) itemView.findViewById(R.id.txtToDateParcelListHome);
            txtToCityLongParcelList = (TextView) itemView.findViewById(R.id.txtToCityLongParcelList);
            txtFromCityLongParcelList = (TextView) itemView.findViewById(R.id.txtFromCityLongParcelList);
            txtWeightParcelListHome = (TextView) itemView.findViewById(R.id.txtWeightParcelListHome);
            llSenderHomeRowMain.setOnClickListener(ParcelListHomeAdapter.this.onClickListener);
        }
    }
}
