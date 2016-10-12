package com.app.mcb.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hitesh on 12-10-2016.
 */
public class WithDrawData implements Serializable
{
    public String status;
    public String errorMessage;
    public String id;
    public String trans_id;
    public String amount;
    public String bank_name;
    public String acct_no;
    public String re_acct_no;
    public String ifsc;
    public String created;
    public String swift_code;
    public String act_name;
    public String TransactionID;
    public String withdrawID;
    public ArrayList<WithDrawData> response;
}
