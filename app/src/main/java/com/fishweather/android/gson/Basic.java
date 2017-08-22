package com.fishweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jason on 2017/8/22.
 */

public class Basic
{
    public String city;

    public String cnty;

    public String id;

    public String lat;

    public String lon;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
