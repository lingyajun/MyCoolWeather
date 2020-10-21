package com.bethel.mycoolweather;

import android.app.Application;

import org.litepal.LitePalApplication;

/**
 * @author: Solomon
 * @date: 2020-10-12 15:59
 * @desc:
 */
public class CoolApplication extends Application {
    private static CoolApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePalApplication.initialize(getApplicationContext());
        app = this;
    }

    public static CoolApplication getApplication() {
        return app;
    }
}
