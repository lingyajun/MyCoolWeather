package com.bethel.mycoolweather.db;

import com.bethel.mycoolweather.json.Area;

import org.litepal.crud.DataSupport;

/**
 * @author: Solomon
 * @date: 2020-10-12 15:49
 * @desc:
 */
public class City extends DataSupport {
    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public static City newInstance(Area area) {
        City city = new City();
        city.setCityCode(area.id);
        city.setCityName(area.name);
        return city;
    }
}
