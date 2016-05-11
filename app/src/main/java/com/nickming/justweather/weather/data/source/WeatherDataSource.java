package com.nickming.justweather.weather.data.source;

import android.content.Context;

import com.nickming.justweather.weather.data.WeatherApi;

import rx.Observable;

/**
 * desc:天气接口处理类
 *
 * @author:nickming date:2016/4/21
 * time: 10:22
 * e-mail：962570483@qq.com
 */

public interface WeatherDataSource {

    interface RequestLocationCallback
    {
        void onSuccess(String city);

        void onFailure(String msg);
    }

    Observable<WeatherApi> requestWeahterInfo(String city,String key);

    Observable<WeatherApi> requestWeahterForName(String cityName,String key);

    void requestCityName(Context context,RequestLocationCallback callback);

    WeatherApi loadCurrentWeatherApi();

    void setCurrentWeatherApi(WeatherApi weatherApi);
}
