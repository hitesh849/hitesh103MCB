package org.byteclues.lib.init;

import android.content.Context;

import org.byteclues.lib.database.DBHelper;


/**
 * Created by admin on 19-07-2015.
 */
public class Env {
    public static Context appContext;
    public static Context currentActivity;
    public static DBHelper dbHelper;
    public static String logFilePath;
    public static boolean isDebugMode;
    public static applicationState APP_STATE;

    public static void init(Context appContext, DBHelper dbHelper, String logFilePath, boolean isDebugMode) {
        Env.appContext = appContext;
        Env.dbHelper = dbHelper;
        Env.logFilePath = logFilePath;
        Env.isDebugMode = isDebugMode;
    }

    public enum applicationState {
        FOREGROUND, BACKGROUND;
    }
}
