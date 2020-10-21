package com.bethel.mycoolweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.bethel.mycoolweather.util.CacheUtil;

public class AreaChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String weatherId = CacheUtil.getString(CacheUtil.WEATHRT_ID);
//        if (!TextUtils.isEmpty(weatherId)) {
//            WeatherActivity.startMe(this, weatherId);
//            finish();
//            return;
//        }
        setContentView(R.layout.activity_main);
    }

    public static void startMe(Context context) {
        Intent intent = new Intent(context, AreaChooseActivity.class);
        context.startActivity(intent);
    }
}
