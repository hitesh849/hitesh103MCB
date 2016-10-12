package com.app.mcb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    public MyWalletAdapter(Context context, ArrayList<MyWalletData> myWalletTripList) {
        this.context = context;
        this.myWalletTripList = myWalletTripList;
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
        holder.txtDateMyWalletRow.setText(Util.getdd_MM_YYYYFormat(myWalletData.insertdate));
        if (!TextUtils.isEmpty(myWalletData.MTripID))
            holder.txtTripIdMyWalletRow.setText(myWalletData.MTripID);
        else
            holder.txtTripIdMyWalletRow.setText("-");

        if (!TextUtils.isEmpty(myWalletData.MParcelID))
            holder.txtParcelIdMyWalletRow.setText(myWalletData.MParcelID);
        else
            holder.txtParcelIdMyWalletRow.setText("-");
        if (!TextUtils.isEmpty(myWalletData.withdrawamount))
            holder.txtWithDrawMyWalletRow.setText(context.getResources().getString(R.string.rs) + " " + myWalletData.withdrawamount);
        else
            holder.txtWithDrawMyWalletRow.setText("-");

        if (!TextUtils.isEmpty(myWalletData.credit))
            holder.txtEarnedAmtWalletRow.setText(context.getResources().getString(R.string.rs)+ " " + myWalletData.credit);
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

        public ViewHolder(View view) {
            super(view);
            txtDateMyWalletRow = (TextView) view.findViewById(R.id.txtDateMyWalletRow);
            txtTripIdMyWalletRow = (TextView) view.findViewById(R.id.txtTripIdMyWalletRow);
            txtParcelIdMyWalletRow = (TextView) view.findViewById(R.id.txtParcelIdMyWalletRow);
            txtWithDrawMyWalletRow = (TextView) view.findViewById(R.id.txtWithDrawMyWalletRow);
            txtEarnedAmtWalletRow = (TextView) view.findViewById(R.id.txtEarnedAmtWalletRow);
            txtUsedAmtWalletRow = (TextView) view.findViewById(R.id.txtUsedAmtWalletRow);
            txtStatusWalletRow = (TextView) view.findViewById(R.id.txtStatusWalletRow);
        }
    }
}
