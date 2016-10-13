package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.retrointerface.RestInterface;

import org.byteclues.lib.model.BasicModel;

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
        restInterface.getUserTripList("11334", new Callback<MyTripsData>() {
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
}
