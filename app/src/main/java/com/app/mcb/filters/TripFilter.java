package com.app.mcb.filters;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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
 * Created by Hitesh Kumawat on 9/21/2016.
 */
public class TripFilter implements View.OnClickListener {
    private AutoCompleteTextView autoCompFromTripFilter;
    private AutoCompleteTextView autoCompToTripFilter;
    private TextInputLayout textInputLaytToTripFilter;
    private EditText etTillDateTripFilter;
    private ImageView imgCalenderTripFilter;
    private LinearLayout llSearchTripFilter;
    private TextView txtToTripFilter;
    private TextView txtFromTripFilter;
    private TripListener tripListener;
    private Context context;
    private DatePickerDialog datePickerDialog;


    public static TripFilter addFilterView(Context context, View view, TripListener tripListener) {
        return new TripFilter(context, view, tripListener);
    }

    public TripFilter(Context context, View view, TripListener tripListener) {
        this.tripListener = tripListener;
        this.context = context;
        init(view);
        setAdapter();
    }

    private void init(View view) {
        autoCompFromTripFilter = (AutoCompleteTextView) view.findViewById(R.id.autoCompFromTripFilter);
        autoCompToTripFilter = (AutoCompleteTextView) view.findViewById(R.id.autoCompToTripFilter);
        textInputLaytToTripFilter = (TextInputLayout) view.findViewById(R.id.textInputLaytToTripFilter);
        etTillDateTripFilter = (EditText) view.findViewById(R.id.etTillDateTripFilter);
        imgCalenderTripFilter = (ImageView) view.findViewById(R.id.imgCalenderTripFilter);
        llSearchTripFilter = (LinearLayout) view.findViewById(R.id.llSearchTripFilter);
        txtToTripFilter = (TextView) view.findViewById(R.id.txtToTripFilter);
        txtFromTripFilter = (TextView) view.findViewById(R.id.txtFromTripFilter);
        llSearchTripFilter.setOnClickListener(this);
        imgCalenderTripFilter.setOnClickListener(this);

        autoCompFromTripFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                AirportData airportData = ((AirportData) parent.getItemAtPosition(position));
                txtFromTripFilter.setText(airportData.location);
                if (airportData.code != null && airportData.code.length() > 0)
                    autoCompFromTripFilter.setText(airportData.code);
                else {
                    String code[] = airportData.location.split(" ");
                    autoCompFromTripFilter.setText(code[0]);
                }
            }
        });

        autoCompToTripFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                AirportData airportData = ((AirportData) parent.getItemAtPosition(position));
                txtToTripFilter.setText(airportData.location);
                if (airportData.code != null && airportData.code.length() > 0)
                    autoCompToTripFilter.setText(airportData.code);
                else {
                    String code[] = airportData.location.split(" ");
                    autoCompToTripFilter.setText(code[0]);
                }
            }
        });
    }

    private void setAdapter() {
        List<AirportData> airportDatas = DatabaseMgr.getInstance(context).getAirportList();
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, airportDatas);
        autoCompFromTripFilter.setDropDownWidth((int) (context.getResources().getDisplayMetrics().widthPixels / 1.5));
        autoCompToTripFilter.setDropDownWidth((int) (context.getResources().getDisplayMetrics().widthPixels / 1.5));
        autoCompFromTripFilter.setAdapter(arrayAdapter);
        autoCompToTripFilter.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.llSearchTripFilter) {
            FilterData filterData = new FilterData();
            filterData.fromLocation = txtFromTripFilter.getText().toString();
            filterData.toLocation = txtToTripFilter.getText().toString();
            filterData.toDate = etTillDateTripFilter.getText().toString();
            filterData.fromDate = Util.getCurrentDate();
            if (filterValidation(filterData)) {
                tripListener.filterData(filterData);
            }

        } else if (id == R.id.imgCalenderTripFilter) {
            showCalander();
        }
    }

    private void showCalander() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                            @Override
                                                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                                                etTillDateTripFilter.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                                                            }
                                                        }
                , mYear, mMonth, mDay
        );
        datePickerDialog.setAccentColor(context.getResources().getColor(R.color.primary_blue));
        datePickerDialog.show(((AbstractFragmentActivity) context).getFragmentManager(), "Datepickerdialog");
        ;
    }

    private boolean filterValidation(FilterData filterData) {


        if (TextUtils.isEmpty(filterData.fromLocation)) {
            autoCompFromTripFilter.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(filterData.toLocation)) {
            autoCompToTripFilter.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(filterData.toDate)) {
            etTillDateTripFilter.setError("Can't be Empty");
            return false;
        }
        return true;
    }
}
