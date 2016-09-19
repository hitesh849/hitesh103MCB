package com.app.mcb.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.mcb.R;

/**
 * Created by Hitesh kumawat on 18-09-2016.
 */
public class ParcelsListVPAdapter extends PagerAdapter {

    private Context mContext;
    private View.OnClickListener onClickListener;
    private ImageView imgViewParcelListRow;
    private ImageView imgSettingsParcelList;

    public ParcelsListVPAdapter(Context context, View.OnClickListener onClickListener) {
        mContext = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.parcels_list_row, collection, false);
        init(layout);
        collection.addView(layout);
        return layout;
    }

    private void init(ViewGroup viewGroup) {
        imgViewParcelListRow = (ImageView) viewGroup.findViewById(R.id.imgViewParcelListRow);
        imgSettingsParcelList = (ImageView) viewGroup.findViewById(R.id.imgSettingsParcelList);
        imgViewParcelListRow.setOnClickListener(onClickListener);
        imgSettingsParcelList.setOnClickListener(onClickListener);
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

