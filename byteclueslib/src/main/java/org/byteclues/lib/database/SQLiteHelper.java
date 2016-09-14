package org.byteclues.lib.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.byteclues.lib.init.Env;


/**
 * Created by admin on 19-07-2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(){
        super(Env.appContext, Env.dbHelper.getDBName(), null, Env.dbHelper.getDBVersion());
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Env.dbHelper.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Env.dbHelper.onUpgrade(db, oldVersion, newVersion);
    }
}
