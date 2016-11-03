package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.BookingRequestData;
import com.app.mcb.dao.MyTripDetailsData;
import com.app.mcb.dao.ParcelBookingChangeStatusData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.PayNowParcelDetailData;
import com.app.mcb.retrointerface.RestInterface;
import com.app.mcb.sharedPreferences.Config;

import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hitesh on 28-10-2016.
 */
public class MyTripParcelListModel extends BasicModel{
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);

    public void getMyTripDetails(String tripId) {
        try {
            restInterface.getMyTripDetails(tripId, new Callback<MyTripDetailsData>() {
                @Override
                public void success(MyTripDetailsData myTripDetailsData, Response response) {
                    notifyObservers(myTripDetailsData);
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


    public void bookingRequest(String tripId,String trans_id) {
        try {
            restInterface.bookingRequest(tripId,trans_id, new Callback<BookingRequestData>() {
                @Override
                public void success(BookingRequestData commonResponseData, Response response) {
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
    public void parcelRejectRequest(ParcelDetailsData parcelDetailsData) {
        try {
            HashMap<String,String> request=new HashMap<String,String>();
            request.put("id", parcelDetailsData.trans_id);
            request.put("status", Constants.ParcelRejectedByTr);
            request.put("process_by", Config.getUserId());
            request.put("reason", "test");
            request.put("parcelid", parcelDetailsData.id);
            restInterface.parcelRejectRequest(request, new Callback<ParcelBookingChangeStatusData>() {
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

    public void usrUpdateTripStatus(ParcelDetailsData parcelDetailsData,String status,String msg) {
        try {
            HashMap<String,String> request=new HashMap<String,String>();
            request.put("id", parcelDetailsData.trans_id);
            request.put("status", status);
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
