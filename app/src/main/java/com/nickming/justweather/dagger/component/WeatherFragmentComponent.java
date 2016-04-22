package com.nickming.justweather.dagger.component;

import com.nickming.justweather.dagger.scope.PerActivity;
import com.nickming.justweather.weather.WeatherFragment;

import dagger.Subcomponent;

/**
 * desc:
 *
 * @author:nickming date:2016/4/22
 * time: 01:42
 * e-mail：962570483@qq.com
 */
@Subcomponent
@PerActivity
public interface WeatherFragmentComponent {

    void inject(WeatherFragment weatherFragment);
}
