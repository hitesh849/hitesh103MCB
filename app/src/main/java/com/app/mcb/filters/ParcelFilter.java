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
public class ParcelFilter implements View.OnClickListener {

    private ParcelFilterListener parcelListener;
    private Context context;
    private EditText etTillDateFilter;
    private AutoCompleteTextView autoCompParcelIdFilter;
    private ImageView imgSearchParcelFilter;
    private LinearLayout llStatusFilter;
    private ImageView imgCalenderParcelFilter;
    private DatePickerDialog datePickerDialog;
    private TextView txtStatusFilter;
    private FilterData filterData = new FilterData();

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
        txtStatusFilter = (TextView) view.findViewById(R.id.txtStatusFilter);
        llStatusFilter.setOnClickListener(this);
        imgCalenderParcelFilter.setOnClickListener(this);
        imgSearchParcelFilter.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgCalenderParcelFilter) {
            showCalendar();
        } else if (id == R.id.llStatusFilter) {
            PopupMenu popup = new PopupMenu(context, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.status_menu, popup.getMenu());
            popup.show();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.action_parcel_created:
                            txtStatusFilter.setText(context.getResources().getString(R.string.parcel_id_created));
                            break;

                        case R.id.action_payment_due:
                            txtStatusFilter.setText(context.getResources().getString(R.string.created_payment_due));
                            break;
                        case R.id.action_booking_with_tr:
                            txtStatusFilter.setText(context.getResources().getString(R.string.booking_with_tr));
                            break;
                        case R.id.action_parcel_collected:
                            txtStatusFilter.setText(context.getResources().getString(R.string.parcel_collected));
                            break;

                        case R.id.action_parcel_delivered:
                            txtStatusFilter.setText(context.getResources().getString(R.string.parcel_delivered));
                            break;
                        case R.id.action_parcel_del_completed:
                            txtStatusFilter.setText(context.getResources().getString(R.string.delivery_completed));
                            break;
                        case R.id.action_parcel_cancelled:
                            txtStatusFilter.setText(context.getResources().getString(R.string.cancelled));
                            break;
                        case R.id.action_parcel_rejected_by_tr:
                            txtStatusFilter.setText(context.getResources().getString(R.string.rejected_by_tr));
                            break;
                    }
                    return false;
                }
            });
        } else if (id == R.id.llSearchTripFilter) {

            filterData.parcelId = autoCompParcelIdFilter.getText().toString();
            filterData.parcelStatus = txtStatusFilter.getText().toString();
            parcelListener.filterData(filterData);

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
                                                                etTillDateFilter.setText(date);
                                                                filterData.tillDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                                            }
                                                        }
                , mYear, mMonth, mDay
        );
        datePickerDialog.setAccentColor(context.getResources().getColor(R.color.primary_blue));
        datePickerDialog.show(((AbstractFragmentActivity) context).getFragmentManager(), "Datepickerdialog");
        ;
    }

}
