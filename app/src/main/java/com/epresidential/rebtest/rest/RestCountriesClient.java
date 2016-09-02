package com.epresidential.rebtest.rest;

import android.content.Context;
import android.provider.SyncStateContract;

import com.epresidential.rebtest.utils.Constants;

/**
 * Created by daniele on 01/09/16.
 */
public class RestCountriesClient extends RebtestRestClient {

    private static final String LOG_TAG = RestCountriesClient.class.getSimpleName();
    private static final String BASE_URL = "https://restcountries.eu/rest/v1";
    private static RestCountriesClient instance;

    public static synchronized RestCountriesClient with(Context context) {
        if (instance == null) {
            if (context == null) {
                throw new IllegalStateException("context not defined for rest client");
            }
            instance = new RestCountriesClient(context);
        }
        return instance;
    }

    public static RestCountriesClient getInstance() {
        return instance;
    }

    protected RestCountriesClient(Context context) {
        super(context);
    }

    public synchronized void getAllCountries(HttpResponseListener listener){
        final String url = buildUrl(BASE_URL, Constants.REST_COUNTRIES_ALL_PATH);
        get(url, listener, true);
    }

    public synchronized void getCountryByCurrency(String currency, HttpResponseListener listener){
        final String url = buildUrl(BASE_URL, Constants.REST_COUNTRIES_CURRENCY_PATH + currency);
        get(url, listener, true);
    }
}
