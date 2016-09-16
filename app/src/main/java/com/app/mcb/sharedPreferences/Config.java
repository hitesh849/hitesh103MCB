package com.app.mcb.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by admin on 8/9/2016.
 */

public class Config {
    private static SharedPreferences preferences = null;
    private static SharedPreferences.Editor editor;
    public static String PREFERENCES_NAME = "mcbPreferences";
    public static final String ISLOGIN = "islogin";

    public static void init(Context mContext) {
        Config.preferences = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Config.editor = preferences.edit();
    }

    public static void clearPreferences() {
        editor.clear();
        savePreferences();
    }

    public static void savePreferences() {
        editor.commit();
    }



    public static void removeKey(String key) {
        editor.remove(key);
        editor.apply();
    }

    public static void setLoginStatus(boolean loginStatus) {
        editor.putBoolean(ISLOGIN, loginStatus);
        savePreferences();
    }

    public static boolean getLoginStatus() {
        return preferences.getBoolean(ISLOGIN, false);
    }
}
