package org.byteclues.lib.network;

import android.text.TextUtils;

import org.byteclues.lib.utils.Util;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 19-07-2015.
 */
public class NetworkResponse {
    public static final int NET_RESP_SUCCESS = 1;

    public enum ResponseCode {
        OK(0), TIMEOUT(1), EXCEPTION(2), FILE_NOT_FOUND(3), EMPTY_URL(4);

        private final int code;

        private ResponseCode(int code) {
            this.code = code;
        }
    }

    public ResponseCode netRespCode = ResponseCode.OK;
    public String respStr = null;
    private JSONObject jsonObject = null;

    public JSONObject getJsonObject() {
        if (jsonObject == null) {
            toJson();
        }
        return jsonObject;
    }

    private void toJson() {
        try {
            if (respStr != null)
                jsonObject = new JSONObject(respStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NetworkResponse() {
        netRespCode = ResponseCode.OK;
        respStr = null;
    }

    public JSONObject getJSonObjectUsingKey(String key)
    {
        JSONObject jsonObject1=null;
        try {
            if(Util.checkValueForKey(jsonObject, key))
             jsonObject1=jsonObject.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1;
    }
    public String getMessageFromServer()
    {
        String errorMsg="Server not responding.";
        if(jsonObject==null) {
            getJsonObject();
        }
        try
        {
            if(jsonObject!=null) {
                errorMsg = jsonObject.getString("msg");
            }
            else
            {
                if(!Util.isDeviceOnline())
                {
                    errorMsg="Internet not available.";
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return errorMsg;
    }


    public String getStatusCode()
    {
        String status_code="";
        if(jsonObject==null) {
            getJsonObject();
        }
        try
        {
            if(jsonObject!=null&&Util.checkValueForKey(jsonObject,"status_code")) {
                status_code = jsonObject.getString("status_code");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return status_code;
    }

    public boolean isSuccess() {
        if (TextUtils.isEmpty(respStr)) {
            return false;
        }
        try {
            String status = (String) getJsonObject().get(Util.FLD_STATUS);
           if(getStatusCode().equals(Util.STATUS_CODE_USER_LOGOUT))
               return false;
            else
            return status.equalsIgnoreCase(Util.VAL_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
