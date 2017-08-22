package com.fishweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jason on 2017/8/22.
 */

public class Forecast {
    public Cond cond;
    public String date;
    public Tmp tmp;

    public class Cond{
        @SerializedName("txt_d")
        public String info;
    }

    public class Tmp{
        public String max;
        public String min;
    }
}
