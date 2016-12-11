package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.CommonResponseData;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.dao.WithDrawData;
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
public class WithDrawModel extends BasicModel {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);

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

    public void withDrawStatusList() {
        try {
            restInterface.withDrawStatusList(Config.getUserId(), new Callback<WithDrawData>() {
                @Override
                public void success(WithDrawData withDrawData, Response response) {
                    notifyObservers(withDrawData);
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
    public void createPaymentRequest(WithDrawData withDrawData) {
        try {

            HashMap<String,String>request =new HashMap<String,String>();
            request.put("trans_id",Config.getUserId());
            request.put("amount",withDrawData.amount);
            request.put("bank_name",withDrawData.bank_name);
            request.put("acct_no",withDrawData.acct_no);
            request.put("ifsc",withDrawData.ifsc);
            request.put("status","N");
            request.put("created","");
            request.put("swift_code",withDrawData.swift_code);
            request.put("act_name",withDrawData.act_name);
            restInterface.createPaymentRequest(request, new Callback<CommonResponseData>() {
                @Override
                public void success(CommonResponseData withDrawData, Response response) {
                    notifyObservers(withDrawData);
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
