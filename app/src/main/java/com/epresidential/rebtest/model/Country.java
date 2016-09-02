package com.epresidential.rebtest.model;

import com.epresidential.rebtest.rest.JsonObject;

/**
 * Created by daniele on 01/09/16.
 */
public class Country extends JsonObject {

    String name;
    String capital;
    String alpha2Code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }
}
