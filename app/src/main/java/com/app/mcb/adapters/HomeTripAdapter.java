package com.app.mcb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.R;

/**
 * Created by Hitesh kumawat on 14-09-2016.
 */
public class HomeTripAdapter extends RecyclerView.Adapter<HomeTripAdapter.ViewHolder> {
    private View.OnClickListener onClickListener;
    private boolean isRowHeaderShow;

    public HomeTripAdapter(Context context, View.OnClickListener onClickListener, boolean isRowHeaderShow) {
        this.onClickListener = onClickListener;
        this.isRowHeaderShow = isRowHeaderShow;
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

        holder.txtViewAllStateRow.setOnClickListener(onClickListener);
        if (!isRowHeaderShow) {
            if (position == 0) {
                holder.llRowHeaderMain.setVisibility(View.VISIBLE);
                holder.txtViewAllStateRow.setVisibility(View.GONE);
            } else {
                holder.llRowHeaderMain.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewAllStateRow;
        public LinearLayout llRowHeaderMain;

        public ViewHolder(View itemView) {
            super(itemView);
            txtViewAllStateRow = (TextView) itemView.findViewById(R.id.txtViewAllStateRow);
            llRowHeaderMain = (LinearLayout) itemView.findViewById(R.id.llRowHeaderMain);

        }
    }
}
