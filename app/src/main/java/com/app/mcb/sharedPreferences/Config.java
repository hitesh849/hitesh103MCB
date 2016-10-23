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
    public static final String USER_ID = "user_id";
    public static final String ISLOGIN = "islogin";
    public static final String USERNAME = "username";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String ROLE_ID = "role_id";
    public static final String ADDRESS = "address";
    public static final String GENDER = "gender";
    public static final String PHOTO = "photo";
    public static final String PHONE = "phone";
    public static final String COUNTRY_CODE = "country_code";
    public static final String MOBILE = "mobile";
    public static final String PINCODE = "pincode";
    public static final String BANK_ACT_NO = "bank_act_no";
    public static final String BANK_ACT_NAME = "bank_act_name";
    public static final String BANK_NAME = "bank_name";
    public static final String BANK_IFSC = "bank_ifsc";
    public static final String BANK_SWIFT_CODE = "bank_swift_code";
    public static final String WALLET = "wallet";
    public static final String PASSPORT_NO = "passport_no";
    public static final String MODIFY_USERNAME = "UserID";


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

    public static void setUserId(String userName) {
        editor.putString(USER_ID, userName);
        savePreferences();
    }

    public static String getUserId() {
        return preferences.getString(USER_ID, null);
    }

    public static void setUserName(String userName) {
        editor.putString(USERNAME, userName);
        savePreferences();
    }

    public static String getUserName() {
        return preferences.getString(USERNAME, null);
    }

    public static void setUserFirstName(String userName) {
        editor.putString(FIRST_NAME, userName);
        savePreferences();
    }

    public static String getUserFirstName() {
        return preferences.getString(FIRST_NAME, null);
    }

    public static void setUserLastName(String userName) {
        editor.putString(LAST_NAME, userName);
        savePreferences();
    }

    public static String getUserLastName() {
        return preferences.getString(LAST_NAME, null);
    }

    public static void setUserRoleId(String userName) {
        editor.putString(ROLE_ID, userName);
        savePreferences();
    }

    public static String getUserRoleId() {
        return preferences.getString(ROLE_ID, null);
    }

    public static void setUserGender(String str) {
        editor.putString(GENDER, str);
        savePreferences();
    }

    public static String getUserGender() {
        return preferences.getString(GENDER, null);
    }

    public static void setUserAddress(String str) {
        editor.putString(ADDRESS, str);
        savePreferences();
    }

    public static String getUserAddress() {
        return preferences.getString(ADDRESS, null);
    }

    public static void setUserImageURl(String str) {
        editor.putString(PHOTO, str);
        savePreferences();
    }

    public static String getUserImageURl() {
        return preferences.getString(PHOTO, null);
    }

    public static void setUserPhone(String str) {
        editor.putString(PHONE, str);
        savePreferences();
    }

    public static String getUserPhone() {
        return preferences.getString(PHONE, null);
    }

    public static void setUserCountryCode(String str) {
        editor.putString(COUNTRY_CODE, str);
        savePreferences();
    }

    public static String getUserCountryCode() {
        return preferences.getString(COUNTRY_CODE, null);
    }

    public static void setUserMobile(String str) {
        editor.putString(MOBILE, str);
        savePreferences();
    }

    public static String getUserMobile() {
        return preferences.getString(MOBILE, null);
    }

    public static void setUserPinCode(String str) {
        editor.putString(PINCODE, str);
        savePreferences();
    }

    public static String getUserPinCode() {
        return preferences.getString(PINCODE, null);
    }

    public static void setUserAccountNumber(String str) {
        editor.putString(BANK_ACT_NO, str);
        savePreferences();
    }

    public static String getUserAccountNumber() {
        return preferences.getString(BANK_ACT_NO, null);
    }


    public static void setUserAccountName(String str) {
        editor.putString(BANK_ACT_NAME, str);
        savePreferences();
    }

    public static String getUserAccountName() {
        return preferences.getString(BANK_ACT_NAME, null);
    }

    public static void setUserBankName(String str) {
        editor.putString(BANK_NAME, str);
        savePreferences();
    }

    public static String getUserBankName() {
        return preferences.getString(BANK_NAME, null);
    }

    public static void setUserBankIFSC(String str) {
        editor.putString(BANK_IFSC, str);
        savePreferences();
    }

    public static String getUserBankIFSC() {
        return preferences.getString(BANK_IFSC, null);
    }

    public static void setUserBankSwiftCode(String str) {
        editor.putString(BANK_SWIFT_CODE, str);
        savePreferences();
    }

    public static String getUserBankSwiftCode() {
        return preferences.getString(BANK_SWIFT_CODE, null);
    }

    public static void setUserWallet(String str) {
        editor.putString(WALLET, str);
        savePreferences();
    }

    public static String getUserWallet() {
        return preferences.getString(WALLET, null);
    }

    public static void setUserPassportNumber(String str) {
        editor.putString(PASSPORT_NO, str);
        savePreferences();
    }

    public static String getUserPassportNumber() {
        return preferences.getString(PASSPORT_NO, null);
    }

    public static void setUserModifyUserName(String str) {
        editor.putString(MODIFY_USERNAME, str);
        savePreferences();
    }

    public static String getUserModifyUserName() {
        return preferences.getString(MODIFY_USERNAME, null);
    }
}
