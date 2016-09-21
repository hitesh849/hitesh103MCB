package com.app.mcb.filters;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.mcb.R;

/**
 * Created by u on 9/21/2016.
 */
public class TripFilter {
    public EditText etFromTripFilter;
    public EditText etToTripFilter;
    public EditText etTillDateTripFilter;
    public ImageView imgCalenderTripFilter;
    public LinearLayout llSearchTripFilter;

    public static TripFilter getInstance(Context context, View view, View.OnClickListener onClickListener) {
        return new TripFilter(context, view, onClickListener);
    }

    public TripFilter(Context context, View view, View.OnClickListener onClickListener) {
        init(view, onClickListener);
    }

    private void init(View view, View.OnClickListener onClickListener) {
        etFromTripFilter = (EditText) view.findViewById(R.id.etFromTripFilter);
        etToTripFilter = (EditText) view.findViewById(R.id.etToTripFilter);
        etTillDateTripFilter = (EditText) view.findViewById(R.id.etTillDateTripFilter);
        imgCalenderTripFilter = (ImageView) view.findViewById(R.id.imgCalenderTripFilter);
        llSearchTripFilter = (LinearLayout) view.findViewById(R.id.llSearchTripFilter);
        llSearchTripFilter.setOnClickListener(onClickListener);
    }

}
