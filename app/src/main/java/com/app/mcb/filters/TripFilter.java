package com.app.mcb.filters;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.mcb.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Calendar;

/**
 * Created by Hitesh Kumawat on 9/21/2016.
 */
public class TripFilter implements View.OnClickListener {
    public EditText etFromTripFilter;
    public EditText etToTripFilter;
    public EditText etTillDateTripFilter;
    public ImageView imgCalenderTripFilter;
    public LinearLayout llSearchTripFilter;
    TripListener tripListener;
    Context context;

    public static TripFilter addFilterView(Context context, View view, TripListener tripListener) {
        return new TripFilter(context, view, tripListener);
    }

    public TripFilter(Context context, View view, TripListener tripListener) {
        this.tripListener = tripListener;
        this.context = context;
        init(view);
    }

    private void init(View view) {

        etFromTripFilter = (EditText) view.findViewById(R.id.etFromTripFilter);
        etToTripFilter = (EditText) view.findViewById(R.id.etToTripFilter);
        etTillDateTripFilter = (EditText) view.findViewById(R.id.etTillDateTripFilter);
        imgCalenderTripFilter = (ImageView) view.findViewById(R.id.imgCalenderTripFilter);
        llSearchTripFilter = (LinearLayout) view.findViewById(R.id.llSearchTripFilter);
        llSearchTripFilter.setOnClickListener(this);
        imgCalenderTripFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.llSearchTripFilter) {
            tripListener.filterData(null);
        }
        else if(id==R.id.imgCalenderTripFilter)
        {
            showCalander();
        }
    }

    private void showCalander()
    {
        DatePickerDialog datePickerDialog=DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                System.out.println("");
            }
        }
        ,2016,9,23
        );
     //   datePickerDialog.setStyle(R.style.DialogTheme,AlertDialog.THEME_HOLO_DARK );
        datePickerDialog.show(((AbstractFragmentActivity) context).getFragmentManager(), "Datepickerdialog");;
    }
}
