package com.example.lab1and2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class WeatherService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Service has stopped", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int time = intent.getIntExtra("time", 0);
        final String city = intent.getStringExtra("city");
        System.out.println(city);
        if(!city.isEmpty()) {
            Toast.makeText(getApplicationContext(), "" + time + city, Toast.LENGTH_SHORT).show();
            Thread weather = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(true) {
                            getWeather(city);
                            TimeUnit.MINUTES.sleep(time);
                        }
                    } catch(InterruptedException ie){
                        ie.printStackTrace();
                    }
                }
            });
            weather.start();
        } else {
            Toast.makeText(getApplicationContext(), "Требуемые параметры не были заданы", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void getWeather(String city) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=d4f9cdcc72088078ab2092ebe1841883";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject main = response.getJSONObject("main");
                            String city = response.getString("name").toUpperCase();
                            Double temperature = main.getDouble("temp");
                            Weather weather = new Weather(city, temperature);
                            weather.save();
                            Toast.makeText(getApplicationContext(), "Запись была добавлена в базу данных", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}
