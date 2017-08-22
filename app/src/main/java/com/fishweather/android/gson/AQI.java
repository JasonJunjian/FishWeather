package com.fishweather.android.gson;

/**
 * Created by jason on 2017/8/22.
 */

public class AQI {
    public AQICity city;

    public class AQICity{
        public String aqi;

        public String co;

        public String no2;

        public String o3;

        public String pm10;

        public String pm25;

        public String qlty;

        public String so2;
    }
}
