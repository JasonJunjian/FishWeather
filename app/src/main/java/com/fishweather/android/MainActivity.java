package com.fishweather.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataSupport.deleteAll(Province.class);
        DataSupport.deleteAll(City.class);
        DataSupport.deleteAll(County.class);
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
}
