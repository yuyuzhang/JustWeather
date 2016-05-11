package com.nickming.justweather.dagger.module;

import com.nickming.justweather.dagger.scope.PerActivity;
import com.nickming.justweather.weather.WeatherFragment;
import com.nickming.justweather.weather.WeatherPresenter;
import com.nickming.justweather.weather.data.source.WeatherRepository;

import dagger.Module;
import dagger.Provides;

/**
 * desc:
 *
 * @author:nickming date:2016/4/22
 * time: 01:45
 * e-mail：962570483@qq.com
 */
@Module
public class WeatherModule {

    WeatherFragment weatherFragment;

    public WeatherModule(WeatherFragment weatherFragment) {
        this.weatherFragment = weatherFragment;
    }

    @PerActivity
    @Provides
    public WeatherPresenter provideWeatherPresenter()
    {
        return new WeatherPresenter(WeatherRepository.getInstance(),this.weatherFragment);
    }


}
