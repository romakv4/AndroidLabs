package com.example.lab1and2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends Activity {

    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onClickStartOneMin(View view) {
        startService(new Intent(this, WeatherService.class).putExtra("time", 1).putExtra("city", city));
    }

    public void onClickStartTwoMins(View view) {
        startService(new Intent(this, WeatherService.class).putExtra("time", 2).putExtra("city", city));
    }

    public void onClickStartThreeMins(View view) {
        startService(new Intent(this, WeatherService.class).putExtra("time", 3).putExtra("city", city));
    }

    public void onClickDropService(View view) {
        stopService(new Intent(this, WeatherService.class));
    }
}
