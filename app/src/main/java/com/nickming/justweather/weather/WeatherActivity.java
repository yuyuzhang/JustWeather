package com.nickming.justweather.weather;

import android.os.Bundle;

import com.nickming.justweather.R;
import com.nickming.justweather.base.BaseActivity;
import com.nickming.justweather.dagger.component.DaggerWeatherComponent;
import com.nickming.justweather.dagger.component.WeatherComponent;
import com.nickming.justweather.dagger.module.WeatherModule;
import com.nickming.justweather.utils.ActivityUtils;

import javax.inject.Inject;

public class WeatherActivity extends BaseActivity {

    protected WeatherComponent mComponent;
    @Inject
    protected WeatherPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_list);

        WeatherFragment weatherFragment = (WeatherFragment) getSupportFragmentManager().findFragmentById(R.id.fl_weather_content);
        if (weatherFragment == null) {
            weatherFragment = WeatherFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), weatherFragment, R.id.fl_weather_content);
        }
        mComponent= DaggerWeatherComponent.builder().appComponent(getAppComponent())
                .activityModule(getActivityModule())
                .weatherModule(new WeatherModule(weatherFragment))
                .build();
        mComponent.inject(this);
    }

    public WeatherComponent getmComponent() {
        return mComponent;
    }
}
