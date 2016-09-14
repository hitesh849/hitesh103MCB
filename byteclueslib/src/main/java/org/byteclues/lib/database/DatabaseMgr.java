package org.byteclues.lib.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import org.byteclues.lib.logger.Logger;

/**
 * Created by by admin on 19-07-2015.
 */
public class DatabaseMgr {
    public static String TAG = "DatabaseMgr";

   public static SQLiteDatabase sqLiteDb = null;
    // public SQLiteDatabase sqLiteDbRO = null;
    private static DatabaseMgr instance = null;

    public synchronized static DatabaseMgr getInstance() throws InstantiationException, IllegalAccessException {
        if (instance == null) {
            instance = new DatabaseMgr();
            instance.init();
        }
        return instance;
    }

    /**
     * This method is called to initialize database module.
     */
    private synchronized static boolean init() throws IllegalAccessException, InstantiationException {
        SQLiteHelper sqLiteHelper = new SQLiteHelper();
        sqLiteDb = sqLiteHelper.getWritableDatabase();
        // sqLiteDbRO = dbHelper.getReadableDatabase();
        sqLiteDb.setPageSize(4 * 1024);// default is 1 K
        return true;
    }

    /**
     * This method is used to insert data in the table.
     *
     * @param tableName
     * @param contentValues
     * @return
     */
    public synchronized static int insertRow(String tableName, ContentValues[] contentValues) {

        if (Logger.IS_DEBUG)
            Logger.info(TAG, "insertRow(): + tableName [" + tableName + "] values [" + ((contentValues != null) ? contentValues.toString() : "") + "]");
        int retCode = -1;
        try {
            getInstance().sqLiteDb.beginTransaction();
            for (ContentValues contactValue : contentValues) {
                try {
                    if (contactValue == null)
                        return 0;
                    retCode = (int) sqLiteDb.insertWithOnConflict(
                            tableName, null, contactValue,
                            SQLiteDatabase.CONFLICT_REPLACE);
                } catch (Exception e) {
                    if (Logger.IS_DEBUG) {
                        Logger.error(TAG, "insertRow(): Exception [" + e + "] tableName [" + tableName + "] values [" + ((contentValues != null) ? contentValues.toString() : "") + "]");
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            if (Logger.IS_DEBUG) {
                Logger.error(TAG, "insertRow(): Exception [" + e + "] tableName [" + tableName + "] values [" + ((contentValues != null) ? contentValues.toString() : "") + "]");
                e.printStackTrace();
            }
        } finally {
            if (sqLiteDb != null){
                sqLiteDb.setTransactionSuccessful();
                sqLiteDb.endTransaction();
            }
        }
        return retCode;
    }

    /**
     * This method is used to insert data in the table.
     *
     * @param tableName
     * @param contentValues
     * @return number of insert value
     */
    public synchronized static int insertRows(String tableName,ContentValues[] contentValues) {
        if (Logger.IS_DEBUG)
            Logger.info(TAG, "insertRows(): tableName [" + tableName + "] values [" + ((contentValues != null) ? contentValues.toString() : "" + "]"));

        int numberOfRowInsert = 0;
        try {
            getInstance().sqLiteDb.beginTransaction();
            for (ContentValues contactValue : contentValues) {
                try {
                    if (contactValue == null)
                        return 0;
                    sqLiteDb.insertWithOnConflict(tableName, null,contactValue, SQLiteDatabase.CONFLICT_REPLACE);
                    numberOfRowInsert++;
                } catch (Exception e) {
                    if (Logger.IS_DEBUG) {
                        Logger.error(TAG, "insertRows(): exception ["
                                + e + "] tableName [" + tableName + "] values [" + ((contentValues != null) ? contentValues.toString() : "" + "]"));
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            if (Logger.IS_DEBUG) {
                Logger.error(TAG, "insertRows(): exception ["
                        + e + "] tableName [" + tableName + "] values [" + ((contentValues != null) ? contentValues.toString() : "" + "]"));
                e.printStackTrace();
            }
        } finally {
            if (sqLiteDb != null){
                sqLiteDb.setTransactionSuccessful();
                sqLiteDb.endTransaction();
            }
        }
        return numberOfRowInsert;
    }

    /**
     * This method is used to updated data in the table.
     *
     * @param tableName
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public synchronized static int updateRow(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        if (Logger.IS_DEBUG) {
            Logger.info(TAG, "updateRow(): tableName [" + tableName + "] values [" + ((values != null) ? values.toString() : "") +
                    "] where [" + whereClause + "] where Args [" + ((whereArgs != null) ? whereArgs.toString() : ""));
        }
        int result = -1;
        try {
            getInstance().sqLiteDb.beginTransaction();
            result = getInstance().sqLiteDb.update(tableName, values,
                    whereClause, whereArgs);
        } catch (Exception e) {
            if (Logger.IS_DEBUG) {
                Logger.error(TAG, "updateRow(): Exception [" + e +
                        "] tableName [" + tableName + "] values [" + ((values != null) ? values.toString() : "") +
                        "] where [" + whereClause + "] where Args [" + ((whereArgs != null) ? whereArgs.toString() : ""));
                e.printStackTrace();
            }
        } finally {
            if (sqLiteDb != null){
                sqLiteDb.setTransactionSuccessful();
                sqLiteDb.endTransaction();
            }
        }
        return result;
    }
}
