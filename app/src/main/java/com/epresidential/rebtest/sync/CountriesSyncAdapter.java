package com.epresidential.rebtest.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.epresidential.rebtest.R;
import com.epresidential.rebtest.data.RebtestContract;
import com.epresidential.rebtest.model.Countries;
import com.epresidential.rebtest.model.Country;
import com.epresidential.rebtest.rest.HttpResponseException;
import com.epresidential.rebtest.rest.JsonHttpResponseListener;
import com.epresidential.rebtest.rest.RestCountriesClient;
import com.epresidential.rebtest.utils.Constants;

import java.util.Vector;

/**
 * Created by daniele on 04/09/16.
 */
public class CountriesSyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = CountriesSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the countries, in seconds.
    // 60 seconds (1 minute) * 360 = 6 hours
    public static final int SYNC_INTERVAL = 60 * 360;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    public CountriesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");
        RestCountriesClient.with(getContext()).getAllCountries(new JsonHttpResponseListener<Countries>(Countries.class) {
            @Override
            public void onFailure(HttpResponseException ex) {
                Log.e(LOG_TAG, ex.getMessage(), ex);
            }

            @Override
            public void onSuccess(Countries result) {

                //Check first loading Not necessary as countries will be updated periodically
                /*
                if(StoreUtils.getFirstLoading(getContext())){
                    StoreUtils.setFirstLoading(getContext(), false);
                }
                */

                //delete old values from db
                Uri countriesUri = RebtestContract.CountryEntry.CONTENT_URI;
                int deleted = getContext().getContentResolver().delete(countriesUri, null, null);
                Log.d(LOG_TAG, deleted + " entries deleted");
                //StoreUtils.setLastLoadedPage(getContext(), page);
                Country[] countries = result.getCountries();

                Vector<ContentValues> cVVector = new Vector<ContentValues>(10);
                for(int i = 0; i<countries.length; i++){
                    //for each received article create the ContentValues object containing the relevant info
                    Country country = countries[i];
                    ContentValues countryValues = new ContentValues();
                    countryValues.put(RebtestContract.CountryEntry.COLUMN_COUNTRY_NAME, country.getName());
                    countryValues.put(RebtestContract.CountryEntry.COLUMN_CAPITAL, country.getCapital());
                    countryValues.put(RebtestContract.CountryEntry.COLUMN_POPULATION, country.getPopulation());
                    countryValues.put(RebtestContract.CountryEntry.COLUMN_COUNTRY_CODE, country.getAlpha2Code());
                    String urlFlag = Constants.FLAGS_BASE_URL + country.getAlpha2Code().toLowerCase() + ".png";
                    countryValues.put(RebtestContract.CountryEntry.COLUMN_FLAG_WEB_URL, urlFlag);
                    cVVector.add(countryValues);
                }
                int inserted = 0;
                // add articles to database with just one operation
                if ( cVVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = getContext().getContentResolver().bulkInsert(RebtestContract.CountryEntry.CONTENT_URI, cvArray);
                }
            }
        });
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        CountriesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

}
