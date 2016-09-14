package org.byteclues.lib.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 10/26/2015.
 */
public class SharedPreferencesLib {
    private static Context mContext;
    private static SharedPreferences preference = null;
    private static SharedPreferences.Editor editor;
    private static final String PREFERENCES_FILE_NAME = "preferences";
    private static final String KEY_USER_TOKEN = "user_token";

    public static void init(Context mContext) {
        SharedPreferencesLib.mContext = mContext;
        SharedPreferencesLib.preference = mContext.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferencesLib.editor = SharedPreferencesLib.preference.edit();
    }

    public static void savePreferences() {
        editor.commit();
    }

    public static void clearPreferences() {
        editor.clear();
        savePreferences();
    }

    public static void removeFromPreferences(String key) {
        if (key != null && !key.isEmpty()) {
            editor.remove(key);
            savePreferences();
        }
    }

    public static void setUserToken(String token) {
        editor.putString(KEY_USER_TOKEN, token);
        savePreferences();
    }

    public static String getUserToken() {
        return preference.getString(KEY_USER_TOKEN, "");
    }

}

