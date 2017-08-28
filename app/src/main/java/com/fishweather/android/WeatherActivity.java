package com.fishweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fishweather.android.gson.Forecast;
import com.fishweather.android.gson.Weather;
import com.fishweather.android.service.AutoUpdateService;
import com.fishweather.android.util.HttpUtil;
import com.fishweather.android.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private TextView titleUpdatetime;
    private TextView cityName;
    private TextView cityWeatherText;
    private TextView cityDegree;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pmText;
    private ScrollView weatherLayout;
    private ImageView bingImage;
    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    private Button navBtn;
    private String weatherId;
    private ChooseAreaFragment chooseArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View docerView = getWindow().getDecorView();
            docerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                    .SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        titleUpdatetime = (TextView) findViewById(R.id.title_updatetime);
        cityName = (TextView) findViewById(R.id.now_cityName);
        cityWeatherText = (TextView) findViewById(R.id.now_weatherTxt);
        cityDegree = (TextView) findViewById(R.id.now_weatherDegree);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pmText = (TextView) findViewById(R.id.pm_text);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        bingImage = (ImageView) findViewById(R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navBtn = (Button)findViewById(R.id.nav_button);
        chooseArea = (ChooseAreaFragment) getSupportFragmentManager().findFragmentById(R.id.choose_area_fragment);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherStr = preferences.getString("weather", null);
       // String bingImageStr = preferences.getString("bingImage", null);

//        if (!TextUtils.isEmpty(bingImageStr)) {
//            Glide.with(this).load(bingImageStr).into(bingImage);
//        } else {
//            loadBingPic();
//        }
        if (TextUtils.isEmpty(weatherStr)) {
            weatherId = getIntent().getStringExtra("weather_id");
            requestWeather(weatherId);
        } else {
            Weather weather = Utility.convertWeatherResponse(weatherStr);
            weatherId = weather.basic.id;
            showWeatherInfo(weather);
            requestWeather(weatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    public void requestWeather(final String weatherId) {
        this.weatherId = weatherId;
        String weatherUrl = "http://guolin.tech/api/weather/?cityid=" + weatherId +
                "&key=5a752d4f0d144b368e57354e67bdc85d";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String body = response.body().string();
                final Weather weather = Utility.convertWeatherResponse(body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", body);
                            editor.apply();
                            weather.weatherImage = getWeatherImage( weather.now.cond.info);
                            showWeatherInfo(weather);
                            Toast.makeText(WeatherActivity.this, "更新成功", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private String getWeatherImage(String weatherInfo){
        String imageUrl = "http://i.tq121.com.cn/i/jsyb/d00.jpg";
        if(weatherInfo.equals("晴")){
            imageUrl = "http://i.tq121.com.cn/i/jsyb/d00.jpg";
        }else if(weatherInfo.contains("云")){
            imageUrl = "http://i.tq121.com.cn/i/jsyb/d01.jpg";
        }else if(weatherInfo.contains("阴")){
            imageUrl = "http://i.tq121.com.cn/i/jsyb/d02.jpg";
        }else if(weatherInfo.contains("雷")){
            imageUrl = "http://i.tq121.com.cn/i/jsyb/d03.jpg";
        }else if(weatherInfo.contains("雪")){
            imageUrl = "http://i.tq121.com.cn/i/jsyb/d17.jpg";
        }else if(weatherInfo.contains("雨")){
            imageUrl = "http://i.tq121.com.cn/i/jsyb/d03.jpg";
        }else if(weatherInfo.contains("雾")){
            imageUrl = "http://i.tq121.com.cn/i/jsyb/d18.jpg";
        }else if(weatherInfo.contains("霾")||weatherInfo.contains("沙")||weatherInfo.contains("尘")){
            imageUrl = "http://i.tq121.com.cn/i/jsyb/d20.jpg";
        }
        return imageUrl;
    }
    private void showWeatherInfo(Weather weather) {
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String name = weather.basic.city;
        String degree = " " + weather.now.tmp + "°";
        String weatherInfo = weather.now.cond.info;
        titleUpdatetime.setText(updateTime);
        cityName.setText(name);
        cityWeatherText.setText(weatherInfo);
        cityDegree.setText(degree);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = (View) LayoutInflater.from(WeatherActivity.this).inflate(R.layout
                    .forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);

            dateText.setText(forecast.date);
            infoText.setText(forecast.cond.info);
            maxText.setText(forecast.tmp.max);
            minText.setText(forecast.tmp.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pmText.setText(weather.aqi.city.pm25);
        }
        Glide.with(this).load(weather.weatherImage).into(bingImage);

        Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
        startService(intent);
    }

    private void loadBingPic() {
        String url = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String body = "https://cn.bing.com/ImageResolution.aspx?w=375&h=667&hash=549135d4769e29708ca72d1ba4c90e9e&intlF=";//response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(body)) {
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("bingImage", body);
                            editor.apply();
                            Glide.with(WeatherActivity.this).load(body).into(bingImage);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.START)){
            if(chooseArea.back()){
                drawerLayout.closeDrawers();
            }
        }else{
            finish();
        }
    }
}
