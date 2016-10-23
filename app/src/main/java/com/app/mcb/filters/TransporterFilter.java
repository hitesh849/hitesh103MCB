package com.app.mcb.filters;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.AirportData;
import com.app.mcb.dao.FilterData;
import com.app.mcb.database.DatabaseMgr;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Hitesh on 16-10-2016.
 */
public class TransporterFilter implements View.OnClickListener {

    private CommonListener commonListener;
    private Context context;
    private DatePickerDialog datePickerDialog;
    private ImageView imgToCalenderTransporterFilter;
    private AutoCompleteTextView autoCompFromTransporterFilter;
    private AutoCompleteTextView autoCompToTransporterFilter;
    private TextView txtFromDateTransporterFilter;
    private ImageView imgFromCalenderTransporterFilter;
    private TextView txtFromToTransporterFilter;
    private LinearLayout llSearchTransporterFilter;
    private FilterData filterData = new FilterData();

    public static TransporterFilter addFilterView(Context context, View view, CommonListener commonListener) {
        return new TransporterFilter(context, view, commonListener);
    }

    public TransporterFilter(Context context, View view, CommonListener commonListener) {
        this.commonListener = commonListener;
        this.context = context;
        init(view);
        setAdapter();
    }

    private void init(View view) {
        autoCompFromTransporterFilter = (AutoCompleteTextView) view.findViewById(R.id.autoCompFromTransporterFilter);
        autoCompToTransporterFilter = (AutoCompleteTextView) view.findViewById(R.id.autoCompToTransporterFilter);
        txtFromDateTransporterFilter = (TextView) view.findViewById(R.id.txtFromDateTransporterFilter);
        imgFromCalenderTransporterFilter = (ImageView) view.findViewById(R.id.imgFromCalenderTransporterFilter);
        imgToCalenderTransporterFilter = (ImageView) view.findViewById(R.id.imgToCalenderTransporterFilter);
        txtFromToTransporterFilter = (TextView) view.findViewById(R.id.txtFromToTransporterFilter);
        llSearchTransporterFilter = (LinearLayout) view.findViewById(R.id.llSearchTransporterFilter);
        imgFromCalenderTransporterFilter.setOnClickListener(this);
        imgToCalenderTransporterFilter.setOnClickListener(this);

    }

    private void setAdapter() {
        List<AirportData> airportDataList = DatabaseMgr.getInstance(context).getAirportList();
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, airportDataList);
        autoCompFromTransporterFilter.setDropDownWidth((int) (context.getResources().getDisplayMetrics().widthPixels / 1.5));
        autoCompToTransporterFilter.setDropDownWidth((int) (context.getResources().getDisplayMetrics().widthPixels / 1.5));
        autoCompFromTransporterFilter.setAdapter(arrayAdapter);
        autoCompToTransporterFilter.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgFromCalenderTransporterFilter) {
            showCalendar(txtFromDateTransporterFilter);
        } else if (id == R.id.imgToCalenderTransporterFilter) {
            showCalendar(txtFromToTransporterFilter);
        }
    }

    private void showCalendar(final TextView textView) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                            @Override
                                                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                                                textView.setText(Util.getDDMMYYYYFormat(dayOfMonth + "/" + (monthOfYear+1) + "/" + year, "dd/MM/yyyy"));
                                                                textView.setTag("");
                                                            }
                                                        }
                , mYear, mMonth, mDay
        );
        datePickerDialog.setAccentColor(context.getResources().getColor(R.color.primary_blue));
        datePickerDialog.show(((AbstractFragmentActivity) context).getFragmentManager(), "Datepickerdialog");
        ;
    }
}
