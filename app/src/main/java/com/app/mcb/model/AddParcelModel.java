package com.app.mcb.model;

import android.os.Build;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.AddParcelData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.SearchReceiverData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.retrointerface.RestInterface;
import com.app.mcb.sharedPreferences.Config;
import com.google.gson.JsonElement;

import org.byteclues.lib.init.Env;
import org.byteclues.lib.model.BasicModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hitesh on 13-10-2016.
 */
public class AddParcelModel extends BasicModel {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);

    public void sendInvitation(UserInfoData userInfoData) {
        HashMap<String, String> request = new HashMap<String, String>();
        request.put("UserID", Config.getUserId());
        request.put("email", userInfoData.email);
        request.put("message", userInfoData.message);
        request.put("name", userInfoData.name);
        request.put("number", userInfoData.mobile);
        restInterface.sendInvitation(request, new Callback<UserInfoData>() {
            @Override
            public void success(UserInfoData userInfoData, Response response) {
                userInfoData.userType = "N";
                notifyObservers(userInfoData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void searchReceiver(String mobile, String email) {

        HashMap<String, Object> request = new HashMap<String, Object>();
        HashMap<String, String> subRequest = new HashMap<String, String>();
        subRequest.put("UserId", Config.getUserId());
        subRequest.put("email", email);
        subRequest.put("mobile", mobile);
        request.put("params", subRequest);
        restInterface.searchReceiver(request, new Callback<SearchReceiverData>() {
            @Override
            public void success(SearchReceiverData userInfoData, Response response) {
                userInfoData.userType = "E";
                notifyObservers(userInfoData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void getUserDetails() {
        try {

            restInterface.getUserDetails(Config.getUserId(), new Callback<UserInfoData>() {
                @Override
                public void success(UserInfoData userInfoData, Response response) {
                    notifyObservers(userInfoData);
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


    public void addParcel(ParcelDetailsData parcelDetailsData) {

        HashMap<String, Object> request = new HashMap<String, Object>();
        request.put("source", parcelDetailsData.source);
        request.put("destination", parcelDetailsData.destination);
        request.put("till_date", parcelDetailsData.till_date);
        request.put("type", parcelDetailsData.type);
        request.put("weight", parcelDetailsData.weight);
        request.put("height", parcelDetailsData.height);
        request.put("width", parcelDetailsData.width);
        request.put("length", parcelDetailsData.length);
        request.put("created",convertEdtToLocalTime());
        request.put("usr_id", Config.getUserId());
        request.put("recv_id", parcelDetailsData.receiverInfoData.id);
        request.put("status", parcelDetailsData.status);
        request.put("description", parcelDetailsData.description);
        request.put("payment", parcelDetailsData.price);

        restInterface.addParcel(request, new Callback<AddParcelData>() {
            @Override
            public void success(AddParcelData addParcelData, Response response) {

                notifyObservers(addParcelData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    private static String convertEdtToLocalTime() {
        //pubDate = 12/19/2012 8:57am EST;
        String localPubDate = null;
        try {
            Date now = new Date();
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = Env.appContext.getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = Env.appContext.getResources().getConfiguration().locale;
            }
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat();
            DateFormat df = DateFormat.getDateTimeInstance(
                    DateFormat.DEFAULT, DateFormat.DEFAULT, locale);
            TimeZone est = TimeZone.getTimeZone("IST");
            TimeZone mst = TimeZone.getTimeZone("MST");

            df.setTimeZone(est);
            System.out.println(df.format(now));
            df.setTimeZone(mst);
            System.out.println(df.format(now));
            return df.format(now);
        } catch (Exception e) {
        }
       return localPubDate;
    }

    public void calculateAmount(final ParcelDetailsData parcelDetailsData) {

        HashMap<String, Object> request = new HashMap<String, Object>();
        request.put("source", parcelDetailsData.source);
        request.put("destination", parcelDetailsData.destination);
        request.put("till_date", parcelDetailsData.till_date);
        request.put("type", parcelDetailsData.type);
        request.put("weight", parcelDetailsData.weight);
        request.put("height", parcelDetailsData.height);
        request.put("width", parcelDetailsData.width);
        request.put("length", parcelDetailsData.length);
        request.put("created", new Date());
        request.put("usr_id", Config.getUserId());
        request.put("recv_id", parcelDetailsData.receiverInfoData.id);
        request.put("status", parcelDetailsData.status);
        request.put("description", parcelDetailsData.description);
        request.put("payment", "9200");
        restInterface.calculateAmount(request, new Callback<ParcelDetailsData>() {
            @Override
            public void success(ParcelDetailsData parcelDetailsData1, Response response) {
                parcelDetailsData.price = parcelDetailsData1.price;
                notifyObservers(parcelDetailsData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void updateParcels(final ParcelDetailsData parcelDetailsData) {

        HashMap<String, Object> request = new HashMap<String, Object>();
        request.put("source", parcelDetailsData.source);
        request.put("id", parcelDetailsData.id);
        request.put("destination", parcelDetailsData.destination);
        request.put("till_date", parcelDetailsData.till_date);
        request.put("type", parcelDetailsData.type);
        request.put("weight", parcelDetailsData.weight);
        request.put("height", parcelDetailsData.height);
        request.put("width", parcelDetailsData.width);
        request.put("length", parcelDetailsData.length);
        request.put("usr_id", parcelDetailsData.usr_id);
        request.put("trans_id", parcelDetailsData.trans_id);
        request.put("recv_id", parcelDetailsData.recv_id);
        request.put("status", parcelDetailsData.status);
        request.put("description", parcelDetailsData.description);
        request.put("payment", parcelDetailsData.payment);
        restInterface.updateParcels(request, new Callback<AddParcelData>() {
            @Override
            public void success(AddParcelData addParcelData, Response response) {

                notifyObservers(addParcelData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }
}

