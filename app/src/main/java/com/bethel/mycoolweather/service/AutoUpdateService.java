package com.bethel.mycoolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.bethel.mycoolweather.WeatherActivity;
import com.bethel.mycoolweather.db.ResponseCacheDB;
import com.bethel.mycoolweather.json.AqiNowBean;
import com.bethel.mycoolweather.json.ForecastBean;
import com.bethel.mycoolweather.json.SuggestionBean;
import com.bethel.mycoolweather.json.WeatherNowBean;
import com.bethel.mycoolweather.util.CacheUtil;
import com.bethel.mycoolweather.util.HttpUtil;
import com.bethel.mycoolweather.util.Utility;
import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    private static final String TAG = "AutoUpdateService";

    private ResponseCacheDB responseCache;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int hour8 = 8*60*60*1000; // 8h
        long triggerAtTime = SystemClock.elapsedRealtime() + hour8;
        Intent intent1 = new Intent(this, AutoUpdateService.class);
        // getService(Context context, int requestCode, @NonNull Intent intent, int flags) {
        PendingIntent pi = PendingIntent.getService(this, 0, intent1, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkhttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: "+ e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                CacheUtil.cacheString(CacheUtil.BG_IMG_URL, bingPic);
            }
        });
    }

    private void updateWeather() {
        String weatherId = CacheUtil.getString(CacheUtil.WEATHRT_ID);
        if (TextUtils.isEmpty(weatherId)) return;
        responseCache = DataSupport.where("weatherId = ?", weatherId).findFirst(ResponseCacheDB.class);
        if (responseCache == null) responseCache = new ResponseCacheDB();
        requestWeatherNow(weatherId);
        requestWeatherFuture(weatherId);
        requestAirQualityNow(weatherId);
        requestWeatherSuggestion(weatherId);
    }

    private void requestWeatherNow(final String weatherId) {
        String api = String.format("https://devapi.heweather.net/v7/weather/now?location=%s&key=e16b8d5939e44769a7751a843e8631a5", weatherId);
        HttpUtil.sendOkhttpRequest(api, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "requestWeatherNow ,onFailure: "+ e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                final WeatherNowBean nowBean = Utility.baseHandleResponse(data, WeatherNowBean.class);

                if ("200".equals(nowBean.getCode())) {
                    responseCache.setWeatherNowBean(data);
                    responseCache.autoSave(weatherId);
                }
            }
        });
    }

    private void requestWeatherFuture(final String weatherId) {
        String api = String.format("https://devapi.heweather.net/v7/weather/3d?location=%s&key=e16b8d5939e44769a7751a843e8631a5", weatherId);
        HttpUtil.sendOkhttpRequest(api, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "requestWeatherFuture ,onFailure: "+ e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                final ForecastBean bean = Utility.baseHandleResponse(data, ForecastBean.class);
                if ("200".equals(bean.getCode())){
                    responseCache.setForecastBean(data);
                    responseCache.autoSave(weatherId);
                }
            }
        });
    }

    private void requestAirQualityNow(final String weatherId) {
        String api = String.format("https://devapi.heweather.net/v7/air/now?location=%s&key=e16b8d5939e44769a7751a843e8631a5", weatherId);
        HttpUtil.sendOkhttpRequest(api, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "requestAirQualityNow ,onFailure: "+ e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                final AqiNowBean bean = Utility.baseHandleResponse(data, AqiNowBean.class);
                if ("200".equals(bean.getCode())) {
                    responseCache.setAqiNowBean(data);
                    responseCache.autoSave(weatherId);
                }
            }
        });
    }

    private void requestWeatherSuggestion(final String weatherId) {
        String api = String.format("https://devapi.heweather.net/v7/indices/1d?location=%s&key=e16b8d5939e44769a7751a843e8631a5&type=1,2,3", weatherId);
        HttpUtil.sendOkhttpRequest(api, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "requestWeatherSuggestion ,onFailure: "+ e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                final SuggestionBean bean = Utility.baseHandleResponse(data, SuggestionBean.class);
                if ("200".equals(bean.getCode())) {
                    responseCache.setSuggestionBean(data);
                    responseCache.autoSave(weatherId);
                }
            }
        });
    }

}
