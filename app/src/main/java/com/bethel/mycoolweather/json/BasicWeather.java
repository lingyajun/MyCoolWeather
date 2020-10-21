package com.bethel.mycoolweather.json;

import com.bethel.mycoolweather.db.DBweather;

import java.util.Date;

/**
 * @author: Bethel
 * @date: 2020-10-14 17:24
 * @desc:
 */
public class BasicWeather extends DBweather {
    protected String code;
//    protected Date updateTime;
    protected String updateTime;
    protected String fxLink;
    protected Refer refer;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFxLink() {
        return fxLink;
    }

    public void setFxLink(String fxLink) {
        this.fxLink = fxLink;
    }

    public Refer getRefer() {
        return refer;
    }

    public void setRefer(Refer refer) {
        this.refer = refer;
    }
}
