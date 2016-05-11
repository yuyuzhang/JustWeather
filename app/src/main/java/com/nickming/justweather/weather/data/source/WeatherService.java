package com.nickming.justweather.weather.data.source;

import com.nickming.justweather.weather.data.WeatherApi;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * desc:
 *
 * @author:nickming date:2016/4/21
 * time: 23:33
 * e-mail：962570483@qq.com
 */

public interface WeatherService {

    String ENDPOINT = "https://api.heweather.com";


    @GET("/x3/weather")
    Observable<WeatherApi> requestWeatherInfo(@Query("cityid") String cityid, @Query("key") String key);

    @GET("/x3/weather")
    Observable<WeatherApi> requestWeatherForCityName(@Query("city") String cityid, @Query("key") String key);
}
