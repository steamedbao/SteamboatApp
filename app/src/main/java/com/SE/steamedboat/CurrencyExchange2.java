package com.SE.steamedboat;

import com.SE.steamedboat.API.Rate;

public class CurrencyExchange2 {
    private String base;
    private String date;
    private Rate rates;

    public float getRates() {
        return rates.getSGD();
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
