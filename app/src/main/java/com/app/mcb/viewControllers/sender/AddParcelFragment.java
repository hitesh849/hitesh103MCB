package com.app.mcb.viewControllers.sender;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.AddParcelData;
import com.app.mcb.dao.AirportData;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.SearchReceiverData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.database.DatabaseMgr;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.filters.TransporterFilter;
import com.app.mcb.model.AddParcelModel;
import com.app.mcb.viewControllers.CommonListWithStateActivity;
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
public class AddParcelFragment extends AbstractFragment implements View.OnClickListener, CommonListener {

    private ViewPager vpParcelList;
    private LinearLayout llReceiverContainerMain;
    private AutoCompleteTextView autoCompFromAddParcel;
    private AutoCompleteTextView autoCompToAddParcel;
    private LinearLayout llSearchReceiver;
    private TextView txtSubmitAddParcel;
    private TextView txtNewReceiverAddParcel;
    private TextView txtExistingReceiverAddParcel;
    private RelativeLayout rlParcelTypeAddParcel;
    private RelativeLayout rlDeliveryTillAddParcel;
    private EditText etParcelSizeAddParcel;
    private EditText etDescriptionAddParcel;
    private EditText etDeliveryTillAddParcel;
    private EditText etHeightAddParcel;
    private EditText etWidthAddParcel;
    private EditText etLengthAddParcel;
    private ImageView imgCalenderAddParcel;
    private ImageView imgSelectParcelAddParcel;
    private EditText etMobileNumberExist;
    private EditText etEmailExistUser;
    private LinearLayout llAddParcelMain;
    private TextView txtReceiverIdAddParcel;
    private TextView txtReceiverMobileAddParcel;
    private TextView txtReceiverEmailAddParcel;
    private TextView txtReceiverNameAddParcel;
    private LinearLayout llEditReceiverInfoAddParcel;
    private EditText etNewReceiverName;
    private EditText etNewReceiverEmail;
    private EditText etNewReceiverMobile;
    private EditText etNewReceiverMessage;
    private TextView txtNewReceiverSendInvitation;
    private EditText etParcelTypeAddParcelType;
    private LinearLayout llBoxAddParcel;
    private AddParcelModel addParcelModel = new AddParcelModel();
    private ParcelDetailsData parcelDetailsData = new ParcelDetailsData();
    private TripTransporterData tripTransporterData;
    private String parcelMode = "new";
    private String listType;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_parcel, container, false);
        init(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            parcelDetailsData = (ParcelDetailsData) bundle.getSerializable("data");
            tripTransporterData = (TripTransporterData) bundle.getSerializable("tripData");
            if (parcelDetailsData != null) {
                parcelMode = "edit";
                listType = bundle.getString("listType");
                setValuesInEditMode();
            }
            if (tripTransporterData != null) {
                setValueByTrip();
            }

        }
        setAdapter();
        return view;
    }


    private void init(View view) {
        llAddParcelMain = (LinearLayout) view.findViewById(R.id.llAddParcelMain);
        llReceiverContainerMain = (LinearLayout) view.findViewById(R.id.llReceiverContainerMain);
        llBoxAddParcel = (LinearLayout) view.findViewById(R.id.llBoxAddParcel);
        llBoxAddParcel.setVisibility(View.GONE);
        autoCompFromAddParcel = (AutoCompleteTextView) view.findViewById(R.id.autoCompFromAddParcel);
        autoCompToAddParcel = (AutoCompleteTextView) view.findViewById(R.id.autoCompToAddParcel);
        rlParcelTypeAddParcel = (RelativeLayout) view.findViewById(R.id.rlParcelTypeAddParcel);
        rlDeliveryTillAddParcel = (RelativeLayout) view.findViewById(R.id.rlDeliveryTillAddParcel);
        etParcelSizeAddParcel = (EditText) view.findViewById(R.id.etParcelSizeAddParcel);
        etDescriptionAddParcel = (EditText) view.findViewById(R.id.etDescriptionAddParcel);
        etDeliveryTillAddParcel = (EditText) view.findViewById(R.id.etDeliveryTillAddParcel);
        etHeightAddParcel = (EditText) view.findViewById(R.id.etHeightAddParcel);
        etWidthAddParcel = (EditText) view.findViewById(R.id.etWidthAddParcel);
        etEmailExistUser = (EditText) view.findViewById(R.id.etEmailExistUser);
        etLengthAddParcel = (EditText) view.findViewById(R.id.etLengthAddParcel);
        etParcelTypeAddParcelType = (EditText) view.findViewById(R.id.etParcelTypeAddParcelType);
        imgCalenderAddParcel = (ImageView) view.findViewById(R.id.imgCalenderAddParcel);
        imgSelectParcelAddParcel = (ImageView) view.findViewById(R.id.imgSelectParcelAddParcel);
        addViewInRelayout(R.layout.add_parcel_receiverinfo_search);
        TransporterFilter.addFilterView(getActivity(), view, this);
        initExistUserView(view);
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.add_parcels));
        rlParcelTypeAddParcel.setOnClickListener(this);
        imgCalenderAddParcel.setOnClickListener(this);
        imgSelectParcelAddParcel.setOnClickListener(this);
        setAutoCmpAdapter();

    }

    private void setValuesInEditMode() {
        autoCompFromAddParcel.setText(parcelDetailsData.source);
        autoCompToAddParcel.setText(parcelDetailsData.destination);
        etParcelTypeAddParcelType.setText(Util.getParcelType(parcelDetailsData.type));
        etParcelSizeAddParcel.setText(parcelDetailsData.weight);
        etDeliveryTillAddParcel.setText(Util.getDDMMYYYYFormat(parcelDetailsData.till_date, "yyyy-MM-dd"));
        parcelDetailsData.till_date = parcelDetailsData.till_date.replace("-", "/");
        etDescriptionAddParcel.setText(parcelDetailsData.description);
        initShowReceiverInfo(addViewInRelayout(R.layout.add_parcel_receiverinfo_submit), null, parcelDetailsData);
    }

    private void setValueByTrip() {
        if (parcelDetailsData == null)
            parcelDetailsData = new ParcelDetailsData();
        autoCompFromAddParcel.setText(tripTransporterData.source);
        autoCompToAddParcel.setText(tripTransporterData.destination);
        etDeliveryTillAddParcel.setText(Util.getDateFromDateTimeFormat(tripTransporterData.dep_time));
        parcelDetailsData.source = tripTransporterData.source;
        parcelDetailsData.destination = tripTransporterData.destination;
        parcelDetailsData.till_date = tripTransporterData.dep_time;
    }

    private void setAutoCmpAdapter() {
        autoCompFromAddParcel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                AirportData airportData = ((AirportData) parent.getItemAtPosition(position));
                autoCompFromAddParcel.setText(airportData.location);
                AddParcelFragment.this.parcelDetailsData.source = airportData.location;
            }
        });
        autoCompToAddParcel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                AirportData airportData = ((AirportData) parent.getItemAtPosition(position));
                autoCompToAddParcel.setText(airportData.location);
                AddParcelFragment.this.parcelDetailsData.destination = airportData.location;
            }
        });
    }

    private void initExistUserView(View view) {
        etMobileNumberExist = (EditText) view.findViewById(R.id.etMobileNumberExist);
        etEmailExistUser = (EditText) view.findViewById(R.id.etEmailExistUser);
        llSearchReceiver = (LinearLayout) view.findViewById(R.id.llSearchReceiver);
        llSearchReceiver.setOnClickListener(this);
    }

    private void initShowReceiverInfo(View view, UserInfoData userInfoData, ParcelDetailsData parcelDetailsData) {
        txtReceiverIdAddParcel = (TextView) view.findViewById(R.id.txtReceiverIdAddParcel);
        txtReceiverMobileAddParcel = (TextView) view.findViewById(R.id.txtReceiverMobileAddParcel);
        txtReceiverEmailAddParcel = (TextView) view.findViewById(R.id.txtReceiverEmailAddParcel);
        txtReceiverNameAddParcel = (TextView) view.findViewById(R.id.txtReceiverNameAddParcel);
        llEditReceiverInfoAddParcel = (LinearLayout) view.findViewById(R.id.llEditReceiverInfoAddParcel);
        llEditReceiverInfoAddParcel.setOnClickListener(this);

        if (userInfoData != null) {
            txtReceiverIdAddParcel.setText(userInfoData.UserID);
            txtReceiverMobileAddParcel.setText(userInfoData.mobile);
            txtReceiverEmailAddParcel.setText(userInfoData.username);
            txtReceiverNameAddParcel.setText(userInfoData.name + " " + userInfoData.l_name);
        }
        if (parcelDetailsData != null) {
            txtReceiverIdAddParcel.setText(parcelDetailsData.MCBreceiverID);
            txtReceiverMobileAddParcel.setText("");
            txtReceiverEmailAddParcel.setText(parcelDetailsData.receiveremail);
            txtReceiverNameAddParcel.setText(parcelDetailsData.recv_name);
        }
    }

    private void initNewReceiverUserView(View view) {
        etNewReceiverName = (EditText) view.findViewById(R.id.etNewReceiverName);
        etNewReceiverEmail = (EditText) view.findViewById(R.id.etNewReceiverEmail);
        etNewReceiverMobile = (EditText) view.findViewById(R.id.etNewReceiverMobile);
        etNewReceiverMessage = (EditText) view.findViewById(R.id.etNewReceiverMessage);
        txtNewReceiverSendInvitation = (TextView) view.findViewById(R.id.txtNewReceiverSendInvitation);
        txtNewReceiverSendInvitation.setOnClickListener(this);
    }

    private void setAdapter() {
        List<AirportData> airportDatas = DatabaseMgr.getInstance(getActivity()).getAirportList();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, airportDatas);
        autoCompFromAddParcel.setDropDownWidth((int) (getActivity().getResources().getDisplayMetrics().widthPixels / 1.5));
        autoCompToAddParcel.setDropDownWidth((int) (getActivity().getResources().getDisplayMetrics().widthPixels / 1.5));
        autoCompFromAddParcel.setAdapter(arrayAdapter);
        autoCompToAddParcel.setAdapter(arrayAdapter);
    }

    private LinearLayout addViewInRelayout(int layout) {
        llReceiverContainerMain.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout llLayout = (LinearLayout) inflater.inflate(layout, null, false);
        llReceiverContainerMain.addView(llLayout);
        txtNewReceiverAddParcel = (TextView) llLayout.findViewById(R.id.txtNewReceiverAddParcel);
        txtExistingReceiverAddParcel = (TextView) llLayout.findViewById(R.id.txtExistingReceiverAddParcel);
        txtSubmitAddParcel = (TextView) llLayout.findViewById(R.id.txtSubmitAddParcel);
        txtNewReceiverAddParcel.setOnClickListener(this);
        txtExistingReceiverAddParcel.setOnClickListener(this);
        if (txtSubmitAddParcel != null)
            txtSubmitAddParcel.setOnClickListener(this);
        return llLayout;
    }

    @Override
    protected BasicModel getModel() {
        return addParcelModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        Util.dimissProDialog();
        try {
            if (data != null && data instanceof SearchReceiverData) {
                SearchReceiverData userInfoData = (SearchReceiverData) data;
                if ("success".equals(userInfoData.status)) {
                    if (userInfoData.response != null) {
                        if ("E".equals(userInfoData.userType)) {
                            if (userInfoData.response.size() > 0) {
                                UserInfoData userInfo = userInfoData.response.get(0);
                                parcelDetailsData.receiverInfoData = userInfo;
                                initShowReceiverInfo(addViewInRelayout(R.layout.add_parcel_receiverinfo_submit), userInfo, null);
                            } else {
                                Util.showSnakBar(llAddParcelMain, getResources().getString(R.string.no_receiver_found));
                            }
                        } else if ("N".equals(userInfoData.userType)) {
                            if (userInfoData.response.size() > 0) {
                                UserInfoData userInfo = userInfoData.response.get(0);
                                parcelDetailsData.receiverInfoData = userInfo;
                                initShowReceiverInfo(addViewInRelayout(R.layout.add_parcel_receiverinfo_submit), userInfo, null);
                            }
                        }
                    }
                }
            } else if (data instanceof UserInfoData) {
                UserInfoData userInfoData = ((UserInfoData) data);
                if ("success".equals(userInfoData.status)) {
                    {
                        UserInfoData obj = userInfoData.response.get(0);
                        if ("Y".equals(obj.status)) {
                            calculateAmount();
                        } else if ("N".equals(obj.status)) {
                            Util.showAlertDialog(null, getString(R.string.user_authorization_on_add_parcel));
                        }
                    }
                } else {
                    Util.showAlertDialog(null, userInfoData.errorMessage);
                }

            } else if (data instanceof ParcelDetailsData) {
                ParcelDetailsData parcelDetailsData = (ParcelDetailsData) data;
                ConfirmParcelFragment confirmParcelFragment = new ConfirmParcelFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", parcelDetailsData);
                confirmParcelFragment.setArguments(bundle);
                Util.replaceFragment(getActivity(), R.id.fmContainerSenderHomeMain, confirmParcelFragment);

            } else if (data instanceof AddParcelData) {
                AddParcelData addParcelData = (AddParcelData) data;
                if ("success".equals(addParcelData.status)) {
                    ParcelsListFragment parcelsListFragment = new ParcelsListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("DATA", listType);
                    parcelsListFragment.setArguments(bundle);
                    Util.replaceFragment(getActivity(), R.id.fmContainerSenderHomeMain, parcelsListFragment);
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llAddParcelMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.llSearchReceiver) {
            String mobileNum = etMobileNumberExist.getText().toString();
            String emailId = etEmailExistUser.getText().toString();
            if (TextUtils.isEmpty(mobileNum) && TextUtils.isEmpty(emailId)) {
                Util.showSnakBar(llAddParcelMain, getResources().getString(R.string.filter_validation));
                return;
            } else {
                searchReceiver(mobileNum, emailId);
            }
        } else if (id == R.id.txtNewReceiverAddParcel) {
            initNewReceiverUserView(addViewInRelayout(R.layout.add_parcel_receiverinfo_new_user));
        } else if (id == R.id.txtExistingReceiverAddParcel) {
            initExistUserView(addViewInRelayout(R.layout.add_parcel_receiverinfo_search));
        } else if (id == R.id.txtSubmitAddParcel) {
            parcelDetailsData.weight = etParcelSizeAddParcel.getText().toString();
            parcelDetailsData.description = etDescriptionAddParcel.getText().toString();
            if ("B".equals(parcelDetailsData.type)) {
                parcelDetailsData.height = etHeightAddParcel.getText().toString();
                parcelDetailsData.width = etWidthAddParcel.getText().toString();
                parcelDetailsData.length = etLengthAddParcel.getText().toString();
            }
            if (addParcelValidation()) {
                if ("new".equals(parcelMode))
                    getUserDetails();
                else if ("edit".equals(parcelMode))
                    updateParcels();

            }
        } else if (id == R.id.imgSelectParcelAddParcel) {
            PopupMenu popup = new PopupMenu(getActivity(), rlParcelTypeAddParcel);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.parcel_type, popup.getMenu());
            popup.show();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.action_parcel_box) {
                        parcelDetailsData.type = "B";
                        etParcelTypeAddParcelType.setText(Constants.BOX);
                        llBoxAddParcel.setVisibility(View.VISIBLE);
                    } else if (id == R.id.action_parcel_packet) {
                        parcelDetailsData.type = "P";
                        etParcelTypeAddParcelType.setText(Constants.PACKET);
                        llBoxAddParcel.setVisibility(View.GONE);
                    } else if (id == R.id.action_parcel_envelope) {
                        parcelDetailsData.type = "E";
                        etParcelTypeAddParcelType.setText(Constants.ENVELOPE);
                        llBoxAddParcel.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
        } else if (id == R.id.imgCalenderAddParcel) {
            showCalendar();
        } else if (id == R.id.llEditReceiverInfoAddParcel) {
            initExistUserView(addViewInRelayout(R.layout.add_parcel_receiverinfo_search));
        } else if (id == R.id.txtNewReceiverSendInvitation) {
            UserInfoData userInfoData = new UserInfoData();
            userInfoData.name = etNewReceiverName.getText().toString();
            userInfoData.email = etNewReceiverEmail.getText().toString();
            userInfoData.mobile = etNewReceiverMobile.getText().toString();
            userInfoData.message = etNewReceiverMessage.getText().toString();
            if (newUserValidation(userInfoData)) {
                sendInvitation(userInfoData);
            }
        }

    }

    private boolean addParcelValidation() {

        if (TextUtils.isEmpty(parcelDetailsData.source)) {
            autoCompFromAddParcel.setError(getResources().getString(R.string.can_not_be_empty));
            autoCompFromAddParcel.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(parcelDetailsData.destination)) {
            autoCompToAddParcel.setError(getResources().getString(R.string.can_not_be_empty));
            autoCompFromAddParcel.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(parcelDetailsData.type)) {
            etParcelTypeAddParcelType.setError(getResources().getString(R.string.can_not_be_empty));
            etParcelTypeAddParcelType.requestFocus();
            return false;
        }

        if ("B".equals(parcelDetailsData.type)) {
            if (TextUtils.isEmpty(parcelDetailsData.height)) {
                etHeightAddParcel.setError(getResources().getString(R.string.can_not_be_empty));
                etHeightAddParcel.requestFocus();
                return false;
            }

            if (TextUtils.isEmpty(parcelDetailsData.width)) {
                etWidthAddParcel.setError(getResources().getString(R.string.can_not_be_empty));
                etWidthAddParcel.requestFocus();
                return false;
            }
            if (TextUtils.isEmpty(parcelDetailsData.length)) {
                etLengthAddParcel.setError(getResources().getString(R.string.can_not_be_empty));
                etLengthAddParcel.requestFocus();
                return false;
            }
        }
        if (TextUtils.isEmpty(parcelDetailsData.weight)) {
            etParcelSizeAddParcel.setError(getResources().getString(R.string.can_not_be_empty));
            etParcelSizeAddParcel.requestFocus();
            return false;
        }

        if (Integer.parseInt(parcelDetailsData.weight) > 30) {
            etParcelSizeAddParcel.setError(String.format(getString(R.string.max_weight), Constants.MAX_WEIGHT, "kg"));
            etParcelSizeAddParcel.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(parcelDetailsData.description)) {
            etDescriptionAddParcel.setError(getResources().getString(R.string.can_not_be_empty));
            etDescriptionAddParcel.requestFocus();
            return false;
        }
        return true;
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
                                                                                 AddParcelFragment.this.parcelDetailsData.till_date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                                                                             }
                                                                         }
                , mYear, mMonth, mDay
        );
        datePickerDialog.setAccentColor(getActivity().getResources().getColor(R.color.primary_blue));
        datePickerDialog.show(((AbstractFragmentActivity) getActivity()).getFragmentManager(), "DatePickerDialog");
        ;
    }

    private void searchReceiver(String mobile, String email) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                addParcelModel.searchReceiver(mobile, email);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendInvitation(UserInfoData userInfoData) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                addParcelModel.sendInvitation(userInfoData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void calculateAmount() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                addParcelModel.calculateAmount(parcelDetailsData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getUserDetails() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                addParcelModel.getUserDetails();
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void updateParcels() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                addParcelModel.updateParcels(parcelDetailsData);
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    boolean newUserValidation(UserInfoData userInfoData) {

        if (TextUtils.isEmpty(userInfoData.name)) {
            etNewReceiverName.setError(getResources().getString(R.string.can_not_be_empty));
            return false;
        }

        if (TextUtils.isEmpty(userInfoData.email)) {
            etNewReceiverEmail.setError(getResources().getString(R.string.can_not_be_empty));
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.mobile)) {
            etNewReceiverMobile.setError(getResources().getString(R.string.can_not_be_empty));
            return false;
        }
        if (TextUtils.isEmpty(userInfoData.message)) {
            etNewReceiverMessage.setError(getResources().getString(R.string.can_not_be_empty));
            return false;
        }
        return true;
    }

    @Override
    public void filterData(FilterData filterData) {
        filterData.type = Constants.KEY_TRANSPORTER;
        TripTransporterData tripTransporterData = new TripTransporterData();
        tripTransporterData.arrival_time = filterData.fromDate;
        tripTransporterData.dep_time = filterData.toDate;
        tripTransporterData.source = filterData.fromLocation;
        tripTransporterData.destination = filterData.toLocation;
        Intent intent = new Intent(getActivity(), CommonListWithStateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("KEY_DATA", tripTransporterData);
        intent.putExtra("KEY_BUNDLE", bundle);
        startActivity(intent);

    }
}
