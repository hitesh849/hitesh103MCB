package com.app.mcb.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.app.mcb.dao.AirportData;

/**
 * Created by admin on 13/09/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MCB";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createCategoryTable(sqLiteDatabase);
    }

    private void createCategoryTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "
                + AirportData.TABLE_NAME + "("
                + AirportData.FLD_ID + " LONG UNIQUE ON CONFLICT REPLACE,"
                + AirportData.FLD_AIRPORT_ID + " TEXT,"
                + AirportData.FLD_LOCATION + " TEXT,"
                + AirportData.FLD_TYPE + " TEXT,"
                + AirportData.FLD_ZONE + " TEXT,"
                + AirportData.FLD_STATUS + " TEXT,"
                + AirportData.FLD_CREATED + " TEXT,"
                + AirportData.FLD_ZONELIST_ID + " TEXT,"
                + AirportData.FLD_CODE + " TEXT,"
                + AirportData.FLD_ZONE_NAME + " TEXT);");
        sqLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS airport_id on " + AirportData.TABLE_NAME + "(" + AirportData.FLD_AIRPORT_ID + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
