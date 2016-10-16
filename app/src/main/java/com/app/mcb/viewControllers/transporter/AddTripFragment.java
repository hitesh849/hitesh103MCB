package com.app.mcb.viewControllers.transporter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.dao.AirportData;
import com.app.mcb.database.DatabaseMgr;
import com.app.mcb.model.AddTripModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.ArrayList;
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
    private TextView txtDateArrival;
    private TextView txtAddButton;
    private EditText etxtFlightNo;
    private EditText etxtPnrBookingRef;
    private EditText etxtCapacity;
    private EditText etxtComment;
    private ArrayList<AirportData> airportList;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_trip_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.add_trip));
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
    public void update(Observable observable, Object o) {

    }

    @Override
    public void onClick(View view) {

    }
}
