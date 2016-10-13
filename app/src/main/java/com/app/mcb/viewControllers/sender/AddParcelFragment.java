package com.app.mcb.viewControllers.sender;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.TripDetailsVPAdapter;
import com.app.mcb.dao.AddParcelData;
import com.app.mcb.dao.AirportData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.database.DatabaseMgr;
import com.app.mcb.model.AddParcelModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Calendar;
import java.util.List;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh kumawat on 18-09-2016.
 */
public class AddParcelFragment extends AbstractFragment implements View.OnClickListener {

    private ViewPager vpParcelList;
    private LinearLayout llReceiverContainerMain;
    private AutoCompleteTextView autoCompFromAddParcel;
    private AutoCompleteTextView autoCompToAddParcel;
    private LinearLayout llSearchReciver;
    private TextView txtSubmitAddParcel;
    private TextView txtNewReceiverAddParcel;
    private TextView txtExistingReceiverAddParcel;
    private RelativeLayout rlParcelTypeAddParcel;
    private RelativeLayout rlDeliveryTillAddParcel;
    private EditText etParcelSizeAddParcel;
    private EditText etDescriptionAddParcel;
    private EditText etDeliveryTillAddParcel;
    private ImageView imgCalenderAddParcel;
    private ImageView imgSelectParcelAddParcel;
    private AddParcelModel addParcelModel=new AddParcelModel();

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_parcel, container, false);
        init(view);
        setAdapter();
        return view;
    }

    private void init(View view) {
        llReceiverContainerMain = (LinearLayout) view.findViewById(R.id.llReceiverContainerMain);
        autoCompFromAddParcel = (AutoCompleteTextView) view.findViewById(R.id.autoCompFromAddParcel);
        autoCompToAddParcel = (AutoCompleteTextView) view.findViewById(R.id.autoCompToAddParcel);
        rlParcelTypeAddParcel = (RelativeLayout) view.findViewById(R.id.rlParcelTypeAddParcel);
        rlDeliveryTillAddParcel = (RelativeLayout) view.findViewById(R.id.rlDeliveryTillAddParcel);
        etParcelSizeAddParcel = (EditText) view.findViewById(R.id.etParcelSizeAddParcel);
        etDescriptionAddParcel = (EditText) view.findViewById(R.id.etDescriptionAddParcel);
        etDeliveryTillAddParcel = (EditText) view.findViewById(R.id.etDeliveryTillAddParcel);
        imgCalenderAddParcel = (ImageView) view.findViewById(R.id.imgCalenderAddParcel);
        imgSelectParcelAddParcel = (ImageView) view.findViewById(R.id.imgSelectParcelAddParcel);
        addViewInRelayout(R.layout.add_parcel_receiverinfo_search);
        llSearchReciver = (LinearLayout) view.findViewById(R.id.llSearchReciver);
        llSearchReciver.setOnClickListener(this);
        rlParcelTypeAddParcel.setOnClickListener(this);
        imgCalenderAddParcel.setOnClickListener(this);
        imgSelectParcelAddParcel.setOnClickListener(this);
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.add_parcels));


        autoCompFromAddParcel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                AirportData airportData = ((AirportData) parent.getItemAtPosition(position));
                autoCompFromAddParcel.setText(airportData.location);
            }
        });
        autoCompToAddParcel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                AirportData airportData = ((AirportData) parent.getItemAtPosition(position));
                autoCompToAddParcel.setText(airportData.location);
            }
        });

    }

    private void setAdapter() {
        List<AirportData> airportDatas = DatabaseMgr.getInstance(getActivity()).getAirportList();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, airportDatas);
        autoCompFromAddParcel.setDropDownWidth((int) (getActivity().getResources().getDisplayMetrics().widthPixels / 1.5));
        autoCompToAddParcel.setDropDownWidth((int) (getActivity().getResources().getDisplayMetrics().widthPixels / 1.5));
        autoCompFromAddParcel.setAdapter(arrayAdapter);
        autoCompToAddParcel.setAdapter(arrayAdapter);
    }

    private void addViewInRelayout(int layout) {
        llReceiverContainerMain.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout llLayout = (LinearLayout) inflater.inflate(layout, null, false);
        llReceiverContainerMain.addView(llLayout);

        txtNewReceiverAddParcel = (TextView) llLayout.findViewById(R.id.txtNewReceiverAddParcel);
        txtNewReceiverAddParcel.setOnClickListener(this);
        txtExistingReceiverAddParcel = (TextView) llLayout.findViewById(R.id.txtExistingReceiverAddParcel);
        txtExistingReceiverAddParcel.setOnClickListener(this);

        txtSubmitAddParcel = (TextView) llLayout.findViewById(R.id.txtSubmitAddParcel);
        if (txtSubmitAddParcel != null)
            txtSubmitAddParcel.setOnClickListener(this);
    }

    @Override
    protected BasicModel getModel() {
        return addParcelModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        Util.dimissProDialog();

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.llSearchReciver) {
            addViewInRelayout(R.layout.add_parcel_receiverinfo_submit);
        } else if (id == R.id.txtNewReceiverAddParcel) {
            addViewInRelayout(R.layout.add_parcel_receiverinfo_new_user);

        } else if (id == R.id.txtExistingReceiverAddParcel) {
            addViewInRelayout(R.layout.add_parcel_receiverinfo_search);
        } else if (id == R.id.txtSubmitAddParcel) {
            Util.replaceFragment(getActivity(), R.id.fmContainerSenderHomeMain, new ConfirmParcelFragment());
        } else if (id == R.id.imgSelectParcelAddParcel) {
            PopupMenu popup = new PopupMenu(getActivity(), rlParcelTypeAddParcel);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.parcel_type, popup.getMenu());
            popup.show();
        } else if (id == R.id.imgCalenderAddParcel) {
            showCalendar();
        }

    }

    private void showCalendar() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                                             @Override
                                                                             public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                                                                 etDeliveryTillAddParcel.setText(Util.getDDMMYYYYFormat(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year, "dd/MM/yyyy"));
                                                                             }
                                                                         }
                , mYear, mMonth, mDay
        );
        datePickerDialog.setAccentColor(getActivity().getResources().getColor(R.color.primary_blue));
        datePickerDialog.show(((AbstractFragmentActivity) getActivity()).getFragmentManager(), "DatePickerDialog");
        ;
    }
}
