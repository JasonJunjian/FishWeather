package com.fishweather.android.util;

import android.text.TextUtils;

import com.fishweather.android.db.City;
import com.fishweather.android.db.County;
import com.fishweather.android.db.Province;

import org.json.JSONArray;
import org.json.JSONObject;

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
                JSONArray  jsonArray = new JSONArray(response);
                for ( int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.setProvinceName(jsonObject.getString("name"));
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
    public  static boolean convertCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray  jsonArray = new JSONArray(response);
                for ( int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setCityName(jsonObject.getString("name"));
                    city.setProvinceCode(provinceId);
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
    public  static boolean convertCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray  jsonArray = new JSONArray(response);
                for ( int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    County county = new County();
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.setCountyName(jsonObject.getString("name"));
                    county.setCtiyId (cityId);
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
