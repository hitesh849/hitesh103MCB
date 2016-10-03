package com.app.mcb.model;

import android.content.Context;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.AirportData;
import com.app.mcb.dao.FilterData;
import com.app.mcb.database.DatabaseMgr;

import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.app.mcb.retrointerface.RestInterface;

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

    public void getTripListByFilter(final Context context, FilterData filterData) {
        try {

            HashMap<String, String> request = new HashMap<String, String>();
            request.put("dateFrom", "");
            request.put("dateTo", "");
            request.put("locationfrom", "");
            request.put("locationto", "");
            request.put("type", "Transporter/Sender");
            restInterface.getAirportData(new Callback<AirportData>() {
                @Override
                public void success(AirportData airportData, Response response) {
                    if (Constants.RESPONSE_SUCCESS_MSG.equals(airportData.status))
                        DatabaseMgr.getInstance(context).insertDataToAirportTable(airportData.response);
                    notifyObservers(airportData);
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