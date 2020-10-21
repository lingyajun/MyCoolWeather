package com.bethel.mycoolweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bethel.mycoolweather.db.County;
import com.bethel.mycoolweather.db.ResponseCacheDB;
import com.bethel.mycoolweather.json.AqiNow;
import com.bethel.mycoolweather.json.AqiNowBean;
import com.bethel.mycoolweather.json.Forecast;
import com.bethel.mycoolweather.json.ForecastBean;
import com.bethel.mycoolweather.json.Suggestion;
import com.bethel.mycoolweather.json.SuggestionBean;
import com.bethel.mycoolweather.json.WeatherNow;
import com.bethel.mycoolweather.json.WeatherNowBean;
import com.bethel.mycoolweather.service.AutoUpdateService;
import com.bethel.mycoolweather.util.HttpUtil;
import com.bethel.mycoolweather.util.CacheUtil;
import com.bethel.mycoolweather.util.Utility;
import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";
    private ScrollView weatherLayout;
    private TextView titleCity, titleUpdateTime, tempText, weatherInfoText, aqiText, pm25Text;
    private LinearLayout forecastLayout, suggestionLayout;
    private ImageView bgImg;
    private SwipeRefreshLayout refreshLayout;
    private DrawerLayout drawerLayout;
    private Button navBtn;

    private ResponseCacheDB responseCache;
//    private static final String BG_IMG_URL = CacheUtil.BG_IMG_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >=21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        View drawerInsertTitle = findViewById(R.id.insert_title4drawer);
        drawerInsertTitle.setVisibility(Build.VERSION.SDK_INT >=21? View.VISIBLE: View.GONE);
//        drawerInsertTitle.setVisibility(View.GONE);

        weatherLayout = findViewById(R.id.weather_layout);
        forecastLayout = findViewById(R.id.forecast_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        tempText = findViewById(R.id.temp_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        suggestionLayout = findViewById(R.id.suggestion_layout);
        bgImg = findViewById(R.id.bg_img);
        refreshLayout = findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        navBtn = findViewById(R.id.nav_btn);
        drawerLayout = findViewById(R.id.drawer_layout);

//        Intent data = getIntent();
//        String weatherId = data.getStringExtra("weather_id");
        String weatherId = CacheUtil.getString(CacheUtil.WEATHRT_ID);
        if (TextUtils.isEmpty(weatherId)) {
            AreaChooseActivity.startMe(this);
            finish();
            return;
        }
        weatherLayout.setVisibility(View.INVISIBLE);
        loadWeatherData(weatherId);
        loadBingPic();
//        CacheUtil.cacheString(CacheUtil.WEATHRT_ID, weatherId);
        Log.i(TAG, "onCreate,weather_id: "+ weatherId);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String weatherId = CacheUtil.getString(CacheUtil.WEATHRT_ID);
                refreshWeatherData(weatherId);
            }
        });
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    /**
     * DrawerLayout,AreaChooseFragment
     * 切换城市
     * */
    public void onAreaChanged(String weatherId) {
        drawerLayout.closeDrawers();
        refreshLayout.setRefreshing(true);
        initResponseCacheDB(weatherId);
        refreshWeatherData(weatherId);
        loadArea(weatherId);
        CacheUtil.cacheString(CacheUtil.WEATHRT_ID, weatherId);
    }

    private void refreshWeatherData(String weatherId) {
        requestWeatherNow(weatherId);
        requestWeatherFuture(weatherId);
        requestAirQualityNow(weatherId);
        requestWeatherSuggestion(weatherId);
    }

    private void loadBingPic() {
        String bingPic = CacheUtil.getString(CacheUtil.BG_IMG_URL);
        if (!TextUtils.isEmpty(bingPic)) {
            Glide.with(WeatherActivity.this).load(bingPic).into(bgImg);
            return;
        }
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this)
                                .load(bingPic).crossFade(300).into(bgImg);
                    }
                });
            }
        });
    }

    private void loadWeatherData(String weatherId) {
        initResponseCacheDB(weatherId);
//        List<WeatherNowBean> list = DataSupport.where("weatherId = ?", weatherId).find(WeatherNowBean.class);
        WeatherNowBean weatherNowBean = Utility.pareseWeatherNowBean(responseCache);
        if (null != weatherNowBean) {
            showWeatherNowInfo(weatherNowBean);
        } else {
            requestWeatherNow(weatherId);
        }

//        List<ForecastBean> list1 = DataSupport.where("weatherId = ?", weatherId).find(ForecastBean.class);
        ForecastBean forecastBean = Utility.pareseForecastBean(responseCache);
        if (forecastBean != null) {
            showWeatherFutureInfo(forecastBean);
        } else {
            requestWeatherFuture(weatherId);
        }

//        List<AqiNowBean> list2 = DataSupport.where("weatherId = ?", weatherId).find(AqiNowBean.class);
        AqiNowBean aqiNowBean = Utility.pareseAqiNowBean(responseCache);
        if (null != aqiNowBean) {
            showAqiNowInfo(aqiNowBean);
        } else {
            requestAirQualityNow(weatherId);
        }

//        List<SuggestionBean> list3 = DataSupport.where("weatherId = ?", weatherId).find(SuggestionBean.class);
        SuggestionBean suggestionBean = Utility.pareseSuggestionBean(responseCache);
        if (null != suggestionBean) {
            showSuggestionsInfo(suggestionBean);
        } else {
            requestWeatherSuggestion(weatherId);
        }

        loadArea(weatherId);
//        Log.i(TAG, String.format("loadWeatherData: %d,  %d, %d, %d",
//                Utility.size(list), Utility.size(list1),
//                Utility.size(list2), Utility.size(list3)));
    }

    private void initResponseCacheDB(String weatherId) {
        responseCache = DataSupport.where("weatherId = ?", weatherId).findFirst(ResponseCacheDB.class);
        if (responseCache == null) responseCache = new ResponseCacheDB();
    }

    private void loadArea(String weatherId) {
        County county = DataSupport.where("weatherId = ?", weatherId).findFirst(County.class);
        if (null!=county && !TextUtils.isEmpty(county.getCountyName())) {
            setTextSafely(titleCity, county.getCountyName());
        }
    }

    private void requestWeatherNow(final String weatherId) {
        String api = String.format("https://devapi.heweather.net/v7/weather/now?location=%s&key=e16b8d5939e44769a7751a843e8631a5", weatherId);
        HttpUtil.sendOkhttpRequest(api, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "requestWeatherNow ,onFailure: "+ e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                final WeatherNowBean nowBean = Utility.baseHandleResponse(data, WeatherNowBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeatherNowInfo(nowBean);
                        refreshLayout.setRefreshing(false);
                    }
                });
                if ("200".equals(nowBean.getCode())) {
                    responseCache.setWeatherNowBean(data);
                    responseCache.autoSave(weatherId);
                }
