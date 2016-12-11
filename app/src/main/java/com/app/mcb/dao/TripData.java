package com.app.mcb.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hitesh on 28-09-2016.
 */
public class TripData implements Serializable {
    public String status;
    public String id;
    public String source;
    public String destination;
    public String dep_time;
    public String d_time;
    public String arrival_time;
    public String a_time;
    public String image;
    public String flight_no;
    public String pnr;
    public String comment;
    public String capacity;
    public String t_id;
    public String created;
    public String processed_by;
    public String update;
    public String requirement;
    public String payment;
    public String TripID;
    public String trans_id;
    public String awailableweight;
    public String statusdescription;
    public String airlinelink;

    public ArrayList<TripData> response;
}
