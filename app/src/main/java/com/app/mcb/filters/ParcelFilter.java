package com.app.mcb.filters;

import android.content.Context;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.mcb.R;

/**
 * Created by Hitesh on 11-10-2016.
 */
public class ParcelFilter implements View.OnClickListener {

    private ParcelFilterListener parcelListener;
    private Context context;
    private EditText etTillDateFilter;
    private AutoCompleteTextView autoCompParcelIdFilter;
    private ImageView imgSearchParcelFilter;
    private LinearLayout llStatusFilter;
    private ImageView imgCalenderParcelFilter;

    public static ParcelFilter addFilterView(Context context, View view, ParcelFilterListener parcelListener) {
        return new ParcelFilter(context, view, parcelListener);
    }

    public ParcelFilter(Context context, View view, ParcelFilterListener parcelListener) {
        this.parcelListener = parcelListener;
        this.context = context;
        init(view);
    }

    private void init(View view) {
        etTillDateFilter = (EditText) view.findViewById(R.id.etTillDateFilter);
        autoCompParcelIdFilter = (AutoCompleteTextView) view.findViewById(R.id.autoCompParcelIdFilter);
        imgSearchParcelFilter = (ImageView) view.findViewById(R.id.imgSearchParcelFilter);
        llStatusFilter = (LinearLayout) view.findViewById(R.id.llStatusFilter);
        imgCalenderParcelFilter = (ImageView) view.findViewById(R.id.imgCalenderParcelFilter);
        llStatusFilter.setOnClickListener(this);
        imgCalenderParcelFilter.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgCalenderParcelFilter) {

        } else if (id == R.id.llStatusFilter) {

        }
    }
}
