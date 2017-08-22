package com.fishweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jason on 2017/8/22.
 */

public class Weather {
    private AQI aqi;

    private Basic basic;

    @SerializedName("daily_forecast")
    private List<Forecast> forecastList;

    private Now now;

    private String status;

    private Suggestion suggestion;

}
