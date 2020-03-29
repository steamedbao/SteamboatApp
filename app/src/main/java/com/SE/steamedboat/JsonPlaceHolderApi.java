package com.SE.steamedboat;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    @GET("latest?symbols=SGD")
    Call<CurrencyExchange2> getCurrencyExchange2(@Query("base") String baseCurrency);
}