//                    nowBean.autoSave(weatherId);
            }
        });
    }

    private void showWeatherNowInfo(WeatherNowBean bean) {
        String code = bean.getCode();
        WeatherNow now = bean.getNow();
        if ("200".equals(code) && null!=now) {
            weatherLayout.setVisibility(View.VISIBLE);

            setTextSafely(titleUpdateTime, Utility.getDateFormat(now.getObsTime(), "yyyy/MM/dd HH:mm"));
            setTextSafely(tempText, now.getTemp());
            tempText.append("℃");
            setTextSafely(weatherInfoText, now.getText());

            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
        } else {
            Log.e(TAG, "showWeatherNowInfo: " + code);
        }
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeatherFutureInfo(bean);
                    }
                });
                if ("200".equals(bean.getCode())){
                    responseCache.setForecastBean(data);
                    responseCache.autoSave(weatherId);
                }
//                bean.autoSave(weatherId);
            }
        });
    }

    private void showWeatherFutureInfo(ForecastBean bean) {
        String code = bean.getCode();
        List<Forecast> list = bean.getDaily();
        if ("200".equals(code) && null!=list) {
            forecastLayout.removeAllViewsInLayout();
            for (Forecast forecast : list) {
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
                TextView dateText = view.findViewById(R.id.date_text);
                TextView infoText = view.findViewById(R.id.info_text);
                TextView maxText = view.findViewById(R.id.max_text);
                TextView minText = view.findViewById(R.id.min_text);
                forecastLayout.addView(view);
                setTextSafely(dateText, forecast.getFxDate());
                setTextSafely(infoText, forecast.getTextDay());
                setTextSafely(maxText, forecast.getTempMax()+ " ℃ ");
                setTextSafely(minText, forecast.getTempMin()+ " ℃ ");
            }
        } else {
            Log.e(TAG, "showWeatherFutureInfo: " + code);
        }
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAqiNowInfo(bean);
                    }
                });
                if ("200".equals(bean.getCode())) {
                    responseCache.setAqiNowBean(data);
                    responseCache.autoSave(weatherId);
                }
//                bean.autoSave(weatherId);
            }
        });
    }

    private void showAqiNowInfo(AqiNowBean bean) {
        String code = bean.getCode();
        AqiNow now = bean.getNow();
        if ("200".equals(code) && null!=now) {
            setTextSafely(aqiText, now.getAqi());
            setTextSafely(pm25Text, now.getPm2p5());
        } else {
            Log.e(TAG, "showAqiNowInfo: " + code);
        }
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showSuggestionsInfo(bean);
                    }
                });
                if ("200".equals(bean.getCode())) {
                    responseCache.setSuggestionBean(data);
                    responseCache.autoSave(weatherId);
                }
//                bean.autoSave(weatherId);
            }
        });
    }

    private void showSuggestionsInfo(SuggestionBean bean) {
        String code = bean.getCode();
        List<Suggestion> list = bean.getDaily();
        if ("200".equals(code) && null != list) {
            suggestionLayout.removeAllViewsInLayout();
            for (Suggestion suggestion: list) {
                TextView tv = (TextView)LayoutInflater.from(this).inflate(R.layout.suggestion_item, suggestionLayout, false);
                setTextSafely(tv, suggestion.getText());
                suggestionLayout.addView(tv);
            }
        } else {
            Log.e(TAG, "showSuggestionsInfo: " + code);
        }
    }

    private void setTextSafely(TextView tv, CharSequence str) {
        tv.setText(null != str ? str: "");
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    public static void startMe(Context context, String weatherId) {
        CacheUtil.cacheString(CacheUtil.WEATHRT_ID, weatherId);
        Intent intent = new Intent(context, WeatherActivity.class);
//        intent.putExtra("weather_id", weatherId);
        context.startActivity(intent);
    }
}
