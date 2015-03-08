package com.teggi.isbuscoming.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.teggi.isbuscoming.dba.BusStopsDBA;
import com.teggi.isbuscoming.dba.BusStopsSchema.*;

/**
 * Created by Komoliddin on 2/26/2015.
 */
public class BusStopsHelper {

    public static Cursor listBusStops(Context context){
        BusStopsDBA mDbHelper = new BusStopsDBA(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Favorites._ID,
                Favorites.COLUMN_ID,
                Favorites.COLUMN_NAME
        };

        Cursor cursor = db.query(
                Favorites.TABLE_NAME,
                projection,
                null, null, null, null, null
        );

        return cursor;
    }

    public static void addBusStop(Context context, int id, String title){
        BusStopsDBA mDbHelper = new BusStopsDBA(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Favorites.COLUMN_ID, id);
        values.put(Favorites.COLUMN_NAME, title);

        db.insert(
                Favorites.TABLE_NAME,
                null,
                values);
    }

    public static void deleteBusStop(Context context, String id){
        BusStopsDBA mDbHelper = new BusStopsDBA(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = Favorites.COLUMN_ID + " LIKE ? ";
        String[] selectionArgs = { String.valueOf(id) };

        db.delete(Favorites.TABLE_NAME, selection, selectionArgs);
    }


}
