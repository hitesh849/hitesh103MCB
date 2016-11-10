package com.app.mcb.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hitesh on 12-10-2016.
 */
public class ReceiverData implements Serializable{

    public String errorMessage;
    public String receivermobile;
    public String receivername;
    public String sendermobile;
    public String transporteremail;
    public String transportermobile;
    public String transportername;
    public String Transporterid;
    public String MCBtransporterid;
    public String MCBSenderID;
    public String flight_no;
    public String arrival_time;
    public String dep_time;
    public String pnr;
    public String TripID;
    public ArrayList<ReceiverData> response;

    public String id;
    public String usr_id;
    public String source;
    public String destination;
    public String till_date;
    public String type;
    public String height = "0";
    public String width = "0";
    public String length = "0";
    public String weight;
    public String created;
    public String description;
    public String status = "0";
    public String recv_id;
    public String recv_email;
    public String recv_mobile;
    public String recv_name;
    public String recv_comment;
    public String processed_by;
    public String payment;
    public String trans_id;
    public String trans_comment;
    public String reason;
    public String ParcelID;
    public String receiveremail;
    public String MCBreceiverID;
    public String transporterID;
    public String statusdescription;
    public String channelid;
    public String Isactive;
    public String transemail;
    public String price;
    public String MCBTransporterID;
    public String BookingID;
    public String senderemail;
    public String sendername;
    public UserInfoData receiverInfoData;
    public ArrayList<MyTripsData> tripsmatch;
    public ArrayList<MyTripsData> trip;
}
