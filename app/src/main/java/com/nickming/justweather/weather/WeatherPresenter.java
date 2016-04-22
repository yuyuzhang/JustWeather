package com.nickming.justweather.weather;

import android.util.Log;

import com.nickming.justweather.dagger.scope.PerActivity;
import com.nickming.justweather.weather.data.WeatherApi;
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

    @Inject
    public WeatherPresenter(WeatherRepository mRepository, WeatherContract.View mWeatherView) {
        this.mRepository = mRepository;
        this.mWeatherView = mWeatherView;
        mWeatherView.setPresenter(this);
    }

    @Override
    public void requestWeatherData(String city) {

        mRepository.requestWeahterInfo("CN101010100","19713447578c4afe8c12a351d46ea922")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherApi>() {
                    @Override
                    public void onCompleted() {
                        mWeatherView.showSnackBarMessage("获取成功!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError :" + String.format(Locale.CHINA, e.toString()));
                    }

                    @Override
                    public void onNext(WeatherApi weatherApi) {
                        mWeatherView.showWeatherList(weatherApi.mHeWeatherDataService30s.get(0));
                    }
                });

//        Observable.from(GitHubRepository.mUserList)
//                .flatMap(new Func1<String, Observable<UserBean>>() {
//                    @Override
//                    public Observable<UserBean> call(String s) {
//                        return GitHubRepository.getInstance().requestNetworkInfo(s);
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<UserBean>() {
//                    @Override
//                    public void onCompleted() {
//                        mWeatherView.showSnackBarMessage("获取完毕");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i(TAG, "onError :" + e.toString());
//                    }
//
//                    @Override
//                    public void onNext(UserBean bean) {
//                        mWeatherView.showWeatherList(bean);
//                    }
//                });
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
    public void start() {

    }
}
