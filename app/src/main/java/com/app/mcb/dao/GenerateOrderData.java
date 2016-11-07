package com.app.mcb.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by u on 11/3/2016.
 */
public class GenerateOrderData implements Serializable
{
    public String status;
    public String TransactionID;
    public String ordernumber;
    public String orderdate;
    public String ParcelID;
    public String TransID;
    public Double Amount;
    public String Paymentvia;
    public String PaymentTransaction;
    public String Txnid;
    public ArrayList<GenerateOrderData> response;
}
