package com.bethel.mycoolweather.json;

import com.bethel.mycoolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @author: Bethel
 * @date: 2020-10-15 14:55
 * @desc:
 */
public class ForecastBean extends BasicWeather {
    private List<Forecast> daily;

    public List<Forecast> getDaily() {
        return daily;
    }

    public List<Forecast> getDailyWithDB() {
        return null != daily? daily : (daily = queryLinkedDatas(Forecast.class));
    }

    public void setDaily(List<Forecast> daily) {
        this.daily = daily;
    }

    @Override
    public boolean autoSave(String weatherId) {
        if (!Utility.isEmpty(daily)) DataSupport.saveAll(daily);
        return super.autoSave(weatherId);
    }
}
