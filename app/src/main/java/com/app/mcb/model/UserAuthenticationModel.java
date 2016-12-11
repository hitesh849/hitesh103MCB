package com.app.mcb.model;

import android.content.Context;
import android.webkit.JsPromptResult;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.AirportData;
import com.app.mcb.dao.ChangePasswordData;
import com.app.mcb.dao.CommonResponseData;
import com.app.mcb.dao.ForgetPasswordData;
import com.app.mcb.dao.ForgotPasswordData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.database.DatabaseMgr;
import com.app.mcb.retrointerface.RestInterface;
import com.google.gson.JsonElement;

import org.byteclues.lib.model.BasicModel;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hitesh on 29-09-2016.
 */
public class UserAuthenticationModel extends BasicModel {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);

    public void forgetPassword(String email) {
        try {

            HashMap<String, String> response = new HashMap<String, String>();
            response.put("userName", email);
            restInterface.forgetPassword(response, new Callback<ForgetPasswordData>() {
                @Override
                public void success(ForgetPasswordData forgetPasswordData, Response response) {
                    notifyObservers(forgetPasswordData);
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

    public void registerUser(UserInfoData userInfoData) {
        try {
            HashMap<String, String> response = new HashMap<String, String>();
            response.put("firstName", userInfoData.firstName);
            response.put("lastName", userInfoData.lastName);
            response.put("email", userInfoData.email);
            response.put("countrycode", userInfoData.country_code);
            response.put("mobilenumber", userInfoData.mobile);
            response.put("password", userInfoData.password);
            response.put("passwordRepeat", userInfoData.password);
            response.put("photo", userInfoData.image);
            response.toString();
            restInterface.registerUser(response, new Callback<UserInfoData>() {
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

    public void changePassword(String email) {
        try {

            HashMap<String, String> response = new HashMap<String, String>();
            response.put("userID", email);
            response.put("password", email);
            response.put("newpassword", email);
            restInterface.changePassword(response, new Callback<ChangePasswordData>() {
                @Override
                public void success(ChangePasswordData changePasswordData, Response response) {
                    notifyObservers(changePasswordData);
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


    public void getLoginUser(String email, String password) {
        try {

            HashMap<String, String> response = new HashMap<String, String>();
            response.put("email", email);
            response.put("password", password);
            restInterface.getLoginUser(response, new Callback<UserInfoData>() {
                @Override
                public void success(UserInfoData changePasswordData, Response response) {
                    notifyObservers(changePasswordData);
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

    public void forgotPassword(String email) {
        try {

            HashMap<String, String> response = new HashMap<String, String>();
            response.put("userName", email);
            restInterface.forgotPassword(response, new Callback<ForgotPasswordData>() {
                @Override
                public void success(ForgotPasswordData forgotPasswordData, Response response) {
                    notifyObservers(forgotPasswordData);
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
