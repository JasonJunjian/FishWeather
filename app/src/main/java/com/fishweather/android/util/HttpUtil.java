package com.fishweather.android.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by jason on 2017/8/17.
 */

public class HttpUtil {

    public  static void sendOkHttpRequest(String addressUrl, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(addressUrl).build();
        client.newCall(request).enqueue(callback);
    }
}
