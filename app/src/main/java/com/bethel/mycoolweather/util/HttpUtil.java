package com.bethel.mycoolweather.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: Bethel
 * @date: 2020-10-12 16:03
 * @desc:
 */
public class HttpUtil {
    private static final String TAG = "HttpUtil";
    public static void sendOkhttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkhttpRequestTest(String address) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        try {
            Response response = client.newCall(request).execute();
            Log.i(TAG, response.body().string() );
            Log.i(TAG, response.code() +"" );
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage() );
        }
    }

}
