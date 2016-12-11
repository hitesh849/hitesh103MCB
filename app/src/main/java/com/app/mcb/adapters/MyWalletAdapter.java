package com.app.mcb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.MyWalletData;

import java.util.ArrayList;

/**
 * Created by u on 9/15/2016.
 */
public class MyWalletAdapter extends RecyclerView.Adapter<MyWalletAdapter.ViewHolder> {


    private ArrayList<MyWalletData> myWalletTripList;
    private Context context;
    private View.OnClickListener onClickListener;

    public MyWalletAdapter(Context context, ArrayList<MyWalletData> myWalletTripList, View.OnClickListener onClickListener) {
        this.context = context;
        this.myWalletTripList = myWalletTripList;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_wallet_row, null);
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

        MyWalletData myWalletData = myWalletTripList.get(position);
        holder.txtDateMyWalletRow.setText(Util.getDDMMYYYYFormat(myWalletData.insertdate, "yyyy-MM-dd HH:mm:ss"));
        if (!TextUtils.isEmpty(myWalletData.MTripID)) {
            holder.txtTripIdMyWalletRow.setText(myWalletData.MTripID);
            holder.imgTripIdDetailMyWalletRow.setOnClickListener(onClickListener);
            holder.imgTripIdDetailMyWalletRow.setTag(myWalletData);
            holder.imgTripIdDetailMyWalletRow.setVisibility(View.VISIBLE);

        } else {
            holder.txtTripIdMyWalletRow.setText("-");
            holder.imgTripIdDetailMyWalletRow.setOnClickListener(null);
            holder.imgTripIdDetailMyWalletRow.setVisibility(View.INVISIBLE);
        }


        if (!TextUtils.isEmpty(myWalletData.MParcelID)) {
            holder.txtParcelIdMyWalletRow.setText(myWalletData.MParcelID);
            holder.imgParcelIdDetailMyWalletRow.setOnClickListener(onClickListener);
            holder.imgParcelIdDetailMyWalletRow.setTag(myWalletData);

            holder.imgParcelIdDetailMyWalletRow.setVisibility(View.VISIBLE);

        } else {
            holder.txtParcelIdMyWalletRow.setText("-");
            holder.imgParcelIdDetailMyWalletRow.setOnClickListener(null);
            holder.imgParcelIdDetailMyWalletRow.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(myWalletData.withdrawID)) {
            holder.txtWithDrawMyWalletRow.setText(myWalletData.withdrawID);
            holder.imgWithDrawMyDetailWalletRow.setTag(myWalletData);
            holder.imgWithDrawMyDetailWalletRow.setVisibility(View.VISIBLE);
            holder.imgWithDrawMyDetailWalletRow.setOnClickListener(onClickListener);
        } else {
            holder.txtWithDrawMyWalletRow.setText("-");
            holder.imgWithDrawMyDetailWalletRow.setOnClickListener(null);
            holder.imgWithDrawMyDetailWalletRow.setVisibility(View.INVISIBLE);
        }


        if (!TextUtils.isEmpty(myWalletData.credit))
            holder.txtEarnedAmtWalletRow.setText(context.getResources().getString(R.string.rs) + " " + myWalletData.credit);
        else
            holder.txtEarnedAmtWalletRow.setText("-");
        if (!TextUtils.isEmpty(myWalletData.debit))
            holder.txtUsedAmtWalletRow.setText(context.getResources().getString(R.string.rs) + " " + myWalletData.debit);
        else
            holder.txtUsedAmtWalletRow.setText("-");

        if ("N".equals(myWalletData.statusdescription))
            holder.txtStatusWalletRow.setText("Pending");
        else
            holder.txtStatusWalletRow.setText(myWalletData.statusdescription);

    }

    @Override
    public int getItemCount() {
        return myWalletTripList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtDateMyWalletRow;
        public TextView txtTripIdMyWalletRow;
        public TextView txtParcelIdMyWalletRow;
        public TextView txtWithDrawMyWalletRow;
        public TextView txtEarnedAmtWalletRow;
        public TextView txtUsedAmtWalletRow;
        public TextView txtStatusWalletRow;
        public ImageView imgTripIdDetailMyWalletRow;
        public ImageView imgParcelIdDetailMyWalletRow;
        public ImageView imgWithDrawMyDetailWalletRow;

        public ViewHolder(View view) {
            super(view);
            txtDateMyWalletRow = (TextView) view.findViewById(R.id.txtDateMyWalletRow);
            txtTripIdMyWalletRow = (TextView) view.findViewById(R.id.txtTripIdMyWalletRow);
            txtParcelIdMyWalletRow = (TextView) view.findViewById(R.id.txtParcelIdMyWalletRow);
            txtWithDrawMyWalletRow = (TextView) view.findViewById(R.id.txtWithDrawMyWalletRow);
            txtEarnedAmtWalletRow = (TextView) view.findViewById(R.id.txtEarnedAmtWalletRow);
            txtUsedAmtWalletRow = (TextView) view.findViewById(R.id.txtUsedAmtWalletRow);
            txtStatusWalletRow = (TextView) view.findViewById(R.id.txtStatusWalletRow);
            imgTripIdDetailMyWalletRow = (ImageView) view.findViewById(R.id.imgTripIdDetailMyWalletRow);
            imgParcelIdDetailMyWalletRow = (ImageView) view.findViewById(R.id.imgParcelIdDetailMyWalletRow);
            imgWithDrawMyDetailWalletRow = (ImageView) view.findViewById(R.id.imgWithDrawMyDetailWalletRow);
        }
    }
}
