package com.bethel.mycoolweather.json;

import com.bethel.mycoolweather.db.DBweather;
import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * @author: Bethel
 * @date: 2020-10-15 15:00
 * @desc: AqiStation
 */

public class AqiStation extends DataSupport {

    private String pubTime;
    private String name;
    @SerializedName("id")
    private String id_station;
    private String aqi;
    private String level;
    private String category;
    @SerializedName("primary")
    private String primary_now;
    private String pm10;
    private String pm2p5;
    private String no2;
    private String so2;
    private String co;
    private String o3;
    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }
    public String getPubTime() {
        return pubTime;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public String getId_station() {
        return id_station;
    }

    public void setId_station(String id_station) {
        this.id_station = id_station;
    }
/*
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
*/

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }
    public String getAqi() {
        return aqi;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public String getLevel() {
        return level;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setPrimary(String primary) {
        this.primary_now = primary;
    }
    public String getPrimary() {
        return primary_now;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }
    public String getPm10() {
        return pm10;
    }

    public void setPm2p5(String pm2p5) {
        this.pm2p5 = pm2p5;
    }
    public String getPm2p5() {
        return pm2p5;
    }

    public void setNo2(String no2) {
        this.no2 = no2;
    }
    public String getNo2() {
        return no2;
    }

    public void setSo2(String so2) {
        this.so2 = so2;
    }
    public String getSo2() {
        return so2;
    }

    public void setCo(String co) {
        this.co = co;
    }
    public String getCo() {
        return co;
    }

    public void setO3(String o3) {
        this.o3 = o3;
    }
    public String getO3() {
        return o3;
    }

}
