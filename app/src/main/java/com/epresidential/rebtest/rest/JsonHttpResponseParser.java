package com.epresidential.rebtest.rest;

import com.epresidential.rebtest.model.Countries;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by daniele on 01/09/16.
 */
public class JsonHttpResponseParser<T> {

    private Class<T> mClass;

    public JsonHttpResponseParser(Class<T> clazz) {
        mClass = clazz;
    }

    public T parse(String json) throws JsonSyntaxException {
        if(json.startsWith("[")){
            if(mClass.getName().equals(Countries.class.getName())){
                json = "{countries: " + json + "}";
            }
        }
        return getGson().fromJson(json, mClass);
    }

    private Gson getGson() {
        return new Gson();
    }

}