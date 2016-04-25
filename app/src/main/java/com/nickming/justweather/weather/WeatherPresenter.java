package com.nickming.justweather.weather;

import android.content.Context;
import android.util.Log;

import com.nickming.justweather.R;
import com.nickming.justweather.common.WeatherHeaderViewFactory;
import com.nickming.justweather.dagger.scope.PerActivity;
import com.nickming.justweather.setting.SettingManager;
import com.nickming.justweather.utils.TimeUtil;
import com.nickming.justweather.weather.data.WeatherApi;
import com.nickming.justweather.weather.data.source.CityType;
import com.nickming.justweather.weather.data.source.WeatherDataSource;
import com.nickming.justweather.weather.data.source.WeatherRepository;

import java.util.Locale;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * desc:
 *
 * @author:nickming date:2016/4/21
 * time: 11:12
 * e-mail：962570483@qq.com
 */
@PerActivity
public class WeatherPresenter implements WeatherContract.Presenter {

    private static final String TAG = WeatherPresenter.class.getSimpleName();

    protected WeatherRepository mRepository;

    protected WeatherContract.View mWeatherView;

    protected SettingManager settingManager;

    @Inject
    public WeatherPresenter(WeatherRepository mRepository, WeatherContract.View mWeatherView) {
        this.mRepository = mRepository;
        this.mWeatherView = mWeatherView;
        this.settingManager = SettingManager.getInstance();
        mWeatherView.setPresenter(this);
    }

    @Override
    public void requestWeatherData(String city, CityType type) {
        if (type == CityType.CITY_ID) {
            mWeatherView.showRefresh();
            mRepository.requestWeahterInfo("CN101010100", "19713447578c4afe8c12a351d46ea922")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<WeatherApi>() {
                        @Override
                        public void onCompleted() {
                            mWeatherView.showCompleteRefresh();
                            mWeatherView.showSnackBarMessage("刷新完成!");
                        }

                        @Override
                        public void onError(Throwable e) {
                            mWeatherView.showCompleteRefresh();
                            mWeatherView.showSnackBarMessage("请求出错!");
                            Log.i(TAG, "onError :" + String.format(Locale.CHINA, e.toString()));
                        }

                        @Override
                        public void onNext(WeatherApi weatherApi) {
                            mWeatherView.showWeather(weatherApi.mHeWeatherDataService30s.get(0));
                            mWeatherView.showSelectCity(weatherApi.mHeWeatherDataService30s.get(0).basic.city);
                            judgeWeatherHeaderView(weatherApi);
                        }
                    });
        } else {
            mWeatherView.showRefresh();
            String cityName = city;
            if (cityName != null) {
                cityName = cityName.replace("市", "")
                        .replace("省", "")
                        .replace("自治区", "")
                        .replace("特别行政区", "")
                        .replace("地区", "")
                        .replace("盟", "");
            }
            mRepository.requestWeahterForName(cityName, "19713447578c4afe8c12a351d46ea922")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<WeatherApi>() {
                        @Override
                        public void onCompleted() {
                            mWeatherView.showCompleteRefresh();
                            mWeatherView.showSnackBarMessage("刷新完成!");
                        }

                        @Override
                        public void onError(Throwable e) {
                            mWeatherView.showSnackBarMessage("请求出错!");
                            mWeatherView.showCompleteRefresh();
                            Log.i(TAG, "onError :" + String.format(Locale.CHINA, e.toString()));
                        }

                        @Override
                        public void onNext(WeatherApi weatherApi) {
                            settingManager.putString(SettingManager.CITY_NAME, weatherApi.mHeWeatherDataService30s.get(0).basic.city);
                            mWeatherView.showWeather(weatherApi.mHeWeatherDataService30s.get(0));
                            mWeatherView.showSelectCity(weatherApi.mHeWeatherDataService30s.get(0).basic.city);
                            judgeWeatherHeaderView(weatherApi);
                        }
                    });
        }

    }

    @Override
    public void addToFavourite() {

    }

    @Override
    public void login() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void requestLocation(Context context) {
        mRepository.requestCityName(context, new WeatherDataSource.RequestLocationCallback() {
            @Override
            public void onSuccess(String city) {
                requestWeatherData(city, CityType.CITY_NAME);
                mWeatherView.showSnackBarMessage("定位成功!" + city);
            }

            @Override
            public void onFailure(String msg) {
                mWeatherView.showSnackBarMessage("定位失败!");
                requestWeatherData(settingManager.getString(SettingManager.CITY_NAME, "佛山"), CityType.CITY_NAME);

            }
        });
    }

    @Override
    public void judgeWeatherHeaderView(WeatherApi weatherApi) {

        if (TimeUtil.isNightMode()) {
            mWeatherView.showHeaderAndDrawerView(WeatherHeaderViewFactory.HeaderViewType.NIGHT);
        } else {
            String cond = weatherApi.mHeWeatherDataService30s.get(0).dailyForecast.get(0).cond.txtD;
            if (cond.contains("雨"))
                mWeatherView.showHeaderAndDrawerView(WeatherHeaderViewFactory.HeaderViewType.DAY_RAINY);
            else if (cond.contains("云"))
                mWeatherView.showHeaderAndDrawerView(WeatherHeaderViewFactory.HeaderViewType.DAY_CLOUDY);
            else
                mWeatherView.showHeaderAndDrawerView(WeatherHeaderViewFactory.HeaderViewType.DAY_SUNNY);
        }

    }

    @Override
    public void start() {
        settingManager.putInt("未知", R.mipmap.none);
        settingManager.putInt("晴", R.mipmap.type_one_sunny);
        settingManager.putInt("阴", R.mipmap.type_one_cloudy);
        settingManager.putInt("多云", R.mipmap.type_one_cloudy);
        settingManager.putInt("少云", R.mipmap.type_one_cloudy);
        settingManager.putInt("晴间多云", R.mipmap.type_one_cloudytosunny);
        settingManager.putInt("小雨", R.mipmap.type_one_light_rain);
        settingManager.putInt("中雨", R.mipmap.type_one_middle_rain);
        settingManager.putInt("大雨", R.mipmap.type_one_heavy_rain);
        settingManager.putInt("阵雨", R.mipmap.type_one_heavy_rain);
        settingManager.putInt("暴雨", R.mipmap.type_one_thunderstorm);
        settingManager.putInt("雷阵雨", R.mipmap.type_one_thunderstorm);
        settingManager.putInt("霾", R.mipmap.type_one_fog);
        settingManager.putInt("雾", R.mipmap.type_one_fog);
    }
}
