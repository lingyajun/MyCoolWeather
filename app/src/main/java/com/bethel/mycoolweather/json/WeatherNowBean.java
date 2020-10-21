package com.bethel.mycoolweather.json;

/**
 * @author: Bethel
 * @date: 2020-10-14 17:26
 * @desc:
 */
public class WeatherNowBean extends BasicWeather {

    private WeatherNow now;

    public WeatherNow getNow() {
        return now;
    }

    public WeatherNow getNowWithDB() {
        return null != now ? now : (now = queryLinkedDataItem(WeatherNow.class));
    }

    public void setNow(WeatherNow now) {
        this.now = now;
    }

    @Override
    public boolean autoSave(String weatherId) {
        if (null!= now) now.save();
        return super.autoSave(weatherId);
    }
}
