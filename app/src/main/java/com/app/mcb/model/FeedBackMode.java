package com.app.mcb.model;

import com.app.mcb.Utility.Constants;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.retrointerface.RestInterface;

import org.byteclues.lib.model.BasicModel;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Hitesh on 15-10-2016.
 */
public class FeedBackMode extends BasicModel {
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
    RestInterface restInterface = adapter.create(RestInterface.class);


}
