package com.nickming.justweather.weather.data.source;

import com.nickming.justweather.common.ServiceFactory;
import com.nickming.justweather.weather.data.WeatherApi;

import rx.Observable;

/**
 * desc:天气数据处理类
 *
 * @author:nickming date:2016/4/21
 * time: 10:22
 * e-mail：962570483@qq.com
 */

public class WeatherRepository implements WeatherDataSource{

    protected static WeatherRepository mInstance;


    public static WeatherRepository getInstance()
    {
        if (mInstance==null)
            mInstance=new WeatherRepository();
        return mInstance;
    }

    @Override
    public Observable<WeatherApi> requestWeahterInfo(String city, String key) {
        WeatherService weatherService= ServiceFactory.createServiceFrom(WeatherService.class,WeatherService.ENDPOINT);
        return weatherService.requestWeatherInfo(city,key);
    }
}
