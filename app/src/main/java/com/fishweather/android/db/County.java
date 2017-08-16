package com.fishweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by yujj on 2017/8/16.
 */

public class County extends DataSupport {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCtiyId() {
        return ctiyId;
    }

    public void setCtiyId(int ctiyId) {
        this.ctiyId = ctiyId;
    }

    private int id;
    private String countyName;
    private String weatherId;
    private int ctiyId;


}
