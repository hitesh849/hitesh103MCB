package com.app.mcb.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 13/10/16.
 */
public class MyTripsData implements Serializable {
    public String id;
    public String source;
    public String destination;
    public String dep_time;
    public String arrival_time;
    public String duration;
    public String image;
    public String flight_no;
    public String pnr;
    public String comment;
    public String capacity;
    public String t_id;
    public String created;
    public String status;
    public String processed_by;
    public String update;
    public String requirement;
    public String payment;
    public String TripID;
    public String awailableweight;
    public String statusdescription;
    public String airlinelink;
    public String myClickOn;
    public String transportername;
    public String transporteremail;
    public String receiveremail;
    public String receivermobile;
    public String receivername;
    public String receivercountrycode;
    public String errorMessage;
    public ArrayList<MyTripsData> response;
}
