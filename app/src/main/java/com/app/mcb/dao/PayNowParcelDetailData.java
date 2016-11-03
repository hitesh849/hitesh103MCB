package com.app.mcb.dao;

import java.io.Serializable;

/**
 * Created by u on 11/3/2016.
 */
public class PayNowParcelDetailData extends CommonResponseData implements Serializable {

    public TripData response;
    public ParcelDetailsData parcellist;
}
