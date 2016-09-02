package com.epresidential.rebtest.rest;

/**
 * Created by daniele on 01/09/16.
 */
public class HttpResponse {

    private boolean mSuccess;

    private String mJson;

    private HttpResponseException mException;

    public static HttpResponse success(String json) {
        return new HttpResponse(json);
    }

    public static HttpResponse failure(HttpResponseException ex) {
        return new HttpResponse(ex);
    }

    private HttpResponse(String json) {
        mSuccess = true;
        mJson = json;
    }

    private HttpResponse(HttpResponseException ex) {
        mSuccess = false;
        mException = ex;
    }

    public boolean isSuccess() {
        return mSuccess;
    }

    public HttpResponseException getException() {
        return mException;
    }

    public String getJson() {
        return mJson;
    }

}