package com.nickming.justweather.weather;

import com.nickming.justweather.base.BasePresenter;
import com.nickming.justweather.base.BaseView;
import com.nickming.justweather.weather.data.Weather;

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

        void showSelectCity();

        void showSnackBarMessage(String msg);

        void showWeatherList(Weather bean);


    }

    interface Presenter extends BasePresenter
    {
        void requestWeatherData(String city);

        void addToFavourite();

        void login();

        void exit();

    }
}
