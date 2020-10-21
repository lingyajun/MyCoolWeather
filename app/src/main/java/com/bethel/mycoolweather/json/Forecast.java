package com.bethel.mycoolweather.json;

import com.bethel.mycoolweather.db.DBweather;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * @author: Bethel
 * @date: 2020-10-15 14:45
 * @desc:
 */
public class Forecast extends DataSupport {

    private String fxDate;
    private String sunrise;
    private String sunset;
    private String moonrise;
    private String moonset;
    private String moonPhase;
    private String tempMax;
    private String tempMin;
    private String iconDay;
    private String textDay;
    private String iconNight;
    private String textNight;
    private String wind360Day;
    private String windDirDay;
    private String windScaleDay;
    private String windSpeedDay;
    private String wind360Night;
    private String windDirNight;
    private String windScaleNight;
    private String windSpeedNight;
    private String humidity;
    private String precip;
    private String pressure;
    private String vis;
    private String cloud;
    private String uvIndex;
    public void setFxDate(String fxDate) {
        this.fxDate = fxDate;
    }
    public String getFxDate() {
        return fxDate;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }
    public String getSunrise() {
        return sunrise;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
    public String getSunset() {
        return sunset;
    }

    public void setMoonrise(String moonrise) {
        this.moonrise = moonrise;
    }
    public String getMoonrise() {
        return moonrise;
    }

    public void setMoonset(String moonset) {
        this.moonset = moonset;
    }
    public String getMoonset() {
        return moonset;
    }

    public void setMoonPhase(String moonPhase) {
        this.moonPhase = moonPhase;
    }
    public String getMoonPhase() {
        return moonPhase;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }
    public String getTempMax() {
        return tempMax;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }
    public String getTempMin() {
        return tempMin;
    }

    public void setIconDay(String iconDay) {
        this.iconDay = iconDay;
    }
    public String getIconDay() {
        return iconDay;
    }

    public void setTextDay(String textDay) {
        this.textDay = textDay;
    }
    public String getTextDay() {
        return textDay;
    }

    public void setIconNight(String iconNight) {
        this.iconNight = iconNight;
    }
    public String getIconNight() {
        return iconNight;
    }

    public void setTextNight(String textNight) {
        this.textNight = textNight;
    }
    public String getTextNight() {
        return textNight;
    }

    public void setWind360Day(String wind360Day) {
        this.wind360Day = wind360Day;
    }
    public String getWind360Day() {
        return wind360Day;
    }

    public void setWindDirDay(String windDirDay) {
        this.windDirDay = windDirDay;
    }
    public String getWindDirDay() {
        return windDirDay;
    }

    public void setWindScaleDay(String windScaleDay) {
        this.windScaleDay = windScaleDay;
    }
    public String getWindScaleDay() {
        return windScaleDay;
    }

    public void setWindSpeedDay(String windSpeedDay) {
        this.windSpeedDay = windSpeedDay;
    }
    public String getWindSpeedDay() {
        return windSpeedDay;
    }

    public void setWind360Night(String wind360Night) {
        this.wind360Night = wind360Night;
    }
    public String getWind360Night() {
        return wind360Night;
    }

    public void setWindDirNight(String windDirNight) {
        this.windDirNight = windDirNight;
    }
    public String getWindDirNight() {
        return windDirNight;
    }

    public void setWindScaleNight(String windScaleNight) {
        this.windScaleNight = windScaleNight;
    }
    public String getWindScaleNight() {
        return windScaleNight;
    }

    public void setWindSpeedNight(String windSpeedNight) {
        this.windSpeedNight = windSpeedNight;
    }
    public String getWindSpeedNight() {
        return windSpeedNight;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
    public String getHumidity() {
        return humidity;
    }

    public void setPrecip(String precip) {
        this.precip = precip;
    }
    public String getPrecip() {
        return precip;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
    public String getPressure() {
        return pressure;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }
    public String getVis() {
        return vis;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }
    public String getCloud() {
        return cloud;
    }

    public void setUvIndex(String uvIndex) {
        this.uvIndex = uvIndex;
    }
    public String getUvIndex() {
        return uvIndex;
    }

}