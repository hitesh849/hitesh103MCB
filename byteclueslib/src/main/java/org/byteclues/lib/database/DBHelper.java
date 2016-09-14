package org.byteclues.lib.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by admin on 19-07-2015..
 */
public interface DBHelper {
    public int getDBVersion();
    public String getDBName();
    public void onCreate(SQLiteDatabase db);
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
