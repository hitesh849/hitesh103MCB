package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.dao.ReceiverData;
import com.app.mcb.retrointerface.RestInterface;
import com.app.mcb.sharedPreferences.Config;

import org.byteclues.lib.model.BasicModel;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hitesh on 12-10-2016.
 */
public class ReceiverModel extends BasicModel {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);

    public void getReceiverData() {
        try {
            restInterface.getReceiverData(Config.getUserId(), new Callback<ReceiverData>() {
                @Override
                public void success(ReceiverData receiverData, Response response) {
                    notifyObservers(receiverData);
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
