package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.ParcelBookingChangeStatusData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.dao.ReceiverData;
import com.app.mcb.retrointerface.RestInterface;
import com.app.mcb.sharedPreferences.Config;

import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;

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


    public void usrUpdateTripStatus(ReceiverData parcelDetailsData, String status, String msg) {
        try {
            HashMap<String,Object> request=new HashMap<String,Object>();
            request.put("id", parcelDetailsData.trans_id);
            request.put("status", Integer.parseInt(status));
            request.put("process_by", Config.getUserId());
            request.put("reason", msg);
            request.put("parcelid", parcelDetailsData.id);
            restInterface.usrUpdateTripStatus(request, new Callback<ParcelBookingChangeStatusData>() {
                @Override
                public void success(ParcelBookingChangeStatusData commonResponseData, Response response) {
                    notifyObservers(commonResponseData);
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
