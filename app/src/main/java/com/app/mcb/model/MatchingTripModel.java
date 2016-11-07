package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
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
}
