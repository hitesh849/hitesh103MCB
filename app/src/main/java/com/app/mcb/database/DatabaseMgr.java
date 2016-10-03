package com.app.mcb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;


import com.app.mcb.dao.AirportData;

import java.util.ArrayList;

/**
 * Created by admin on 13/09/16.
 */
public class DatabaseMgr {
    private static DatabaseMgr instance;
    private static SQLiteDatabase sqLiteDb;

    private DatabaseMgr() {

    }

    public static DatabaseMgr getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseMgr();
            init(context);
        }
        return instance;
    }

    private synchronized static boolean init(Context context) {
        sqLiteDb = new DatabaseHelper(context).getWritableDatabase();
        sqLiteDb.setPageSize(4 * 1024);
        return true;
    }

    /**
     * This method is used to insert data in the table.
     *
     * @param tableName
     * @param contentValues
     * @return
     */
    private synchronized int insertRows(String tableName, ContentValues[] contentValues) {
        int retCode = -1;
        try {
            sqLiteDb.beginTransaction();
            for (ContentValues contactValue : contentValues) {
                try {
                    if (contactValue == null)
                        return 0;
                    retCode = (int) sqLiteDb.insertWithOnConflict(tableName, null, contactValue, SQLiteDatabase.CONFLICT_REPLACE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (sqLiteDb != null) {
                sqLiteDb.setTransactionSuccessful();
                sqLiteDb.endTransaction();
            }
        }
        return retCode;
    }

    /**
     * method to insert one row at a time in table
     *
     * @param tableName
     * @param contentValues
     * @return
     */
    private synchronized int insertRow(String tableName, ContentValues contentValues) {
        int retCode = -1;
        try {
            sqLiteDb.beginTransaction();
            try {
                if (contentValues == null)
                    return 0;
                retCode = (int) sqLiteDb.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (sqLiteDb != null) {
                sqLiteDb.setTransactionSuccessful();
                sqLiteDb.endTransaction();
            }
        }
        return retCode;
    }


    public long getNoOfRecords(String tableName) {
        Cursor cursor;
        long noOfEntries = 0;
        try {
            noOfEntries = DatabaseUtils.queryNumEntries(sqLiteDb, tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfEntries;
    }


    public void insertDataToAirportTable(ArrayList<AirportData> list) {
        try {

            for (int i = 0; i < list.size(); i++) {
                AirportData airportData = list.get(i);
                insertRow(AirportData.TABLE_NAME, createContentValuesFromObject(airportData));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private ContentValues createContentValuesFromObject(AirportData airportData) {
        ContentValues contentValues = null;
        try {
            contentValues = new ContentValues();
            contentValues.put(AirportData.FLD_AIRPORT_ID, airportData.id);
            contentValues.put(AirportData.FLD_LOCATION, airportData.location);
            contentValues.put(AirportData.FLD_TYPE, airportData.type);
            contentValues.put(AirportData.FLD_ZONE, airportData.zone);
            contentValues.put(AirportData.FLD_STATUS, airportData.status);
            contentValues.put(AirportData.FLD_CREATED, airportData.created);
            contentValues.put(AirportData.FLD_ZONELIST_ID, airportData.zonelistid);
            contentValues.put(AirportData.FLD_ZONE_NAME, airportData.Zonename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentValues;
    }


    public ArrayList<AirportData> getAirportList() {
        ArrayList<AirportData> airportDatas = new ArrayList<AirportData>();
        try {
            Cursor cursor = sqLiteDb.query(AirportData.TABLE_NAME, null, null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    AirportData airportData = new AirportData();
                    airportData.id = cursor.getString(cursor.getColumnIndex(AirportData.FLD_AIRPORT_ID));
                    airportData.location = cursor.getString(cursor.getColumnIndex(AirportData.FLD_LOCATION));
                    airportData.zone = cursor.getString(cursor.getColumnIndex(AirportData.FLD_ZONE));
                    airportData.status = cursor.getString(cursor.getColumnIndex(AirportData.FLD_STATUS));
                    airportData.created = cursor.getString(cursor.getColumnIndex(AirportData.FLD_CREATED));
                    airportData.zonelistid = cursor.getString(cursor.getColumnIndex(AirportData.FLD_ZONELIST_ID));
                    airportData.Zonename = cursor.getString(cursor.getColumnIndex(AirportData.FLD_ZONE_NAME));
                    airportDatas.add(airportData);
                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return airportDatas;
    }


    public void clearDB() {
        sqLiteDb.execSQL("delete from " + AirportData.TABLE_NAME);
    }
}
