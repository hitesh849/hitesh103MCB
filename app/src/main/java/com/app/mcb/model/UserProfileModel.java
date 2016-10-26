package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.retrointerface.RestInterface;
import com.app.mcb.sharedPreferences.Config;

import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hitesh on 16-10-2016.
 */
public class UserProfileModel extends BasicModel {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);


    public void updateProfile(UserInfoData userInfoData) {
        try {

            HashMap<String, String> request = new HashMap<String, String>();
            request.put("id", Config.getUserId());
            request.put("username", userInfoData.email);
            request.put("mobile", userInfoData.mobile);
            request.put("phone", userInfoData.phone);
            request.put("passportno", userInfoData.passportno);
            request.put("address", userInfoData.address);
            request.put("photo", userInfoData.photo);
            if (userInfoData.name.trim().contains(" ")) {
                String name[] = userInfoData.name.split(" ");
                request.put("name", name[0]);
                request.put("l_name", name[1]);
            } else {
                request.put("name", userInfoData.name);
            }

            restInterface.updateProfile(request, new Callback<UserInfoData>() {
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
}

