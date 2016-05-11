package com.nickming.justweather.dagger.component;

import com.nickming.justweather.dagger.module.ActivityModule;
import com.nickming.justweather.dagger.module.WeatherModule;
import com.nickming.justweather.dagger.scope.PerActivity;
import com.nickming.justweather.weather.WeatherActivity;

import dagger.Component;

/**
 * desc:
 *
 * @author:nickming date:2016/4/22
 * time: 01:40
 * e-mail：962570483@qq.com
 */
@PerActivity
@Component(dependencies = AppComponent.class,modules = {ActivityModule.class, WeatherModule.class})
public interface WeatherComponent extends ActivityComponent{

    void inject(WeatherActivity weatherActivity);

    WeatherFragmentComponent weatherFragmentComponent();

}
