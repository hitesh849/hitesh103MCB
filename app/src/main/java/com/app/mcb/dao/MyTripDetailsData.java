package com.app.mcb.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hitesh on 28-10-2016.
 */
public class MyTripDetailsData extends TripData implements Serializable {

    public String errorMessage;
    public ArrayList<ParcelDetailsData> parcel;
    public ArrayList<ParcelDetailsData> parcellist;
}
