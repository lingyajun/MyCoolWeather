package com.bethel.mycoolweather.db;

import com.bethel.mycoolweather.json.Area;

import org.litepal.crud.DataSupport;

/**
 * @author: Solomon
 * @date: 2020-10-12 15:51
 * @desc:
 */
public class County extends DataSupport {
    private int id;
    private String countyName;
    private  String weatherId;
    private int cityId;

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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public static County newInstance(Area area) {
        County county = new County();
        county.setCountyName(area.name);
        county.setWeatherId(area.weather_id);
        county.setId(area.id);
        return county;
    }

}
