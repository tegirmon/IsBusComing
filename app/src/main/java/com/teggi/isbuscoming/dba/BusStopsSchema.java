package com.teggi.isbuscoming.dba;

import android.provider.BaseColumns;

/**
 * Created by Komoliddin on 2/26/2015.
 */
public class BusStopsSchema {
    public BusStopsSchema() {}

    public static abstract class Favorites implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_ID = "stopid";
        public static final String COLUMN_NAME = "name";
    }
}
