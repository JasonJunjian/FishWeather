package com.fishweather.android.util;

import android.text.TextUtils;

import com.fishweather.android.db.City;
import com.fishweather.android.db.County;
import com.fishweather.android.db.Province;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by jason on 2017/8/17.
 */

public class Utility {

    /*
    * 解析和处理服务器返回的省级数据
    * */
    public  static boolean convertProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Province>>() {
                }.getType();
                ArrayList<Province> provinces = gson.fromJson(response,type);
                for(Province province : provinces){
                    province.save();
                }
                return  true;
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return  false;
    }
    /*
    * 解析和处理服务器返回的城市数据
    * */
    public  static boolean convertCityResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<City>>() {
                }.getType();
                ArrayList<City> citys = gson.fromJson(response,type);
                for(City city : citys){
                    city.save();
                }
                return  true;
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return  false;
    }
    /*
    * 解析和处理服务器返回的省级数据
    * */
    public  static boolean convertCountyResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<County>>() {
                }.getType();
                ArrayList<County> countys = gson.fromJson(response,type);
                for(County county : countys){
                    county.save();
                }
                return  true;
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return  false;
    }
}
