package com.fishweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.security.PublicKey;

/**
 * Created by jason on 2017/8/22.
 */

public class Now {
    public Cond cond;

    public String fl;

    public String hum;

    public String pcpn;

    public String pres;

    public String tmp;

    public String vis;

    public class Cond{
        @SerializedName("txt")
        public String info;
    }
}
