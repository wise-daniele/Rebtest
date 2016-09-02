package com.epresidential.rebtest.rest;

import android.util.Log;

import com.google.gson.JsonSyntaxException;

/**
 * Created by daniele on 01/09/16.
 */
public abstract class JsonHttpResponseListener<T> extends HttpResponseListener {

    private static final String LOG_TAG = JsonHttpResponseListener.class.getSimpleName();

    private JsonHttpResponseParser<T> mJsonParser;

    public JsonHttpResponseListener(Class<T> clazz) {
        mJsonParser = new JsonHttpResponseParser<>(clazz);
    }

    @Override
    public void onPrepare() {
    }

    @Override
    public final void onSuccess(String json) {
        Log.d(LOG_TAG, json);
        try {
            onSuccess(mJsonParser.parse(json));
        } catch (JsonSyntaxException ex) {
            onFailure(new HttpResponseException(-1, ex.getMessage(), ex));
        }
    }

    public abstract void onSuccess(T result);

}