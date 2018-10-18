package com.example.lab1and2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView cityTextView = findViewById(R.id.cityName);
        final TextView degreesTextView = findViewById(R.id.temperature);
        final TextView allRowsInDBTextView = findViewById(R.id.allRowsInDBTextView);
        allRowsInDBTextView.setMovementMethod(new ScrollingMovementMethod());

        Button goButton = findViewById(R.id.sendRequest);
        Button saveButton = findViewById(R.id.saveButton);
        Button searchButton = findViewById(R.id.searchButton);
        Button viewButton = findViewById(R.id.viewButton);

        final TextView cityChoose = findViewById(R.id.cityChoose);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cityChoose.getText().toString().isEmpty()) {
                    sendReq(cityChoose.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Вы не указали город!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cityTextView.getText().toString().isEmpty() && !degreesTextView.getText().toString().isEmpty()) {
                    String city = cityTextView.getText().toString();
                    String temperature = degreesTextView.getText().toString();
                    Weather weather = new Weather(city, Double.parseDouble(temperature.replace(" ℃", "")));
                    weather.save();
                    Toast.makeText(getApplicationContext(), "Запись была добавлена в базу данных", Toast.LENGTH_SHORT).show();
                    cityTextView.setText("");
                    degreesTextView.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Нечего добавлять", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String city = cityChoose.getText().toString();
                List<Weather> weather = Weather.listAll(Weather.class);
                for (Weather w: weather) {
                    if(city.toLowerCase().equals(w.getCity().toLowerCase())) {
                        cityTextView.setText("Есть у нас такое");
                        degreesTextView.setText("");
                        return;
                    } else {
                        cityTextView.setText("Нет у нас такого");
                        degreesTextView.setText("");
                    }
                }
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allRowsInDBTextView.setText("");
                List<Weather> weather = Weather.listAll(Weather.class);
                for (int i = 0; i < weather.size(); i++) {
                    allRowsInDBTextView.append((i+1) + " " + weather.get(i).getCity() + " " + weather.get(i).getTemperature() + "\n");
                    System.out.println((i+1) + " " + weather.get(i).getCity() + " " + weather.get(i).getTemperature() + "\n");
                }
            }
        });

    }



    public void sendReq(String city) {
        final TextView cityTextView = findViewById(R.id.cityName);
        final TextView degreesTextView = findViewById(R.id.temperature);
        // Instantiate the RequestQueue.
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
                            cityTextView.setText(city);
                            degreesTextView.setText(temperature.toString() + " ℃");
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


