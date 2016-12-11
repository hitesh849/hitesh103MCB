package com.app.mcb.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.AirportData;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.database.DatabaseMgr;
import com.app.mcb.retrointerface.RestInterface;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.byteclues.lib.model.BasicModel;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hitesh on 28-09-2016.
 */
public class HomeTripModel extends BasicModel {
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
            HashMap<String, Object> request = new HashMap<String, Object>();
            HashMap<String, String> subrequest = new HashMap<String, String>();
            String blank = " ";
            if (!TextUtils.isEmpty(filterData.fromDate))
                subrequest.put("dateFrom", filterData.fromDate);
            else
                subrequest.put("dateFrom", blank);

            if (!TextUtils.isEmpty(filterData.toDate))
                subrequest.put("dateTo", filterData.toDate);
            else
                subrequest.put("dateTo", blank);

            if (!TextUtils.isEmpty(filterData.fromLocation))
                subrequest.put("locationfrom", filterData.fromLocation);
            else
                subrequest.put("locationfrom", blank);

            if (!TextUtils.isEmpty(filterData.toLocation))
                subrequest.put("locationto", filterData.toLocation);
            else
                subrequest.put("locationto", blank);

            subrequest.put("city", filterData.city);
            subrequest.put("type", filterData.type);
            request.put("params", subrequest);
            Log.d("test", request.toString());
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

    public void getParcelsListByFilter(FilterData filterData) {
        try {
            HashMap<String, Object> request = new HashMap<String, Object>();
            HashMap<String, String> subrequest = new HashMap<String, String>();
            String blank = " ";
            if (!TextUtils.isEmpty(filterData.fromDate))
                subrequest.put("dateFrom", filterData.fromDate);
            else
                subrequest.put("dateFrom", blank);

            if (!TextUtils.isEmpty(filterData.toDate))
                subrequest.put("dateTo", filterData.toDate);
            else
                subrequest.put("dateTo", blank);

            if (!TextUtils.isEmpty(filterData.fromLocation))
                subrequest.put("locationfrom", filterData.fromLocation);
            else
                subrequest.put("locationfrom", blank);

            if (!TextUtils.isEmpty(filterData.toLocation))
                subrequest.put("locationto", filterData.toLocation);
            else
                subrequest.put("locationto", blank);

            subrequest.put("type", filterData.type);
            request.put("params", subrequest);
            Log.d("test", request.toString());
            restInterface.getParcelsListByFilter(request, new Callback<ParcelListData>() {
                @Override
                public void success(ParcelListData list, Response response) {
                    notifyObservers(list);
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

    public void getTopForCityInHome() {
        try {
            restInterface.getTopForCityInHome(new Callback<TripTransporterData>() {
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