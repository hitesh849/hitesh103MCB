package com.app.mcb.retrointerface;

import com.app.mcb.dao.AddParcelData;
import com.app.mcb.dao.AddTrip;
import com.app.mcb.dao.AirportData;
import com.app.mcb.dao.BookingRequestData;
import com.app.mcb.dao.ChangePasswordData;
import com.app.mcb.dao.CommonResponseData;
import com.app.mcb.dao.ForgetPasswordData;
import com.app.mcb.dao.ForgotPasswordData;
import com.app.mcb.dao.GenerateOrderData;
import com.app.mcb.dao.MyTripDetailsData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.MyWalletData;
import com.app.mcb.dao.ParcelBookingChangeStatusData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.dao.PayNowParcelDetailData;
import com.app.mcb.dao.ReceiverData;
import com.app.mcb.dao.SearchReceiverData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.dao.WithDrawData;
import com.google.gson.JsonElement;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Hitesh on 28-09-2016.
 */
public interface RestInterface {
    @Headers({"Content-Type:application/json"})
    @GET("/getcountries")
    public void getAirportData(Callback<AirportData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/searchhome")
    public void getTripListByFilter(@Body HashMap<String, Object> request, Callback<TripTransporterData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/searchhome")
    public void getParcelsListByFilter(@Body HashMap<String, Object> request, Callback<ParcelListData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/gettopcountrytrips")
    public void getTopForCityInHome(Callback<TripTransporterData> cb);

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
    @POST("/forgetpassword")
    public void forgotPassword(@Body HashMap<String, String> request, Callback<ForgotPasswordData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/searchuser")
    public void searchUser(@Body HashMap<String, String> request, Callback<UserInfoData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/addparcel")
    public void addParcel(@Body HashMap<String, Object> request, Callback<AddParcelData> cb);

    @Headers({"Content-Type:application/json"})
    @GET("/parcellist/{userId}")
    public void getActiveParcels(@Path("userId") String userId, Callback<ParcelListData> cb);

    @Headers({"Content-Type:application/json"})
    @GET("/cancelparcellist/{userId}")
    public void getAllParcels(@Path("userId") String userId, Callback<ParcelListData> cb);

    @Headers({"Content-Type:application/json"})
    @GET("/getparceldetail/{parcel_id}")
    public void getParcelDetails(@Path("parcel_id") String parcel_id, Callback<ParcelDetailsData> cb);

    @Headers({"Content-Type:application/json"})
    @GET("/getwalletstatement/{userId}")
    public void getMyWalletDetails(@Path("userId") String userId, Callback<MyWalletData> cb);

    @Headers({"Content-Type:application/json"})
    @GET("/getuserdetails/{userId}")
    public void getUserDetails(@Path("userId") String userId, Callback<UserInfoData> cb);

    @Headers({"Content-Type:application/json"})
    @GET("/receiverlist/{userId}")
    public void getReceiverData(@Path("userId") String userId, Callback<ReceiverData> cb);

    @Headers({"Content-Type:application/json"})
    @GET("/triplist/{userId}")
    public void getUserTripList(@Path("userId") String userId, Callback<MyTripsData> cb);

    @Headers({"Content-Type:application/json"})
    @GET("/canceltripslist/{userId}")
    public void getCancelledTripList(@Path("userId") String userId, Callback<MyTripsData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/sendinvite")
    public void sendInvitation(@Body HashMap<String, String> request, Callback<UserInfoData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/searchuser")
    public void searchReceiver(@Body HashMap<String, Object> request, Callback<SearchReceiverData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/calculateamount")
    public void calculateAmount(@Body HashMap<String, Object> request, Callback<ParcelDetailsData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/usrupdateParceltatus")
    public void cancelParcel(@Body HashMap<String, Object> request, Callback<JsonElement> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/usrupdatetripstatus")
    public void cancelTrip(@Body HashMap<String, Object> request, Callback<JsonElement> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/updateparcel")
    public void updateParcels(@Body HashMap<String, Object> request, Callback<AddParcelData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/updateuserdetails")
    public void updateProfile(@Body HashMap<String, String> request, Callback<UserInfoData> cb);


    @Headers({"Content-Type:application/json"})
    @POST("/paymentrequestlist/{userId}")
    public void withDrawStatusList(@Path("userId") String userId, Callback<WithDrawData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/addtrip")
    public void addTrip(@Body HashMap<String, String> request, Callback<CommonResponseData> cb);


    @Headers({"Content-Type:application/json"})
    @POST("/updatetrip")
    public void updateTrip(@Body HashMap<String, String> request, Callback<AddTrip> cb);

    @Headers({"Content-Type:application/json"})
    @GET("/gettransporterdetail/{tripId}")
    public void getMyTripDetails(@Path("tripId") String tripId, Callback<MyTripDetailsData> cb);


    @Headers({"Content-Type:application/json"})
    @GET("/senderbookingrequest/{parcel_id}/{trans_id}")
    public void bookingRequest(@Path("parcel_id") String parcel_id, @Path("trans_id") String trans_id, Callback<BookingRequestData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/cancelparcelbytransporter")
    public void parcelRejectRequest(@Body HashMap<String, String> request, Callback<ParcelBookingChangeStatusData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/usrupdatetripstatus")
    public void usrUpdateTripStatus(@Body HashMap<String, String> request, Callback<ParcelBookingChangeStatusData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/generateordernumber")
    public void generateOrder(@Body HashMap<String, Object> request, Callback<GenerateOrderData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/creatCouerier")
    public void creatCourierRequest(@Body HashMap<String, String> request, Callback<GenerateOrderData> cb);

}
