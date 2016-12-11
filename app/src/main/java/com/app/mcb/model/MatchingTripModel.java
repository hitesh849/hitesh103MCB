package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.AddParcelData;
import com.app.mcb.dao.BookingRequestData;
import com.app.mcb.dao.GenerateOrderData;
import com.app.mcb.dao.MyTripDetailsData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.retrointerface.RestInterface;
import com.app.mcb.sharedPreferences.Config;

import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hitesh on 01-11-2016.
 */
public class MatchingTripModel extends BasicModel {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);

    public void getParcelDetails(String parcelId) {
        try {

            restInterface.getParcelDetails(parcelId, new Callback<ParcelDetailsData>() {
                @Override
                public void success(ParcelDetailsData parcelDetailsData, Response response) {
                    notifyObservers(parcelDetailsData);
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

    public void getMyTripDetails(String tripId) {
        try {
            restInterface.getMyTripDetailsWithPaymentDue(tripId, new Callback<MyTripsData>() {
                @Override
                public void success(MyTripsData myTripDetailsData, Response response) {
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

    public void bookingRequest(String tripId,String trans_id) {
        try {
            restInterface.bookingRequest(tripId,trans_id, new Callback<BookingRequestData>() {
                @Override
                public void success(BookingRequestData commonResponseData, Response response) {
                    notifyObservers(commonResponseData);
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

    public void updateParcels(final ParcelDetailsData parcelDetailsData) {

        HashMap<String, Object> request = new HashMap<String, Object>();
        request.put("source", parcelDetailsData.source);
        request.put("id", parcelDetailsData.id);
        request.put("destination", parcelDetailsData.destination);
        request.put("till_date", parcelDetailsData.till_date);
        request.put("type", parcelDetailsData.type);
        request.put("weight", parcelDetailsData.weight);
        request.put("height", parcelDetailsData.height);
        request.put("width", parcelDetailsData.width);
        request.put("length", parcelDetailsData.length);
        request.put("usr_id", parcelDetailsData.usr_id);
        request.put("trans_id", parcelDetailsData.trans_id);
        request.put("recv_id", parcelDetailsData.recv_id);
        request.put("status", parcelDetailsData.status);
        request.put("description", parcelDetailsData.description);
        request.put("payment", parcelDetailsData.payment);
        restInterface.updateParcels(request, new Callback<AddParcelData>() {
            @Override
            public void success(AddParcelData addParcelData, Response response) {

                notifyObservers(addParcelData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }
}
