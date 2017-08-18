package com.fishweather.android;

import android.app.Application;

import com.facebook.stetho.Stetho;

import org.litepal.LitePal;

/**
 * Created by yujj on 2017/8/18.
 */

public class FishApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        Stetho.initializeWithDefaults(this);
    }
}
