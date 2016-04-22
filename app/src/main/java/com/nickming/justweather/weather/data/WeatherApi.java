package com.nickming.justweather.weather.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 *
 * @author:nickming date:2016/4/21
 * time: 10:19
 * e-mail：962570483@qq.com
 */

public class WeatherApi implements Serializable{

    @SerializedName("HeWeather data service 3.0") @Expose
    public List<Weather> mHeWeatherDataService30s
            = new ArrayList<>();
}
