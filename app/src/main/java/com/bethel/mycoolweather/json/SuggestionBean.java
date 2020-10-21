package com.bethel.mycoolweather.json;

import com.bethel.mycoolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @author: Bethel
 * @date: 2020-10-15 15:18
 * @desc:
 */
public class SuggestionBean extends BasicWeather {
    List<Suggestion> daily;

    public List<Suggestion> getDaily() {
        return daily;
    }

    public List<Suggestion> getDailyWithDB() {
        return null != daily ? daily : (daily = queryLinkedDatas(Suggestion.class));
    }

    public void setDaily(List<Suggestion> daily) {
        this.daily = daily;
    }

    @Override
    public boolean autoSave(String weatherId) {
        boolean r = super.autoSave(weatherId);
        if(!Utility.isEmpty(daily)) {
           DataSupport.saveAll(daily);
            queryLinkedDatas(Suggestion.class);
        }
        return  r;
    }
}
