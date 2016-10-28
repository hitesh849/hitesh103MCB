package com.app.mcb.model;

import android.content.Context;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.MyTripDetailsData;
import com.app.mcb.retrointerface.RestInterface;
import com.google.gson.JsonElement;

import org.byteclues.lib.model.BasicModel;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hitesh on 28-10-2016.
 */
public class MyTripParcelListModel extends BasicModel{
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
}
