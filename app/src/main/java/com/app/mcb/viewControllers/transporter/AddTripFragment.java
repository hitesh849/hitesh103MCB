package com.app.mcb.viewControllers.transporter;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.AddTrip;
import com.app.mcb.dao.AirportData;
import com.app.mcb.dao.CommonResponseData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.TripData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.database.DatabaseMgr;
import com.app.mcb.model.AddTripModel;
import com.app.mcb.viewControllers.dashboardFragments.DashBoardFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;

/**
 * Created by Hitesh kumawat on 19-09-2016.
 */
public class AddTripFragment extends AbstractFragment implements View.OnClickListener {

    private AddTripModel addTripModel = new AddTripModel();
    private AutoCompleteTextView txtAutoCompleteSource;
    private AutoCompleteTextView txtAutoCompleteDestination;
    private ImageView imgUploadTicket;
    private ImageView imgCalendarDeparture;
    private ImageView imgCalendarArrival;
    private ImageView imgClockDeparture;
    private ImageView imgClockArrival;
    private TextView txtDateDeparture;
    private TextView txtTimeDeparture;
    private TextView txtTimeArrival;
    private TextView txtDateArrival;
    private TextView txtAddButton;
    private EditText etxtFlightNo;
    private EditText etxtPnrBookingRef;
    private EditText etxtCapacity;
    private EditText etxtComment;
    private LinearLayout llAddTripMain;
    private ArrayList<AirportData> airportList;
    private TripData tripData = new TripData();
    private MyTripsData myTripsData;
    private String TripMode;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_trip_fragment, container, false);
        init(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            myTripsData = (MyTripsData) bundle.getSerializable("tripData");
            if (myTripsData != null) {
                TripMode = "edit";
                setValuesInEditMode();

            }
        }

        return view;
    }

    private void setValuesInEditMode() {
        tripData.id = myTripsData.id;
        tripData.status = myTripsData.status;
        txtAutoCompleteSource.setText(myTripsData.source);
        txtAutoCompleteDestination.setText(myTripsData.destination);
        etxtFlightNo.setText(myTripsData.flight_no);
        etxtPnrBookingRef.setText(myTripsData.pnr);
        etxtCapacity.setText(myTripsData.capacity);
        etxtComment.setText(myTripsData.comment);
        txtDateDeparture.setText(Util.getDDMMYYYYFormat(myTripsData.dep_time, "yyyy-MM-dd HH:mm:ss"));
        txtDateArrival.setText(Util.getDDMMYYYYFormat(myTripsData.arrival_time, "yyyy-MM-dd HH:mm:ss"));
        txtTimeDeparture.setText(Util.getTimeFromDateTimeFormat(myTripsData.dep_time));
        txtTimeArrival.setText(Util.getTimeFromDateTimeFormat(myTripsData.arrival_time));
        txtDateDeparture.setTag(myTripsData.dep_time.split(" ")[0]);
        txtTimeDeparture.setTag(myTripsData.dep_time.split(" ")[1]);
        txtDateArrival.setTag(myTripsData.arrival_time.split(" ")[0]);
        txtTimeArrival.setTag(myTripsData.arrival_time.split(" ")[1]);
    }

    private void init(View view) {
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.add_trip));
        llAddTripMain = (LinearLayout) view.findViewById(R.id.llAddTripMain);
        txtAutoCompleteSource = (AutoCompleteTextView) view.findViewById(R.id.txtAutoCompleteSource);
        txtAutoCompleteDestination = (AutoCompleteTextView) view.findViewById(R.id.txtAutoCompleteDestination);
        etxtFlightNo = (EditText) view.findViewById(R.id.etxtFlightNo);
        etxtPnrBookingRef = (EditText) view.findViewById(R.id.etxtPnrBookingRef);
        etxtCapacity = (EditText) view.findViewById(R.id.etxtCapacity);
        etxtComment = (EditText) view.findViewById(R.id.etxtComment);
        imgUploadTicket = (ImageView) view.findViewById(R.id.imgUploadTicket);
        imgCalendarDeparture = (ImageView) view.findViewById(R.id.imgCalendarDeparture);
        imgCalendarArrival = (ImageView) view.findViewById(R.id.imgCalendarArrival);
        imgClockArrival = (ImageView) view.findViewById(R.id.imgClockArrival);
        imgClockDeparture = (ImageView) view.findViewById(R.id.imgClockDeparture);
        txtDateDeparture = (TextView) view.findViewById(R.id.txtDateDeparture);
        txtTimeDeparture = (TextView) view.findViewById(R.id.txtTimeDeparture);
        txtTimeArrival = (TextView) view.findViewById(R.id.txtTimeArrival);
        txtDateArrival = (TextView) view.findViewById(R.id.txtDateArrival);
        txtAddButton = (TextView) view.findViewById(R.id.txtAddButton);
        imgCalendarDeparture.setOnClickListener(this);
        imgCalendarArrival.setOnClickListener(this);
        imgClockDeparture.setOnClickListener(this);
        imgClockArrival.setOnClickListener(this);
        txtAddButton.setOnClickListener(this);
        airportList = DatabaseMgr.getInstance(getActivity()).getAirportList();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, airportList);
        txtAutoCompleteSource.setAdapter(arrayAdapter);
        txtAutoCompleteDestination.setAdapter(arrayAdapter);

    }

    @Override
    protected BasicModel getModel() {
        return addTripModel;
    }

    @Override
    public void update(Observable observable, Object data) {

        Util.dimissProDialog();
        if (data instanceof CommonResponseData) {
            CommonResponseData commonResponseData = (CommonResponseData) data;
            if ("success".equals(commonResponseData.status)) {
                Util.showAlertDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Util.replaceFragment(getActivity(), R.id.fmHomeContainer, new DashBoardFragment());
                    }
                }, (commonResponseData.errorMessage != null) ? commonResponseData.errorMessage : getResources().getString(R.string.trip_add_success));

            } else if ("Error".equals(commonResponseData.status)) {
                Util.showOKSnakBar(llAddTripMain, commonResponseData.errorMessage);
            }

        } else if (data instanceof AddTrip) {
            AddTrip addTripData = (AddTrip) data;
            if ("success".equals(addTripData.status)) {
                Util.showAlertDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Util.replaceFragment(getActivity(), R.id.fmHomeContainer, new DashBoardFragment());
                    }
                }, addTripData.response);

            } else if ("Error".equals(addTripData.status)) {
                Util.showOKSnakBar(llAddTripMain, (addTripData.errorMessage != null) ? addTripData.errorMessage : getResources().getString(R.string.trip_add_success));
            }

        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.imgClockDeparture) {
            timePicker(txtTimeDeparture);
        } else if (id == R.id.imgClockArrival) {
            timePicker(txtTimeArrival);
        } else if (id == R.id.imgCalendarDeparture) {
            showCalendar(txtDateDeparture);
        } else if (id == R.id.imgCalendarArrival) {
            showCalendar(txtDateArrival);
        } else if (id == R.id.txtAddButton) {

            tripData.source = txtAutoCompleteSource.getText().toString();
            tripData.destination = txtAutoCompleteDestination.getText().toString();
            tripData.flight_no = etxtFlightNo.getText().toString();
            tripData.pnr = etxtPnrBookingRef.getText().toString();
            tripData.capacity = etxtCapacity.getText().toString();
            tripData.comment = etxtComment.getText().toString();

            tripData.dep_time = ((String) txtDateDeparture.getTag()) + " " + txtTimeDeparture.getTag();
            tripData.arrival_time = ((String) txtDateArrival.getTag()) + " " + txtTimeArrival.getTag();
            if (validation(tripData)) {
                if ("edit".equals(TripMode)) {
                    updateTrip(tripData);
                } else {
                    addTrip(tripData);
                }
            }
        }
    }

    private boolean validation(TripData tripData) {

        if (TextUtils.isEmpty(tripData.source)) {
            txtAutoCompleteSource.setError(getResources().getString(R.string.can_not_be_empty));
            txtAutoCompleteSource.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(tripData.destination)) {
            txtAutoCompleteDestination.setError(getResources().getString(R.string.can_not_be_empty));
            txtAutoCompleteDestination.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(((String) txtDateDeparture.getTag()))) {
            Util.showSnakBar(llAddTripMain, getResources().getString(R.string.departure_date_can_not_empty));
            return false;
        }

        if (TextUtils.isEmpty(((String) txtTimeDeparture.getTag()))) {
            Util.showSnakBar(llAddTripMain, getResources().getString(R.string.departure_time_can_not_empty));
            return false;
        }

        if (TextUtils.isEmpty(((String) txtDateArrival.getTag()))) {
            Util.showSnakBar(llAddTripMain, getResources().getString(R.string.arrival_date_can_not_empty));
            return false;
        }

        if (TextUtils.isEmpty(((String) txtTimeArrival.getTag()))) {
            Util.showSnakBar(llAddTripMain, getResources().getString(R.string.arrival_time_can_not_empty));
            return false;
        }

        if (TextUtils.isEmpty(tripData.flight_no)) {
            etxtFlightNo.setError(getResources().getString(R.string.can_not_be_empty));
            etxtFlightNo.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(tripData.pnr)) {
            etxtPnrBookingRef.setError(getResources().getString(R.string.can_not_be_empty));
            etxtPnrBookingRef.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(tripData.capacity)) {
            etxtCapacity.setError(getResources().getString(R.string.can_not_be_empty));
            etxtCapacity.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(tripData.comment)) {
            etxtComment.setError(getResources().getString(R.string.can_not_be_empty));
            etxtComment.requestFocus();
            return false;
        }

        return true;
    }

    private void timePicker(final TextView textView) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        final TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                textView.setTag(selectedHour + ":" + selectedMinute);
                boolean isPM = (selectedHour >= 12);
                textView.setText(String.format("%02d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, selectedMinute, isPM ? "PM" : "AM"));
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void addTrip(TripData tripData) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                addTripModel.addTrip(tripData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateTrip(TripData tripData) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                addTripModel.updateTrip(tripData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showCalendar(final TextView textView) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                                             @Override
                                                                             public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                                                                                 textView.setTag(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                                                                 textView.setText(Util.getDDMMYYYYFormat(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year, "dd/MM/yyyy"));
                                                                                 //AddParcelFragment.this.parcelDetailsData.till_date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                                                                             }
                                                                         }
                , mYear, mMonth, mDay
        );
        datePickerDialog.setAccentColor(getActivity().getResources().getColor(R.color.primary_blue));
        datePickerDialog.show(((AbstractFragmentActivity) getActivity()).getFragmentManager(), "DatePickerDialog");
        ;
    }
}
