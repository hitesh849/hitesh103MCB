package com.app.mcb.filters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rebus.permissionutils.AskagainCallback;
import rebus.permissionutils.FullCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.PermissionUtils;

/**
 * Created by Hitesh on 16-10-2016.
 */
public class TransporterFilter implements View.OnClickListener, FullCallback {

    private CommonListener commonListener;
    private Context context;
    private DatePickerDialog datePickerDialog;
    private ImageView imgToCalenderTransporterFilter;
    private AutoCompleteTextView autoCompFromTransporterFilter;
    private AutoCompleteTextView autoCompToTransporterFilter;
    private TextView txtFromDateTransporterFilter;
    private ImageView imgFromCalenderTransporterFilter;
    private TextView txtToDateTransporterFilter;
    private TextView txtFromAirportName;
    private TextView txtToAirportName;
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
        setDate();
        setCityAirport();
    }

    private void setCityAirport() {

        if (isCallPermissionGranted(context)) {
            String city = Util.getCityName(context);
            List<AirportData> airportDataList = DatabaseMgr.getInstance(context).getCityAirportList(city);
            if (airportDataList.size() > 0) {
                AirportData airportData = airportDataList.get(0);
                autoCompFromTransporterFilter.setText(airportData.city);
                txtFromAirportName.setText(airportData.location);
            }
        }
        else
        {
            permission(context,this);
        }

    }

    private void setDate() {
        filterData.fromDate = Util.getCurrentDate();
        filterData.toDate = Util.getNextDays(5);
        txtFromDateTransporterFilter.setText(Util.getDDMMYYYYFormat(filterData.fromDate, "yyyy/MM/dd"));
        txtToDateTransporterFilter.setText(Util.getDDMMYYYYFormat(filterData.toDate, "yyyy/MM/dd"));
        txtFromDateTransporterFilter.setTag(filterData.fromDate);
        txtToDateTransporterFilter.setTag(filterData.toDate);
    }

    private void init(View view) {
        autoCompFromTransporterFilter = (AutoCompleteTextView) view.findViewById(R.id.autoCompFromTransporterFilter);
        autoCompToTransporterFilter = (AutoCompleteTextView) view.findViewById(R.id.autoCompToTransporterFilter);
        txtFromDateTransporterFilter = (TextView) view.findViewById(R.id.txtFromDateTransporterFilter);
        imgFromCalenderTransporterFilter = (ImageView) view.findViewById(R.id.imgFromCalenderTransporterFilter);
        imgToCalenderTransporterFilter = (ImageView) view.findViewById(R.id.imgToCalenderTransporterFilter);
        txtToDateTransporterFilter = (TextView) view.findViewById(R.id.txtToDateTransporterFilter);
        txtToAirportName = (TextView) view.findViewById(R.id.txtToAirportName);
        llSearchTransporterFilter = (LinearLayout) view.findViewById(R.id.llSearchTransporterFilter);
        txtFromAirportName = (TextView) view.findViewById(R.id.txtFromAirportName);
        imgFromCalenderTransporterFilter.setOnClickListener(this);
        imgToCalenderTransporterFilter.setOnClickListener(this);
        llSearchTransporterFilter.setOnClickListener(this);
        autoCompFromTransporterFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AirportData airportData = ((AirportData) parent.getItemAtPosition(position));
                String city = (airportData.city != null) ? airportData.city : Util.getFirstName(airportData.location);
                autoCompFromTransporterFilter.setText(city);
                txtFromAirportName.setText(airportData.location);
            }
        });
        autoCompToTransporterFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AirportData airportData = ((AirportData) parent.getItemAtPosition(position));
                String city = (airportData.city != null) ? airportData.city : Util.getFirstName(airportData.location);
                autoCompToTransporterFilter.setText(city);
                txtToAirportName.setText(airportData.location);
            }
        });
    }

    public void setAdapter() {
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
            showCalendar(txtToDateTransporterFilter);
        } else if (id == R.id.llSearchTransporterFilter) {
            FilterData filterData = new FilterData();
            filterData.fromLocation = txtFromAirportName.getText().toString();
            filterData.toLocation = txtToAirportName.getText().toString();
            filterData.toDate = ((String) txtToDateTransporterFilter.getTag());
            filterData.fromDate = ((String) txtFromDateTransporterFilter.getTag());
            commonListener.filterData(filterData);
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
                                                                textView.setText(Util.getDDMMYYYYFormat(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year, "dd/MM/yyyy"));
                                                                textView.setTag((year + "/" + (monthOfYear + 1) + "/" + dayOfMonth));
                                                            }
                                                        }
                , mYear, mMonth, mDay
        );
        datePickerDialog.setAccentColor(context.getResources().getColor(R.color.primary_blue));
        datePickerDialog.show(((AbstractFragmentActivity) context).getFragmentManager(), "Datepickerdialog");
        Calendar calendar = Calendar.getInstance();
        datePickerDialog.setMinDate(calendar);
    }

    public static boolean isCallPermissionGranted(Context context) {
        if (PermissionUtils.isGranted(context, PermissionEnum.ACCESS_FINE_LOCATION) && PermissionUtils.isGranted(context, PermissionEnum.ACCESS_COARSE_LOCATION)) {
            return true;
        }

        return false;
    }

    public static void permission(final Context context, FullCallback fullCallback) {

        PermissionManager.with(context)
                .permission(PermissionEnum.ACCESS_FINE_LOCATION, PermissionEnum.ACCESS_COARSE_LOCATION)
                .askagain(true)
                .askagainCallback(new AskagainCallback() {
                    @Override
                    public void showRequestPermission(UserResponse response) {
                        Util.showDialog(context, response);
                    }
                })
                .callback(fullCallback)
                .ask();
    }

    @Override
    public void result(ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {
        List<String> msg = new ArrayList<>();
        for (PermissionEnum permissionEnum : permissionsGranted) {
            msg.add(permissionEnum.toString() + " [Granted]");
        }
        for (PermissionEnum permissionEnum : permissionsDenied) {
            msg.add(permissionEnum.toString() + " [Denied]");
        }
        for (PermissionEnum permissionEnum : permissionsDeniedForever) {
            msg.add(permissionEnum.toString() + " [DeniedForever]");
        }
        /*for (PermissionEnum permissionEnum : permissionsAsked) {
            msg.add(permissionEnum.toString() + " [Asked]");
        }*/
        String[] items = msg.toArray(new String[msg.size()]);

        ContextThemeWrapper cw = new ContextThemeWrapper(context, R.style.AlertDialogTheme);
        new AlertDialog.Builder(cw)
                .setTitle("Permission result")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
}
