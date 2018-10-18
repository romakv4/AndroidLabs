package com.example.lab1and2;

import com.orm.SugarRecord;

public class Weather extends SugarRecord {
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    private String city;
    private Double temperature;

    public Weather(){}

    public Weather(String city, Double temperature){
        this.city = city;
        this.temperature = temperature;
    }
}
