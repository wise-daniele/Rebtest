package com.epresidential.rebtest.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by daniele on 04/09/16.
 */
public class CountriesAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private CountriesAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new CountriesAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
