package com.epresidential.rebtest.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by daniele on 01/09/16.
 */
public class Client {

    public static final String LOG_TAG = "Rest";
    private static final int CONNECTION_TIMEOUT = (int) TimeUnit.SECONDS.toMillis(15);
    private static final int READ_TIMEOUT = (int) TimeUnit.SECONDS.toMillis(15);

    private enum HttpMethod {
        GET,
        POST
    }

    private Context mContext;

    protected Client(Context context) {
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }

    /**
     * Implementation of async get request.
     */
    public void getAsync(final String url, final HttpResponseListener listener, final HttpHeader... headers) {
        (new AsyncTask<Void,Void,HttpResponse>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (listener != null) {
                    listener.onPrepare();
                }
            }

            @Override
            protected HttpResponse doInBackground(Void... voids) {
                return getSync(url);
            }

            @Override
            protected void onPostExecute(HttpResponse response) {
                super.onPostExecute(response);
                if (listener != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(response.getJson());
                    } else {
                        listener.onFailure(response.getException());
                    }
                }
            }

        }).execute();
    }

    /**
     * Implementation of sync get request.
     */
    public HttpResponse getSync(final String address, final HttpHeader... headers) {
        return request(HttpMethod.GET, address, null);
    }

    public HttpResponse request(HttpMethod method, String address, JsonObject object, HttpHeader... headers) {
        HttpURLConnection conn = null;
        InputStream in = null;
        OutputStream out = null;
        HttpResponse response = null;
        try {
            // building api url
            URL url = new URL(address);
            Log.i(LOG_TAG, method + " URL " + url.toString());
            // establishing connection with server
            conn = (HttpURLConnection) url.openConnection();
            // building headers
            buildHeaders(conn);
            for (HttpHeader header : headers) {
                conn.setRequestProperty(header.getKey(), header.getValue());
            }
            // defining connection params
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod(method.toString());
            conn.setDoInput(true);
            // getting response
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                in = new BufferedInputStream(conn.getInputStream());
                // building json string from stream
                StringBuilder sb = new StringBuilder();
                int b;
                while ((b = in.read()) != -1) {
                    sb.append((char) b);
                }
                String json = sb.toString().replace("\n","");
                response = HttpResponse.success(json);
                in.close();
            } else {
                response = HttpResponse.failure(new HttpResponseException(code, "server error"));
            }
        } catch (MalformedURLException ex) {
            Log.e(LOG_TAG, "malformed url");
            Log.e(LOG_TAG, ex.getMessage(), ex);
            response = HttpResponse.failure(new HttpResponseException(400, ex.getMessage(), ex));
        } catch (IOException ex) {
            Log.e(LOG_TAG, "I/O exception");
            Log.e(LOG_TAG, ex.getMessage(), ex);
            response = HttpResponse.failure(new HttpResponseException(500, ex.getMessage(), ex));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return response;
    }

    protected void buildHeaders(HttpURLConnection connection) {
        connection.setRequestProperty("Content-Type", "application/json");
    }

}