package com.fishweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jason on 2017/8/22.
 */

public class Weather {
    public AQI aqi;

    public Basic basic;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

    public Now now;

    public String status;

    public Suggestion suggestion;

}
