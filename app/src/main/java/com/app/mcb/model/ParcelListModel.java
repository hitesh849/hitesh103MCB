package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.ParcelBookingChangeStatusData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.retrointerface.RestInterface;
import com.app.mcb.sharedPreferences.Config;
import com.google.gson.JsonElement;

import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;

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
            restInterface.getActiveParcels(Config.getUserId(), new Callback<ParcelListData>() {
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

            restInterface.getAllParcels(Config.getUserId(), new Callback<ParcelListData>() {
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

    public void getParcelDetails(String parcelId) {
        try {

            restInterface.getParcelDetails(parcelId ,new Callback<ParcelDetailsData>() {
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


    public void cancelParcel(ParcelDetailsData parcelDetailsData) {

        HashMap<String, Object> request = new HashMap<String, Object>();
        request.put("id", parcelDetailsData.id);
        request.put("process_by", Config.getUserId());
        request.put("reason", "");
        request.put("status", "6");
        restInterface.cancelParcel(request, new Callback<ParcelBookingChangeStatusData>() {
            @Override
            public void success(ParcelBookingChangeStatusData jsonElement, Response response) {

                notifyObservers(jsonElement);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void usrUpdateParcelStatus(ParcelDetailsData parcelDetailsData,String status,String msg) {
        try {
            HashMap<String,Object> request=new HashMap<String,Object>();
            request.put("id", parcelDetailsData.id);
            request.put("status", Integer.parseInt(status));
            request.put("process_by", Config.getUserId());
            request.put("reason", msg);
            restInterface.usrUpdateParcelStatus(request, new Callback<ParcelBookingChangeStatusData>() {
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
