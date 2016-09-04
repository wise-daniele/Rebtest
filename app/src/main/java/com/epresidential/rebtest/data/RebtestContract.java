package com.epresidential.rebtest.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by daniele on 01/09/16.
 */
public class RebtestContract {

    public static final String CONTENT_AUTHORITY = "com.epresidential.rebtest.app";
    // CONTENT AUTORITY for the access to the DB of this app
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // Path to the table Article
    public static final String PATH_COUNTRY = "country";

    /* Inner class that defines the table contents of the location table */
    public static final class CountryEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COUNTRY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY;

        // Table name
        public static final String TABLE_NAME = "country";
        public static final String COLUMN_COUNTRY_NAME = "country_name";
        public static final String COLUMN_COUNTRY_CODE = "country_code";
        public static final String COLUMN_CAPITAL = "capital";
        public static final String COLUMN_POPULATION = "population";
        public static final String COLUMN_FLAG_WEB_URL = "flag_web_url";

        public static Uri buildCountryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
