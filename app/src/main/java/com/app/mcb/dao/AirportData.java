package com.app.mcb.dao;

import java.util.ArrayList;

/**
 * Created by Hitesh on 28-09-2016.
 */
public class AirportData extends CommonResponseData {
    public static final String TABLE_NAME = "airport";
    public static final String FLD_ID = "id";
    public static final String FLD_AIRPORT_ID = "airport_id";
    public static final String FLD_LOCATION = "location";
    public static final String FLD_TYPE = "type";
    public static final String FLD_ZONE = "zone";
    public static final String FLD_STATUS = "status";
    public static final String FLD_CREATED = "created";
    public static final String FLD_ZONELIST_ID = "zonelistid";
    public static final String FLD_CODE = "code";
    public static final String FLD_ZONE_NAME = "Zonename";

    public String id;
    public String location;
    public String type;
    public String zone;
    public String created;
    public String zonelistid;
    public String code;
    public String Zonename;
    public ArrayList<AirportData> response;

    @Override
    public String toString() {
        return location;
    }

}
