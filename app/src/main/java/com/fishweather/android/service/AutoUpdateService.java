package com.fishweather.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.widget.Toast;

import com.fishweather.android.WeatherActivity;
import com.fishweather.android.gson.Weather;
import com.fishweather.android.util.HttpUtil;
import com.fishweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  null;
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        updateWeather();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60*60*1*1000;
        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi =  PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent,flags,startId);
    }

    public void updateWeather() {
        String weatherId = "北京";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
        String weatherStr = preferences.getString("weather", null);
        if (!TextUtils.isEmpty(weatherStr)) {
            Weather weather = Utility.convertWeatherResponse(weatherStr);
            weatherId = weather.basic.id;
        }
        String weatherUrl = "http://guolin.tech/api/weather/?cityid=" + weatherId +
                "&key=5a752d4f0d144b368e57354e67bdc85d";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String body = response.body().string();
                final Weather weather = Utility.convertWeatherResponse(body);
                if (weather != null && "ok".equals(weather.status)) {
                    SharedPreferences.Editor editor = PreferenceManager
                            .getDefaultSharedPreferences(AutoUpdateService.this).edit();
                    editor.putString("weather", body);
                    editor.apply();
                }
            }
        });
    }
}
