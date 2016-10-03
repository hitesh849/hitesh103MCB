package com.app.mcb.filters;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
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
import com.app.mcb.dao.AirportData;
import com.app.mcb.database.DatabaseMgr;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Hitesh Kumawat on 9/21/2016.
 */
public class TripFilter implements View.OnClickListener {
    private AutoCompleteTextView autoCompFromTripFilter;
    private AutoCompleteTextView autoCompToTripFilter;
    private EditText etToTripFilter;
    private EditText etTillDateTripFilter;
    private ImageView imgCalenderTripFilter;
    private LinearLayout llSearchTripFilter;
    private TextView txtToTripFilter;
    private TextView txtFromTripFilter;
    private TripListener tripListener;
    private Context context;


    public static TripFilter addFilterView(Context context, View view, TripListener tripListener) {
        return new TripFilter(context, view, tripListener);
    }

    public TripFilter(Context context, View view, TripListener tripListener) {
        this.tripListener = tripListener;
        this.context = context;
        init(view);
        setAdapter();
    }

    private void setAdapter()
    {
        List<AirportData> airportDatas=DatabaseMgr.getInstance(context).getAirportList();
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, airportDatas);
        autoCompFromTripFilter.setDropDownWidth((int)(context.getResources().getDisplayMetrics().widthPixels/1.5));
        autoCompToTripFilter.setDropDownWidth((int)(context.getResources().getDisplayMetrics().widthPixels/1.5));
        autoCompFromTripFilter.setAdapter(arrayAdapter);
        autoCompToTripFilter.setAdapter(arrayAdapter);
    }

    private void init(View view) {
        autoCompFromTripFilter = (AutoCompleteTextView) view.findViewById(R.id.autoCompFromTripFilter);
        autoCompToTripFilter = (AutoCompleteTextView) view.findViewById(R.id.autoCompToTripFilter);
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
                if(airportData.code!=null && airportData.code.length()>0)
                    autoCompFromTripFilter.setText(airportData.code);
                else
                {
                    String code[]=airportData.location.split(" ");
                    autoCompFromTripFilter.setText(code[0]);
                }
            }
        });

        autoCompToTripFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                AirportData airportData = ((AirportData) parent.getItemAtPosition(position));
                txtToTripFilter.setText(airportData.location);
                if(airportData.code!=null && airportData.code.length()>0)
                    autoCompToTripFilter.setText(airportData.code);
                else
                {
                    String code[]=airportData.location.split(" ");
                    autoCompToTripFilter.setText(code[0]);
                }
            }
        });
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
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog=DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                System.out.println("");
            }
        }
        ,mYear,mMonth,mDay
        );
        datePickerDialog.setAccentColor(context.getResources().getColor(R.color.primary_blue));
        datePickerDialog.show(((AbstractFragmentActivity) context).getFragmentManager(), "Datepickerdialog");;
    }

    private boolean filterValidation()
    {
        if(TextUtils.isEmpty(autoCompFromTripFilter.getText().toString()))
        {
            autoCompFromTripFilter.setError("Can't be Empty");
            return false;
        }
        if(TextUtils.isEmpty(etToTripFilter.getText().toString()))
        {
            etToTripFilter.setError("Can't be Empty");
            return false;
        }
        return true;
    }
}
