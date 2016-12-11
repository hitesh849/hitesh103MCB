package com.app.mcb.viewControllers.sender;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.GenerateOrderData;
import com.app.mcb.dao.MyTripDetailsData;
import com.app.mcb.dao.OrderConfirmData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.TripData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.model.PayNowModel;
import com.app.mcb.sharedPreferences.Config;
import com.google.gson.JsonElement;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by u on 11/3/2016.
 */
public class ParcelPayNowActivity extends AbstractFragmentActivity implements View.OnClickListener, CommonListener {

    private ViewPager vpParcelList;
    private TextView txtFromShortParcelDetails;
    private TextView txtToShortParcelDetails;
    private TextView txtFromLongParcelDetails;
    private TextView txtToLongParcelDetails;
    private TextView txtToDateParcelDetails;
    private TextView txtParcelIdParcelDetails;
    private TextView txtParcelWeightParcelDetails;
    private TextView txtParcelTypeParcelDetails;
    private TextView txtSelectorTransDetails;
    private TextView txtSelectorReceiverDetails;
    private ParcelDetailsData parcelDetailsData;
    private UserInfoData userInfoData;
    private LinearLayout llTranOrReceiverContainer;
    private TextView txtReceiverIdParcelDetails;
    private TextView txtReceiverMobileParcelDetails;
    private TextView txtReceiverNameParcelDetails;
    private TextView txtReceiverEmailParcelDetails;
    private TextView txtFlightNumberParcelDetails;
    private TextView txtDepartureDateParcelDetails;
    private TextView txtDepartureTimeParcelDetails;
    private TextView txtArrivalDateParcelDetails;
    private TextView txtArrivalTimeParcelDetails;
    private TextView txtTransporterAmountToPayParcelDetails;
    private LinearLayout llFindParcelsMyParcel;
    private AppHeaderView appHeaderView;
    private LinearLayout llParcelDetailsMain;
    private LinearLayout llChkWallet;
    private TextView txtPayNowParcelPayment;
    private TextView txtConfirmParcelPayment;
    private TextView txtWalletAmount;
    private TextView txtTripId;
    private TextView txtTransporterId;
    private CheckBox chkUseWallet;
    private TripData myTripsData;
    private boolean isWalletUse;
    private String parcelStatus;
    public static final String TAG = "PayUMoney";
    private GenerateOrderData generateOrderData;
    private PayNowModel payNowModel = new PayNowModel();


    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.parcel_paynow_activity);
        init();
        parcelStatus = ((String) getIntent().getSerializableExtra("status"));
        getParcelDetails(((String) getIntent().getSerializableExtra("parcelId")));
    }


    private void setValues(ParcelDetailsData parcelDetailsData) {
        txtFromShortParcelDetails.setText(Util.getFirstName(parcelDetailsData.source));
        txtToShortParcelDetails.setText(Util.getFirstName(parcelDetailsData.destination));
        txtFromLongParcelDetails.setText(parcelDetailsData.source);
        txtToLongParcelDetails.setText(parcelDetailsData.destination);
        txtToDateParcelDetails.setText(Util.getDDMMYYYYFormat(parcelDetailsData.till_date, "yyyy-MM-dd"));
        txtParcelIdParcelDetails.setText(parcelDetailsData.ParcelID);
        txtParcelWeightParcelDetails.setText(parcelDetailsData.weight + " " + "KG");
        txtParcelTypeParcelDetails.setText(Util.getParcelType(parcelDetailsData.type));
    }

    private void init() {
        llParcelDetailsMain = (LinearLayout) findViewById(R.id.llParcelDetailsMain);
        txtFromShortParcelDetails = (TextView) findViewById(R.id.txtFromShortParcelDetails);
        txtToShortParcelDetails = (TextView) findViewById(R.id.txtToShortParcelDetails);
        txtFromLongParcelDetails = (TextView) findViewById(R.id.txtFromLongParcelDetails);
        txtToLongParcelDetails = (TextView) findViewById(R.id.txtToLongParcelDetails);
        txtToDateParcelDetails = (TextView) findViewById(R.id.txtToDateParcelDetails);
        txtParcelIdParcelDetails = (TextView) findViewById(R.id.txtParcelIdParcelDetails);
        txtParcelWeightParcelDetails = (TextView) findViewById(R.id.txtParcelWeightParcelDetails);
        txtParcelTypeParcelDetails = (TextView) findViewById(R.id.txtParcelTypeParcelDetails);
        txtSelectorTransDetails = (TextView) findViewById(R.id.txtSelectorTransDetails);
        txtSelectorReceiverDetails = (TextView) findViewById(R.id.txtSelectorReceiverDetails);
        txtPayNowParcelPayment = (TextView) findViewById(R.id.txtPayNowParcelPayment);
        txtConfirmParcelPayment = (TextView) findViewById(R.id.txtConfirmParcelPayment);
        llTranOrReceiverContainer = (LinearLayout) findViewById(R.id.llTranOrReceiverContainer);

        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        appHeaderView.txtHeaderNamecenter.setText(getString(R.string.make_payment));
        llChkWallet = (LinearLayout) findViewById(R.id.llChkWallet);
        txtWalletAmount = (TextView) findViewById(R.id.txtWalletAmount);
        chkUseWallet = (CheckBox) findViewById(R.id.chkUseWallet);
        txtSelectorTransDetails.setOnClickListener(this);
        txtSelectorReceiverDetails.setOnClickListener(this);
        txtPayNowParcelPayment.setOnClickListener(this);
        txtConfirmParcelPayment.setOnClickListener(this);

        chkUseWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isWalletUse = isChecked;
            }
        });
    }


    private void getParcelDetails(String parcelId) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            payNowModel.getMyTripDetails(parcelId);
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
        }
    }

    private void getUserDetails(String parcelId) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            payNowModel.getUserDetails(parcelId);
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
        }
    }

    private void generateOrder() {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            payNowModel.generateOrder(parcelDetailsData, myTripsData, userInfoData, isWalletUse);
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
        }
    }

    private void orderConfirm(GenerateOrderData generateOrderData) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            payNowModel.orderConfirm(generateOrderData);
        } else {
            Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
        }
    }


    @Override
    protected BasicModel getModel() {
        return payNowModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof MyTripDetailsData) {
                MyTripDetailsData parcelDetailsDataMain = (MyTripDetailsData) data;
                if ("success".equals(parcelDetailsDataMain.status)) {

                    if (Constants.ParcelPaymentDue.equals(parcelStatus) && parcelDetailsDataMain.parcel != null && parcelDetailsDataMain.parcel.size() > 0)
                        parcelDetailsData = parcelDetailsDataMain.parcel.get(0);
                    if (parcelDetailsDataMain.parcellist.size() > 0)
                        parcelDetailsData = parcelDetailsDataMain.parcellist.get(0);
                    if (parcelDetailsDataMain.parcellist != null)
                        myTripsData = parcelDetailsDataMain.response.get(0);
                    addTransPorterView();
                    setValues(parcelDetailsData);
                    getUserDetails(parcelDetailsData.recv_id);

                }
            } else if (data != null && data instanceof UserInfoData) {
                UserInfoData userInfoData = (UserInfoData) data;
                if ("success".equals(userInfoData.status)) {
                    this.userInfoData = userInfoData.response.get(0);
                    if(Float.parseFloat(Config.getUserWallet())>0)
                    {
                        chkUseWallet.setChecked(true);
                    }
                    txtWalletAmount.setText(Config.getUserWallet());
                }
            } else if (data != null && data instanceof GenerateOrderData) {
                generateOrderData = ((GenerateOrderData) data).response.get(0);
                if ("success".equals(((GenerateOrderData) data).status)) {
                    llChkWallet.setVisibility(View.GONE);
                    if (generateOrderData.Amount > 0) {
                        txtConfirmParcelPayment.setVisibility(View.VISIBLE);
                        txtPayNowParcelPayment.setVisibility(View.GONE);

                    }
                } else if ("successpayment".equals(((GenerateOrderData) data).status)) {
                    goTOHomeActivity();
                }
            } else if (data instanceof OrderConfirmData) {
                OrderConfirmData orderConfirmData = ((OrderConfirmData) data);
                if ("success".equals(orderConfirmData.status)) {
                    goTOHomeActivity();
                }

            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llParcelDetailsMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void goTOHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.txtSelectorTransDetails) {
            txtSelectorTransDetails.setTextColor(getResources().getColor(R.color.white));
            txtSelectorReceiverDetails.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtSelectorTransDetails.setBackgroundResource(R.drawable.rect_left_corners_pink_bg);
            txtSelectorReceiverDetails.setBackgroundResource(R.drawable.rect_right_corners_pink_border_grey_bg);
            addTransPorterView();

        } else if (id == R.id.txtSelectorReceiverDetails) {
            txtSelectorTransDetails.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtSelectorReceiverDetails.setTextColor(getResources().getColor(R.color.white));
            txtSelectorTransDetails.setBackgroundResource(R.drawable.rect_left_corners_pink_border_grey_bg);
            txtSelectorReceiverDetails.setBackgroundResource(R.drawable.rect_right_corners_pink_bg);
            initReceiverInfoParcelDetails(addViewInRelayout(R.layout.receiver_info_parcel_details));
        } else if (id == R.id.llFindParcelsMyParcel) {
            Intent intent = new Intent(this, MatchingTripListActivity.class);
            intent.putExtra("data", parcelDetailsData);
            startActivity(intent);
        } else if (id == R.id.txtPayNowParcelPayment) {
            generateOrder();
        } else if (id == R.id.txtConfirmParcelPayment) {
            //orderConfirm(generateOrderData);
            if (Config.getUserMobile() != null)
                makePayment();
            else
                Util.showAlertDialog(null, getString(R.string.update_mobile));
        }
    }

    private LinearLayout addViewInRelayout(int layout) {
        llTranOrReceiverContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout llLayout = (LinearLayout) inflater.inflate(layout, null, false);
        llTranOrReceiverContainer.addView(llLayout);
        return llTranOrReceiverContainer;
    }

    private void initReceiverInfoParcelDetails(View view) {
        txtReceiverIdParcelDetails = (TextView) view.findViewById(R.id.txtReceiverIdParcelDetails);
        txtReceiverMobileParcelDetails = (TextView) view.findViewById(R.id.txtReceiverMobileParcelDetails);
        txtReceiverNameParcelDetails = (TextView) view.findViewById(R.id.txtReceiverNameParcelDetails);
        txtReceiverEmailParcelDetails = (TextView) view.findViewById(R.id.txtReceiverEmailParcelDetails);
        if (userInfoData != null) {
            txtReceiverIdParcelDetails.setText(userInfoData.UserID);
            txtReceiverMobileParcelDetails.setText(userInfoData.mobile);
            txtReceiverNameParcelDetails.setText(userInfoData.name);
            txtReceiverEmailParcelDetails.setText(userInfoData.email);
            txtWalletAmount.setText(userInfoData.wallet);
        }
    }

    private void addTransPorterView() {
        initTransporterInfoParcelDetails(addViewInRelayout(R.layout.transporter_info_payment_side));
    }

    private void initFindMatchingParcel(View view) {
        llFindParcelsMyParcel = (LinearLayout) view.findViewById(R.id.llFindParcelsMyParcel);
        llFindParcelsMyParcel.setOnClickListener(this);
    }

    private void initTransporterInfoParcelDetails(View view) {
        txtFlightNumberParcelDetails = (TextView) view.findViewById(R.id.txtFlightNumberParcelDetails);
        txtDepartureDateParcelDetails = (TextView) view.findViewById(R.id.txtDepartureDateParcelDetails);
        txtDepartureTimeParcelDetails = (TextView) view.findViewById(R.id.txtDepartureTimeParcelDetails);
        txtArrivalDateParcelDetails = (TextView) view.findViewById(R.id.txtArrivalDateParcelDetails);
        txtArrivalTimeParcelDetails = (TextView) view.findViewById(R.id.txtArrivalTimeParcelDetails);
        txtTripId = (TextView) view.findViewById(R.id.txtTripId);
        txtTransporterId = (TextView) view.findViewById(R.id.txtTransporterId);
        txtTransporterAmountToPayParcelDetails = (TextView) view.findViewById(R.id.txtTransporterAmountToPayParcelDetails);
        txtFlightNumberParcelDetails.setText(myTripsData.flight_no);
        txtTransporterAmountToPayParcelDetails.setText(parcelDetailsData.payment);
        txtDepartureDateParcelDetails.setText(Util.getDDMMYYYYFormat(myTripsData.dep_time, "yyyy-MM-dd HH:mm:ss"));
        txtArrivalDateParcelDetails.setText(Util.getDDMMYYYYFormat(myTripsData.arrival_time, "yyyy-MM-dd HH:mm:ss"));
        txtDepartureTimeParcelDetails.setText(Util.getTimeFromDateTimeFormat(myTripsData.dep_time));
        txtArrivalTimeParcelDetails.setText(Util.getTimeFromDateTimeFormat(myTripsData.arrival_time));
        txtTripId.setText(myTripsData.TripID);
        txtTransporterId.setText(Constants.BEGIN_WITH_USER_ID+myTripsData.t_id);

    }

    @Override
    public void filterData(FilterData filterData) {

    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String getTxnId() {
        return ("0nf7" + System.currentTimeMillis());
    }

    private double getAmount() {

        Double amount = 10.0;

        if (isDouble(String.valueOf(amount))) {
            amount = amount;
            return amount;
        } else {
            Toast.makeText(getApplicationContext(), "Paying Default Amount ₹10", Toast.LENGTH_LONG).show();
            return amount;
        }
    }

    public void makePayment() {

        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();

        String userName = Config.getUserFirstName();
        userName = (Config.getUserLastName() != null) ? userName + Config.getUserLastName() : "";
        builder.setAmount(generateOrderData.Amount)
                .setTnxId(generateOrderData.ordernumber)
                .setPhone(Config.getUserMobile())
                .setProductName(generateOrderData.ParcelID)
                .setFirstName(Config.getUserFirstName())
                .setEmail(Config.getUserName())
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setIsDebug(true)
                .setKey("dRQuiA")
                .setMerchantId("4928174");
        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();

            /*
             server side call required to calculate hash with the help of <salt>
             <salt> is already shared along with merchant <key>
             serverCalculatedHash =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|<salt>)

             (e.g.)

             sha512(FCstqb|0nf7|10.0|product_name|piyush|piyush.jain@payu.in||||||MBgjYaFG)

             9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc

            */

        // Recommended
        calculateServerSideHashAndInitiatePayment(paymentParam);

           /*
            testing purpose

            String serverCalculatedHash="9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc";
            paymentParam.setMerchantHash(serverCalculatedHash);
            PayUmoneySdkInitilizer.startPaymentActivityForResult(this, paymentParam);
            */
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    private void calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitilizer.PaymentParam paymentParam) {

        // Replace your server side hash generator API URL
        String url = "https://test.payumoney.com/payment/op/calculateHashForTest";

        Toast.makeText(this, "Please wait... Generating hash from server ... ", Toast.LENGTH_LONG).show();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has(SdkConstants.STATUS)) {
                        String status = jsonObject.optString(SdkConstants.STATUS);
                        if (status != null || status.equals("1")) {

                            String hash = jsonObject.getString(SdkConstants.RESULT);
                            Log.i("app_activity", "Server calculated Hash :  " + hash);

                            paymentParam.setMerchantHash(hash);

                            PayUmoneySdkInitilizer.startPaymentActivityForResult(ParcelPayNowActivity.this, paymentParam);
                        } else {
                            Toast.makeText(ParcelPayNowActivity.this,
                                    jsonObject.getString(SdkConstants.RESULT),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(ParcelPayNowActivity.this,
                            ParcelPayNowActivity.this.getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ParcelPayNowActivity.this,
                            error.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paymentParam.getParams();
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {

            /*if(data != null && data.hasExtra("result")){
              String responsePayUmoney = data.getStringExtra("result");
                if(SdkHelper.checkForValidString(responsePayUmoney))
                    showDialogMessage(responsePayUmoney);
            } else {
                showDialogMessage("Unable to get Status of Payment");
            }*/


            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
                //showDialogMessage("Payment Success Id : " + paymentId);
                generateOrderData.payUMoneyId = paymentId;
               AlertDialog alertDialog= Util.showAlertDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        orderConfirm(generateOrderData);
                    }
                }, "Payment Success Id : " + paymentId);
                alertDialog.setCancelable(false);

            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "failure");
                showDialogMessage("cancelled");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                Log.i("app_activity", "failure");

                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {
                        showDialogMessage("failure");
                    }
                }
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
                Log.i(TAG, "User returned without login");
                showDialogMessage("User returned without login");
            }
        }
    }

    private void showDialogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TAG);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }
}