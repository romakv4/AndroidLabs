package com.example.lab1and2;

import android.app.Application;

import com.orm.SugarContext;

public class WeatherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }
}
