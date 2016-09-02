package com.epresidential.rebtest.rest;

/**
 * Created by daniele on 01/09/16.
 */
public class HttpResponseException extends Exception {

    private int mCode;

    public HttpResponseException(int code, String message) {
        super(message);
        mCode = code;
    }

    public HttpResponseException(int code, String message, Throwable throwable) {
        super(message, throwable);
        mCode = code;
    }

    public int getHttpCode() {
        return mCode;
    }

}