package com.epresidential.rebtest.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by daniele on 04/09/16.
 */
public class CountriesSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static CountriesSyncAdapter sCountriesSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("CountriesSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sCountriesSyncAdapter == null) {
                sCountriesSyncAdapter = new CountriesSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sCountriesSyncAdapter.getSyncAdapterBinder();
    }
}
