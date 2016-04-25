package com.nickming.justweather.weather;

import android.content.Context;

import com.nickming.justweather.base.BasePresenter;
import com.nickming.justweather.base.BaseView;
import com.nickming.justweather.common.WeatherHeaderViewFactory;
import com.nickming.justweather.weather.data.Weather;
import com.nickming.justweather.weather.data.WeatherApi;
import com.nickming.justweather.weather.data.source.CityType;

/**
 * desc:列表管理契约类
 *
 * @author:nickming date:2016/4/21
 * time: 10:04
 * e-mail：962570483@qq.com
 */

public interface WeatherContract {

    interface View extends BaseView<Presenter>
    {
        void showDrawer();

        void showSetting();

        void showAbout();

        void showSelectCity(String city);

        void showSnackBarMessage(String msg);

        void showWeather(Weather bean);

        void showRefresh();

        void showCompleteRefresh();

        void showHeaderAndDrawerView(WeatherHeaderViewFactory.HeaderViewType type);

    }

    interface Presenter extends BasePresenter
    {
        void requestWeatherData(String city,CityType Type);

        void addToFavourite();

        void login();

        void exit();

        void requestLocation(Context context);

        void judgeWeatherHeaderView(WeatherApi weatherApi);

    }
}
