package com.app.mcb.filters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.FilterData;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Calendar;

/**
 * Created by Hitesh on 11-10-2016.
 */
public class TripFilter implements View.OnClickListener {

    private CommonListener commonListener;
    private Context context;
    private EditText etDepartureDateFilter;
    private AutoCompleteTextView autoCompTripIdFilter;
    private ImageView imgSearchTripFilter;
    private LinearLayout llStatusFilter;
    private ImageView imgCalenderTripFilter;
    private DatePickerDialog datePickerDialog;
    private TextView txtStatusFilter;
    private FilterData filterData = new FilterData();

    public static TripFilter addFilterView(Context context, View view, CommonListener commonListener) {
        return new TripFilter(context, view, commonListener);
    }

    public TripFilter(Context context, View view, CommonListener commonListener) {
        this.commonListener = commonListener;
        this.context = context;
        init(view);
    }

    private void init(View view) {
        etDepartureDateFilter = (EditText) view.findViewById(R.id.etDepartureDateFilter);
        autoCompTripIdFilter = (AutoCompleteTextView) view.findViewById(R.id.autoCompTripIdFilter);
        imgSearchTripFilter = (ImageView) view.findViewById(R.id.imgSearchTripFilter);
        llStatusFilter = (LinearLayout) view.findViewById(R.id.llStatusFilter);
        imgCalenderTripFilter = (ImageView) view.findViewById(R.id.imgCalenderTripFilter);
        txtStatusFilter = (TextView) view.findViewById(R.id.txtStatusFilter);
        llStatusFilter.setOnClickListener(this);
        imgCalenderTripFilter.setOnClickListener(this);
        imgSearchTripFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgCalenderTripFilter) {
            showCalendar();
        } else if (id == R.id.llStatusFilter) {
            PopupMenu popup = new PopupMenu(context, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.trip_status, popup.getMenu());
            popup.show();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.action_trip_pending:
                            txtStatusFilter.setText(context.getResources().getString(R.string.pending));
                            txtStatusFilter.setTag(Constants.TripPending);
                            break;

                        case R.id.action_trip_approved:
                            txtStatusFilter.setText(context.getResources().getString(R.string.approved));
                            txtStatusFilter.setTag(Constants.TripApproved);
                            break;
                        case R.id.request_sent:
                            txtStatusFilter.setText(context.getResources().getString(R.string.booking_request_sent));
                            txtStatusFilter.setTag(Constants.TripRequestSent);
                            break;
                        case R.id.action_trip_booked:
                            txtStatusFilter.setText(context.getResources().getString(R.string.booked));
                            txtStatusFilter.setTag(Constants.TripBooked);
                            break;

                        case R.id.action_trip_rejected:
                            txtStatusFilter.setText(context.getResources().getString(R.string.rejected));
                            txtStatusFilter.setTag(Constants.TripRejected);
                            break;
                        case R.id.action_trip_delivered:
                            txtStatusFilter.setText(context.getResources().getString(R.string.delivered));
                            txtStatusFilter.setTag(Constants.TripDelivered);
                            break;
                        case R.id.action_trip_complete:
                            txtStatusFilter.setText(context.getResources().getString(R.string.complete));
                            txtStatusFilter.setTag(Constants.TripComplete);
                            break;
                        case R.id.action_trip_on_hold:
                            txtStatusFilter.setText(context.getResources().getString(R.string.on_hold));
                            txtStatusFilter.setTag(Constants.TripHold);
                            break;
                    }
                    return false;
                }
            });
        } else if (id == R.id.imgSearchTripFilter) {

            filterData.tripId = autoCompTripIdFilter.getText().toString();
            filterData.tripStatus = (String) txtStatusFilter.getTag();
            commonListener.filterData(filterData);

        }
    }

    private void showCalendar() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                            @Override
                                                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                                                                String date = Util.getDDMMYYYYFormat(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year, "dd/MM/yyyy");
                                                                etDepartureDateFilter.setText(date);
                                                                filterData.departure_date = date;
                                                            }
                                                        }
                , mYear, mMonth, mDay
        );
        datePickerDialog.setAccentColor(context.getResources().getColor(R.color.primary_blue));
        datePickerDialog.show(((AbstractFragmentActivity) context).getFragmentManager(), "Datepickerdialog");
        ;
    }

}
