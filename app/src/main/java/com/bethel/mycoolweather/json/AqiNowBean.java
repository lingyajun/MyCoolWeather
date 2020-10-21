package com.bethel.mycoolweather.json;

import com.bethel.mycoolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @author: Bethel
 * @date: 2020-10-15 15:02
 * @desc:
 */
public class AqiNowBean extends BasicWeather {

    private AqiNow now;
    private List<AqiStation> station;

    public AqiNow getNow() {
        return now;
    }

    public AqiNow getNowWithDB() {
        return null != now ? now : queryAqiNow();
    }

    public void setNow(AqiNow now) {
        this.now = now;
    }

    public List<AqiStation> getStation() {
        return station;
    }

    public List<AqiStation> getStationWithDB() {
        return null != station ? station : queryStation();
    }

    public void setStation(List<AqiStation> station) {
        this.station = station;
    }

    @Override
    public boolean autoSave(String weatherId) {
        if (null != now) now.save();
        if (!Utility.isEmpty(station)) DataSupport.saveAll(station);
        return super.autoSave(weatherId);
    }
    public AqiNow queryAqiNow() {
        now = queryLinkedDataItem(AqiNow.class);
        return now;
    }


    public List<AqiStation> queryStation() {
        return station;
    }

}
