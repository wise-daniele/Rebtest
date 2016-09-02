package com.epresidential.rebtest.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by daniele on 02/09/16.
 */
public class StoreUtils {

    public static final String MAIN_PREFERENCE = "main_preference";
    //last loaded page of nyt articles
    public static final String LAST_LOADED_PAGE = "last_loaded_page";
    public static final String FIRST_LOADING = "first_loading";

    public static boolean getFirstLoading(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MAIN_PREFERENCE, Context.MODE_PRIVATE);
        return prefs.getBoolean(FIRST_LOADING, true);
    }

    public static void setFirstLoading(Context context, boolean isFirst) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                MAIN_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(FIRST_LOADING, isFirst);
        editor.apply();
    }

    public static int getLastLoadedPage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MAIN_PREFERENCE, Context.MODE_PRIVATE);
        return prefs.getInt(LAST_LOADED_PAGE, -1);
    }

    public static void setLastLoadedPage(Context context, int page) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                MAIN_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(LAST_LOADED_PAGE, page);
        editor.apply();
    }
}
