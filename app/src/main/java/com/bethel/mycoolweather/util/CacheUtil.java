package com.bethel.mycoolweather.util;

import android.content.SharedPreferences;

import com.bethel.mycoolweather.CoolApplication;

/**
 * @author: Bethel
 * @date: 2020-10-16 16:02
 * @desc:
 */
public class CacheUtil {
    private static final String PREFERENCE_NAME = "cool_preference";
    public static final String WEATHRT_ID = "weather_id";
    public static final String BG_IMG_URL = "bing_pic_link";

    public static void cacheString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key) {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getString(key, "");
    }

    private static SharedPreferences getSharedPreferences() {
        return CoolApplication.getApplication().getSharedPreferences(PREFERENCE_NAME, 0);
    }
}
