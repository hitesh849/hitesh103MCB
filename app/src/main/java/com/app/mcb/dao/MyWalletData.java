package com.app.mcb.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hitesh on 12-10-2016.
 */
public class MyWalletData implements Serializable
{
    public String ststus;
    public String errorMessage;
    public String id;
    public String comment;
    public String insertdate;
    public String amount;
    public String credit;
    public String debit;
    public String userid;
    public String parcelid;
    public String tripid;
    public String weight;
    public String withdrawID;
    public String status;
    public String withdrawamount;
    public String statusdescription;
    public String MParcelID;
    public String MTripID;
    public ArrayList<MyWalletData> response;

}
