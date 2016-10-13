package com.app.mcb.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.mcb.R;
import com.app.mcb.dao.MyTripsData;

import java.util.ArrayList;

/**
 * Created by Hitesh kumawat on 19-09-2016.
 */
public class MyTripListVPAdapter extends PagerAdapter {

    private Context mContext;
    private View.OnClickListener onClickListener;
    private ImageView imgViewParcelListRow;
    private ArrayList<MyTripsData> myTripsList;

    public MyTripListVPAdapter(Context context, View.OnClickListener onClickListener, ArrayList<MyTripsData> tripsList) {
        mContext = context;
        this.onClickListener = onClickListener;
        this.myTripsList = tripsList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.my_trip_list_row, collection, false);
        init(layout);
        collection.addView(layout);
        return layout;
    }

    private void init(ViewGroup viewGroup) {
        imgViewParcelListRow = (ImageView) viewGroup.findViewById(R.id.imgViewParcelListRow);
        imgViewParcelListRow.setOnClickListener(onClickListener);
    }

    @Override
    public int getCount() {
        return myTripsList.size();
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
