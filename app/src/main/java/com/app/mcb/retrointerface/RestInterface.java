package com.app.mcb.retrointerface;

import com.app.mcb.dao.AddParcelData;
import com.app.mcb.dao.AirportData;
import com.app.mcb.dao.ChangePasswordData;
import com.app.mcb.dao.ForgetPasswordData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.TripData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.dao.UserInfoData;
import com.google.gson.JsonElement;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by Hitesh on 28-09-2016.
 */
public interface RestInterface {
    @Headers({"Content-Type:application/json"})
    @GET("/getcountries")
    public void getAirportData(Callback<AirportData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/searchhome")
    public void getTripListByFilter(@Body HashMap<String, HashMap> request,Callback<TripTransporterData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/forgetpassword")
    public void forgetPassword(@Body HashMap<String, String> request, Callback<ForgetPasswordData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/registeruser")
    public void registerUser(@Body HashMap<String, String> request, Callback<UserInfoData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/changepassword")
    public void changePassword(@Body HashMap<String, String> request, Callback<ChangePasswordData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/getloginuser")
    public void getLoginUser(@Body HashMap<String, String> request, Callback<UserInfoData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/searchuser")
    public void searchUser(@Body HashMap<String, String> request, Callback<UserInfoData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/addparcel")
    public void addParcel(@Body HashMap<String, String> request, Callback<AddParcelData> cb);
}
