package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.GenerateOrderData;
import com.app.mcb.dao.MyTripDetailsData;
import com.app.mcb.dao.OrderConfirmData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.TripData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.retrointerface.RestInterface;
import com.app.mcb.sharedPreferences.Config;
import com.google.gson.JsonElement;

import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by u on 11/3/2016.
 */
public class PayNowModel extends BasicModel {

    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);


    public void getMyTripDetails(String tripId) {
        try {
            restInterface.getMyTripDetails(tripId, new Callback<MyTripDetailsData>() {
                @Override
                public void success(MyTripDetailsData myTripDetailsData, Response response) {
                    notifyObservers(myTripDetailsData);
                }

                @Override
                public void failure(RetrofitError error) {
                    notifyObservers(error);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void getUserDetails(String userId) {
        try {

            restInterface.getUserDetails(userId, new Callback<UserInfoData>() {
                @Override
                public void success(UserInfoData userInfoData, Response response) {
                    notifyObservers(userInfoData);
                }

                @Override
                public void failure(RetrofitError error) {
                    notifyObservers(error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateOrder(ParcelDetailsData parcelDetailsData, TripData myTripsData, UserInfoData userInfoData, boolean usewalletamount) {
        try {

            HashMap<String, Object> request = new HashMap<String, Object>();
            request.put("ParcelID", parcelDetailsData.id);
            request.put("TransID", myTripsData.id);
            request.put("status", Constants.ParcelPaymentDue);
            request.put("ordernumber", "" +getTxnId());
            request.put("Amount", parcelDetailsData.payment);
            request.put("Paymentvia", "Payu Money Gateway");
            request.put("usewalletamount", usewalletamount);
            request.put("walletamount", userInfoData.wallet);
            request.put("loginuserid", Config.getUserId());
            restInterface.generateOrder(request, new Callback<GenerateOrderData>() {
                @Override
                public void success(GenerateOrderData generateOrderData, Response response) {
                    notifyObservers(generateOrderData);
                }

                @Override
                public void failure(RetrofitError error) {
                    notifyObservers(error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getTxnId() {
        return ("0nf7" + System.currentTimeMillis());
    }

    public void orderConfirm(GenerateOrderData generateOrderData) {
        try {

            HashMap<String, Object> request = new HashMap<String, Object>();
            request.put("txnid", generateOrderData.ordernumber);
            request.put("payuMoneyId", generateOrderData.payUMoneyId);

            restInterface.orderConfirm(request, new Callback<OrderConfirmData>() {
                @Override
                public void success(OrderConfirmData generateOrderData, Response response) {
                    notifyObservers(generateOrderData);
                }

                @Override
                public void failure(RetrofitError error) {
                    notifyObservers(error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
