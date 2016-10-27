package com.app.mcb.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hitesh on 11-10-2016.
 */
public class ParcelListData implements Serializable {
    public String status;
    public String errorMessage;
    public ArrayList<ParcelDetailsData> response;
}
