package com.epresidential.rebtest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.epresidential.rebtest.data.RebtestContract.CountryEntry;

/**
 * Created by daniele on 01/09/16.
 */
public class RebtestDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "rebtest.db";

    public RebtestDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold countries.
        final String SQL_CREATE_ARTICLE_TABLE = "CREATE TABLE " + CountryEntry.TABLE_NAME + " (" +
                CountryEntry._ID + " INTEGER PRIMARY KEY," +
                CountryEntry.COLUMN_COUNTRY_NAME + " TEXT NOT NULL, " +
                CountryEntry.COLUMN_COUNTRY_CODE + " TEXT UNIQUE NOT NULL, " +
                CountryEntry.COLUMN_CAPITAL + " TEXT NOT NULL, " +
                CountryEntry.COLUMN_FLAG_WEB_URL + " TEXT" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_ARTICLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CountryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}