package com.bethel.mycoolweather.db;

/**
 * @author: Bethel
 * @date: 2020-10-16 16:05
 * @desc:
 */
public class ResponseCacheDB extends DBweather {
    private String weatherNowBean;
    private String aqiNowBean;
    private String forecastBean;
    private String suggestionBean;

    public String getWeatherNowBean() {
        return weatherNowBean;
    }

    public void setWeatherNowBean(String weatherNowBean) {
        this.weatherNowBean = weatherNowBean;
    }

    public String getAqiNowBean() {
        return aqiNowBean;
    }

    public void setAqiNowBean(String aqiNowBean) {
        this.aqiNowBean = aqiNowBean;
    }

    public String getForecastBean() {
        return forecastBean;
    }

    public void setForecastBean(String forecastBean) {
        this.forecastBean = forecastBean;
    }

    public String getSuggestionBean() {
        return suggestionBean;
    }

    public void setSuggestionBean(String suggestionBean) {
        this.suggestionBean = suggestionBean;
    }
}
