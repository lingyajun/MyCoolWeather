package com.bethel.mycoolweather.util;

import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;

import com.bethel.mycoolweather.db.City;
import com.bethel.mycoolweather.db.County;
import com.bethel.mycoolweather.db.Province;
import com.bethel.mycoolweather.db.ResponseCacheDB;
import com.bethel.mycoolweather.json.AqiNowBean;
import com.bethel.mycoolweather.json.Area;
import com.bethel.mycoolweather.json.ForecastBean;
import com.bethel.mycoolweather.json.SuggestionBean;
import com.bethel.mycoolweather.json.WeatherNowBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author: Bethel
 * @date: 2020-10-13 13:36
 * @desc:
 */
public class Utility {
    private static final String TAG = "Utility";

    public final static <T> T  baseHandleResponse(String response, Type typeOfT) {
        if (!TextUtils.isEmpty(response)) {
            return new Gson().fromJson(response, typeOfT);
        }
        return null;
    }

    public final static <T> T  baseHandleResponse(String response, Class<T> clazz) {
        if (!TextUtils.isEmpty(response)) {
            return new Gson().fromJson(response, clazz);
        }
        return null;
    }

//    public final static <T extends DataSupport> List<T> baseHandleAreasResponse(String response, Type typeOfT) {
//        List<T> list = baseHandleResponse(response, typeOfT);
////                new TypeToken<List<T>>(){}.getType());
//        if (null!=list && list.size() > 0) {
//            for (T p: list ) {
//                p.save();
//            }
//        }
//        return list;
//    }
//
//    public final static List<Province> handleProvinceListResponse(String response) {
//        return baseHandleAreasResponse(response, new TypeToken<List<Province>>(){}.getType());
//    }
//
//    public final static List<City> handleCityListResponse(String response) {
//        return baseHandleAreasResponse(response, new TypeToken<List<City>>(){}.getType());
//    }
//
//    public final static List<County> handleCountyListResponse(String response) {
//        return baseHandleAreasResponse(response, new TypeToken<List<County>>(){}.getType());
//    }

    public final static boolean handleProvinceResponse(String response) {
        List<Area> list = baseHandleResponse(response,
                        new TypeToken<List<Area>>(){}.getType());
        if (null!=list && list.size() > 0) {
            for (Area p: list ) {
                Log.i(TAG, "handleProvinceResponse: "+ p.name);
                Province.newInstance(p).save();
            }
            return true;
        }
        return false;
    }

    public final static boolean handleCityResponse(String response, int provinceId) {
        List<Area> list = baseHandleResponse(response,
                new TypeToken<List<Area>>(){}.getType());
        if (null!=list && list.size() > 0) {
            for (Area a: list ) {
                City p = City.newInstance(a);
                p.setProvinceId(provinceId);
                p.save();
            }
            return true;
        }
        return false;
    }

    public final static boolean handleCountyResponse(String response, int cityId) {
        List<Area> list = baseHandleResponse(response,
                new TypeToken<List<Area>>(){}.getType());
        if (null!=list && list.size() > 0) {
            for (Area a: list ) {
                County p = County.newInstance(a);
                p.setCityId(cityId);
                p.save();
            }
            return true;
        }
        return false;
    }

    /**
     * 将格林威治时间字符串转换为yyyy-MM-dd HH:mm:ss字符串
     * "2020-10-15T16:50+08:00"
     * */
    public static String getDateFormat(String dateStr, String format) {
//        Date date = new Date(dateStr);
        SimpleDateFormat sf = new SimpleDateFormat(format);
        SimpleDateFormat sd =new SimpleDateFormat("yyyy-MM-dd'T'HH:mmX");
        Date date = null;
        String str = "";
        try {
            date = sd.parse(dateStr);
            str = sf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static boolean isEmpty(Collection collection) {
        return null==collection || collection.isEmpty();
    }

    public static int size(Collection collection) {
        return isEmpty(collection) ? -1 : collection.size();
    }

    public static WeatherNowBean pareseWeatherNowBean(ResponseCacheDB data) {
        String text = null == data? null:data.getWeatherNowBean();
        return TextUtils.isEmpty(text) ? null: baseHandleResponse(text, WeatherNowBean.class);
    }
    public static AqiNowBean pareseAqiNowBean(ResponseCacheDB data) {
        String text = null == data? null:data.getAqiNowBean();
        return TextUtils.isEmpty(text) ? null: baseHandleResponse(text, AqiNowBean.class);
    }
    public static ForecastBean pareseForecastBean(ResponseCacheDB data) {
        String text = null == data? null:data.getForecastBean();
        return TextUtils.isEmpty(text) ? null: baseHandleResponse(text, ForecastBean.class);
    }
    public static SuggestionBean pareseSuggestionBean(ResponseCacheDB data) {
        String text = null == data? null:data.getSuggestionBean();
        return TextUtils.isEmpty(text) ? null: baseHandleResponse(text, SuggestionBean.class);
    }
}
