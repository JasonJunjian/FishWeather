package com.fishweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fishweather.android.db.City;
import com.fishweather.android.db.County;
import com.fishweather.android.db.Province;
import com.fishweather.android.util.HttpUtil;
import com.fishweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ChooseAreaFragment choose_area;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherStr = preferences.getString("weather", null);
        if (!TextUtils.isEmpty(weatherStr)) {
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }
        choose_area = (ChooseAreaFragment) getSupportFragmentManager().findFragmentById(R.id.choose_area);
//        HttpUtil.sendOkHttpRequest("http://guolin.tech/api/china", new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();
//                Utility.convertProvinceResponse(result);
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        if(choose_area.back()){
            finish();
        }

    }
}
