package com.teggi.isbuscoming.dba;

import android.content.Context;
import android.database.sqlite.*;
import com.teggi.isbuscoming.dba.BusStopsSchema.*;

/**
 * Created by Komoliddin on 2/26/2015.
 */
public class BusStopsDBA extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BusStops.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Favorites.TABLE_NAME + " (" +
                    Favorites._ID + " INTEGER PRIMARY KEY," +
                    Favorites.COLUMN_ID + TEXT_TYPE + COMMA_SEP +
                    Favorites.COLUMN_NAME + TEXT_TYPE  +  " )";

    public BusStopsDBA(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO write a relevant upgrade of DB
        //db.execSQL(SQL_DELETE_ENTRIES);
        //onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
