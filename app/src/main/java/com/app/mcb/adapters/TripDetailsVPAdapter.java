package com.app.mcb.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.R;

/**
 * Created by u on 9/16/2016.
 */
public class TripDetailsVPAdapter extends PagerAdapter {

    private Context mContext;
    private View.OnClickListener onClickListener;
    private TextView txtTransporterId1TD;
    private TextView txtWeight1TD;
    private TextView txtFromLong1TD;
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
    private TextView txtDtaeTo2TD;
    private TextView txtTimeTo2TD;

    public TripDetailsVPAdapter(Context context, View.OnClickListener onClickListener) {
        mContext = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.trip_details_row, collection, false);
        init(layout);


        collection.addView(layout);
        return layout;
    }

    private void init(ViewGroup viewGroup) {
        llBookNoewTripDetailsRow1 = (LinearLayout) viewGroup.findViewById(R.id.llBookNoewTripDetailsRow1);
        llBookNoewTripDetailsRow2 = (LinearLayout) viewGroup.findViewById(R.id.llBookNoewTripDetailsRow2);
        txtTransporterId1TD = (TextView) viewGroup.findViewById(R.id.txtTransporterId1TD);
        txtWeight1TD = (TextView) viewGroup.findViewById(R.id.txtWeight1TD);
        txtFromLong1TD = (TextView) viewGroup.findViewById(R.id.txtFromLong1TD);
        txtToShort1TD = (TextView) viewGroup.findViewById(R.id.txtToShort1TD);
        txtToLong1TD = (TextView) viewGroup.findViewById(R.id.txtToLong1TD);
        txtDtaeTo1TD = (TextView) viewGroup.findViewById(R.id.txtDtaeTo1TD);
        llBookNoewTripDetailsRow1 = (LinearLayout) viewGroup.findViewById(R.id.llBookNoewTripDetailsRow1);
        txtTransporterId2TD = (TextView) viewGroup.findViewById(R.id.txtTransporterId2TD);
        txtWeight2TD = (TextView) viewGroup.findViewById(R.id.txtWeight2TD);
        txtFromLong2TD = (TextView) viewGroup.findViewById(R.id.txtFromLong2TD);
        txtToShort2TD = (TextView) viewGroup.findViewById(R.id.txtToShort2TD);
        txtToLong2TD = (TextView) viewGroup.findViewById(R.id.txtToLong2TD);
        txtDtaeTo2TD = (TextView) viewGroup.findViewById(R.id.txtDtaeTo2TD);
        llBookNoewTripDetailsRow2 = (LinearLayout) viewGroup.findViewById(R.id.llBookNoewTripDetailsRow2);
        llBookNoewTripDetailsRow1.setOnClickListener(onClickListener);
        llBookNoewTripDetailsRow2.setOnClickListener(onClickListener);
    }


    @Override
    public int getCount() {
        return 10;
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
