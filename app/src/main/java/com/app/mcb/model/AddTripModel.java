package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.AddTrip;
import com.app.mcb.dao.AddTripData;
import com.app.mcb.dao.CommonResponseData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.TripData;
import com.app.mcb.retrointerface.RestInterface;
import com.app.mcb.sharedPreferences.Config;
import com.google.gson.JsonElement;

import org.byteclues.lib.model.BasicModel;
import org.json.JSONObject;

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

    public void addTrip(TripData tripData) {

        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("source", tripData.source);
        requestData.put("destination", tripData.destination);
        requestData.put("d_date", tripData.dep_time);
        requestData.put("a_date", tripData.arrival_time);
        requestData.put("flight_no", tripData.flight_no);
        requestData.put("pnr", tripData.pnr);
        requestData.put("capacity", tripData.capacity);
        requestData.put("comment", tripData.comment);
        requestData.put("t_id", Config.getUserId());
        tripData.image = (tripData.image == null) ? "" : tripData.image;
        requestData.put("ticket", tripData.image);
        String str=new JSONObject(requestData).toString();
        System.out.println(str);
        restInterface.addTrip(requestData, new Callback<AddTripData>() {
            @Override
            public void success(AddTripData jsonElement, Response response) {
                notifyObservers(jsonElement);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void updateTrip(TripData tripData) {

        HashMap<String, String> requestData = new HashMap<String, String>();
        requestData.put("source", tripData.source);
        requestData.put("destination", tripData.destination);
        requestData.put("d_date", tripData.dep_time);
        requestData.put("a_date", tripData.arrival_time);
        requestData.put("flight_no", tripData.flight_no);
        requestData.put("pnr", tripData.pnr);
        requestData.put("capacity", tripData.capacity);
        requestData.put("comment", tripData.comment);
        requestData.put("status", tripData.status);
        requestData.put("t_id", Config.getUserId());
        requestData.put("id", tripData.id);
        tripData.image = (tripData.image == null) ? "" : tripData.image;
        requestData.put("ticket", tripData.image);
        System.out.println(requestData.toString());
        restInterface.updateTrip(requestData, new Callback<AddTrip>() {
            @Override
            public void success(AddTrip jsonElement, Response response) {
                notifyObservers(jsonElement);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

}
