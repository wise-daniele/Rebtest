package com.epresidential.rebtest.rest;

import android.content.Context;

/**
 * Created by daniele on 01/09/16.
 */
public class RebtestRestClient extends Client {

    private static final String DEFAULT_CONTENT = "";


    protected RebtestRestClient(Context context) {
        super(context);
    }

    protected String buildParams(String... params){
        String paramsStr = "?";
        for(int i = 0; i<params.length; i++){
            if(i < params.length - 1){
                paramsStr = paramsStr + params[i] + "&";
            }
            else{
                paramsStr = paramsStr + params[i];
            }
        }
        return paramsStr;
    }

    protected String buildUrl(String baseUrl, String path) {
        return baseUrl + path;
    }

    protected void get(String url, HttpResponseListener listener, boolean async, HttpHeader... headers) {

        if (async) {
            getAsync(url, listener, headers);
        } else {
            HttpResponse response = getSync(url, headers);
            handleResponse(response, listener);
        }
    }

    private void handleResponse(HttpResponse response, HttpResponseListener listener) {
        if (listener != null) {
            if (response.isSuccess()) {
                listener.onSuccess(response.getJson());
            } else {
                listener.onFailure(response.getException());
            }
        }
    }
}