package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
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
 * Created by Hitesh on 13-10-2016.
 */
public class AddParcelModel extends BasicModel {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);

    public void sendInvitation(UserInfoData userInfoData) {
        HashMap<String, String> request = new HashMap<String, String>();
        request.put("UserID", userInfoData.id);
        request.put("email", userInfoData.email);
        request.put("message", userInfoData.message);
        request.put("name", userInfoData.name);
        request.put("number", userInfoData.mobile);
        restInterface.sendInvitation(request, new Callback<UserInfoData>() {
            @Override
            public void success(UserInfoData userInfoData, Response response) {
                notifyObservers(userInfoData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void searchReceiver(UserInfoData userInfoData) {
        HashMap<String, String> request = new HashMap<String, String>();
        request.put("UserId", userInfoData.id);
        request.put("email", userInfoData.email);
        request.put("mobile", userInfoData.mobile);
        restInterface.sendInvitation(request, new Callback<UserInfoData>() {
            @Override
            public void success(UserInfoData userInfoData, Response response) {
                notifyObservers(userInfoData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }
}

