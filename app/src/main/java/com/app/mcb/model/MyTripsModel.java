package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelBookingChangeStatusData;
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
 * Created by Admin on 10/12/2016.
 */
public class MyTripsModel extends BasicModel {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);

    public void getUserTripList() {
        restInterface.getUserTripList(Config.getUserId(), new Callback<MyTripsData>() {
            @Override
            public void success(MyTripsData responesData, Response response) {
                notifyObservers(responesData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void cancelTrip(HashMap<String,Object> requestData) {
        restInterface.cancelTrip(requestData, new Callback<ParcelBookingChangeStatusData>() {
            @Override
            public void success(ParcelBookingChangeStatusData jsonElement, Response response) {
                notifyObservers(jsonElement);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }
}
