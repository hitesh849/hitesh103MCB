package com.app.mcb.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.mcb.R;

/**
 * Created by u on 9/16/2016.
 */
public class TipDetailsVPAdapter extends PagerAdapter {

    private Context mContext;
    private View.OnClickListener onClickListener;
    private LinearLayout llBookNoewTripDetailsRow;

    public TipDetailsVPAdapter(Context context,View.OnClickListener onClickListener ) {
        mContext = context;
        this.onClickListener=onClickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.trip_details_row, collection, false);
        llBookNoewTripDetailsRow=(LinearLayout)layout.findViewById(R.id.llBookNoewTripDetailsRow);
        llBookNoewTripDetailsRow.setOnClickListener(onClickListener);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
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

}
