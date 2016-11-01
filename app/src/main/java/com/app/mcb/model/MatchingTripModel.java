package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.retrointerface.RestInterface;

import org.byteclues.lib.model.BasicModel;

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
}
