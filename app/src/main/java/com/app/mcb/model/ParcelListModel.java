package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.retrointerface.RestInterface;

import org.byteclues.lib.model.BasicModel;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hitesh on 11-10-2016.
 */
public class ParcelListModel extends BasicModel {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);

    public void getActiveParcels() {
        try {

            restInterface.getActiveParcels("11334",new Callback<ParcelListData>() {
                @Override
                public void success(ParcelListData parcelListData, Response response) {
                    notifyObservers(parcelListData);
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

    public void getAllParcels() {
        try {

            restInterface.getAllParcels("11334",new Callback<ParcelListData>() {
                @Override
                public void success(ParcelListData parcelListData, Response response) {
                    notifyObservers(parcelListData);
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
