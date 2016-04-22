package com.nickming.justweather.weather.data.source;

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

    Observable<WeatherApi> requestWeahterInfo(String city,String key);
}
