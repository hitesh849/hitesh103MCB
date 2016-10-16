package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.retrointerface.RestInterface;
import com.google.gson.JsonElement;

import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Admin on 10/17/2016.
 */
public class AddTripModel extends BasicModel {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);

    public void addTrip(HashMap<String, Object> requestData) {
        restInterface.addTrip(requestData, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                notifyObservers(jsonElement);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }
}
