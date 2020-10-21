package com.bethel.mycoolweather.db;

import android.util.Log;

import com.bethel.mycoolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @author: Bethel
 * @date: 2020-10-16 09:17
 * @desc:
 */
public class DBweather extends DataSupport {
    private static final String TAG = "DBweather";
    protected int id;
    protected String weatherId;
    protected long cacheTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

    public boolean autoSave(String weatherId) {
        this.cacheTime = System.currentTimeMillis();
        this.weatherId = weatherId;
        Log.d(TAG,  String.format("autoSave: %s - %s", cacheTime ,weatherId));
        return save();
    }

    public <T extends DataSupport> List<T> queryLinkedDatas (Class<T> clz) {
        String linkId=this.getClass().getSimpleName().toLowerCase();
        List<T> list = DataSupport.where(linkId+"_id=?",String.valueOf(id)).find(clz);
        return list;
    }
    public <T extends DataSupport>  T queryLinkedDataItem (Class<T> clz) {
        List<T> list = queryLinkedDatas(clz);
        return Utility.isEmpty(list) ? null : list.get(0);
    }
}
