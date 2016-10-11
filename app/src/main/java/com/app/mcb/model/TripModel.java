package com.app.mcb.model;

import android.content.Context;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.AirportData;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.TripData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.database.DatabaseMgr;

import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.app.mcb.retrointerface.RestInterface;
import com.google.gson.JsonElement;

/**
 * Created by Hitesh on 28-09-2016.
 */
public class TripModel extends BasicModel {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);

    public void getAirportData(final Context context) {
        try {

            restInterface.getAirportData(new Callback<AirportData>() {
                @Override
                public void success(AirportData airportData, Response response) {
                    if (Constants.RESPONSE_SUCCESS_MSG.equals(airportData.status)) {
                        DatabaseMgr.getInstance(context).insertDataToAirportTable(airportData.response);
                        notifyObservers(airportData);
                    }
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

    public void getTripListByFilter(FilterData filterData) {
        try {
            HashMap<String, HashMap> request = new HashMap<String, HashMap>();
            HashMap<String, String> subrequest = new HashMap<String, String>();
            subrequest.put("dateFrom", filterData.fromDate);
            subrequest.put("dateTo", filterData.toDate);
            subrequest.put("locationfrom", filterData.fromLocation);
            subrequest.put("locationto", filterData.toLocation);
            subrequest.put("type", filterData.type);
            request.put("params", subrequest);

            restInterface.getTripListByFilter(request, new Callback<TripTransporterData>() {
                @Override
                public void success(TripTransporterData tripData, Response response) {
                    notifyObservers(tripData);
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