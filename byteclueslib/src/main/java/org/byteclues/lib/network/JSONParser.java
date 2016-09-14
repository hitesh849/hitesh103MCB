package org.byteclues.lib.network;

import org.byteclues.lib.dao.LocationDataObject;
import org.byteclues.lib.utils.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParser {
    public static final String KEY_RESULTS = "results";
    public static final String KEY_ADDRESS_COMPONENTS = "address_components";
    public static final String KEY_GEOMETRY = "geometry";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_LONGITUDE = "lng";
    public static final String KEY_LATITUDE = "lat";
    public static final String KEY_TYPES = "types";
    public static final String KEY_ADMINISTRATIVE_AREA_LEVEL_1 = "administrative_area_level_1";
    public static final String KEY_LONG_NAME = "long_name";
    public static final String KEY_ADMINISTRATIVE_AREA_LEVEL_2 = "administrative_area_level_2";
    public static final String KEY_POSTAL_CODE = "postal_code";
    public static final String KEY_FORMATTED_ADDRESS = "formatted_address";

    public JSONParser() {

    }

    /**
     * method to get JSONObject from the specified url
     *
     * @param url
     * @return
     */
    public JSONObject getJSONFromUrl(String url) {

        URL server_url = null;
        HttpURLConnection connection = null;
        StringBuilder result = new StringBuilder();
        JSONObject jobject = null;
        try {
            server_url = new URL(url);
            connection = (HttpURLConnection) server_url.openConnection();
            InputStream stream = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            jobject = new JSONObject(result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return jobject;

    }

    /**
     * method to get latitude and longitude for the entered address
     *
     * @param youraddress
     * @return
     */
    public JSONObject getLatLongFromGivenAddress(String youraddress) {
        youraddress = youraddress.replaceAll(" ", "%20");
        return getJSONFromUrl("http://maps.google.com/maps/api/geocode/json?address=" + youraddress + "&sensor=false");
    }

    public LocationDataObject parseJsonToLocationObject(JSONObject obj) {
        LocationDataObject locationDataObject = new LocationDataObject();
        try {

            JSONArray array = obj.getJSONArray(KEY_RESULTS);
            JSONObject object = array.getJSONObject(0);
            locationDataObject.address = object.getString(KEY_FORMATTED_ADDRESS);
            JSONArray address_information = object.getJSONArray(KEY_ADDRESS_COMPONENTS);
            for (int i = 0; i < address_information.length(); i++) {
                JSONObject component_address = address_information.getJSONObject(i);
                if (Util.checkValueForKey(component_address, KEY_TYPES)) {
                    JSONArray array_obj = component_address.getJSONArray(KEY_TYPES);
                    {
                        if (array_obj.getString(0).equals(KEY_ADMINISTRATIVE_AREA_LEVEL_1)) {
                            locationDataObject.state = component_address.getString(KEY_LONG_NAME);
                        }
                        if (array_obj.getString(0).equals(KEY_ADMINISTRATIVE_AREA_LEVEL_2)) {
                            locationDataObject.city = component_address.getString(KEY_LONG_NAME);
                        }
                        if (array_obj.getString(0).equals(KEY_POSTAL_CODE)) {
                            locationDataObject.zipcode = component_address.getString(KEY_LONG_NAME);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return locationDataObject;
    }

}

