package com.nickming.justweather.weather.data.source;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nickming.justweather.common.ServiceFactory;
import com.nickming.justweather.weather.data.WeatherApi;

import rx.Observable;

/**
 * desc:天气数据处理类
 *
 * @author:nickming date:2016/4/21
 * time: 10:22
 * e-mail：962570483@qq.com
 */

public class WeatherRepository implements WeatherDataSource {

    private static final String TAG = WeatherRepository.class.getSimpleName();

    protected static WeatherRepository mInstance;

    public static WeatherRepository getInstance() {
        if (mInstance == null)
            mInstance = new WeatherRepository();
        return mInstance;
    }

    @Override
    public Observable<WeatherApi> requestWeahterInfo(String city, String key) {
        WeatherService weatherService = ServiceFactory.createServiceFrom(WeatherService.class, WeatherService.ENDPOINT);
        return weatherService.requestWeatherInfo(city, key);
    }

    @Override
    public Observable<WeatherApi> requestWeahterForName(String cityName, String key) {
        WeatherService weatherService = ServiceFactory.createServiceFrom(WeatherService.class, WeatherService.ENDPOINT);
        return weatherService.requestWeatherForCityName(cityName, key);
    }

    @Override
    public void requestCityName(Context context, final RequestLocationCallback callback) {
        final LocationClient client = new LocationClient(context);
        BDLocationListener locationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                client.stop();
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    if (callback!=null)
                    {
                        callback.onSuccess(bdLocation.getCity().toString());
                        Log.i(TAG, "onReceiveLocation :gps定位成功"+bdLocation.getCity().toString());
                    }
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    if (callback!=null)
                    {
                        callback.onSuccess(bdLocation.getCity().toString());
                        Log.i(TAG, "onReceiveLocation :网络定位成功"+bdLocation.getCity().toString());
                    }
                } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    if (callback!=null)
                    {
                        callback.onSuccess(bdLocation.getCity().toString());
                        Log.i(TAG, "onReceiveLocation :离线定位成功，离线定位结果也是有效的"+bdLocation.getCity().toString());
                    }
                } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {
                    if (callback!=null)
                    {
                        callback.onFailure("服务端网络定位失败");
                        Log.i(TAG, "onReceiveLocation :服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                    }
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                    if (callback!=null)
                    {
                        callback.onFailure("服务端网络定位失败");
                        Log.i(TAG, "onReceiveLocation :网络不同导致定位失败，请检查网络是否通畅");
                    }
                } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                    if (callback!=null)
                    {
                        callback.onFailure("飞行模式");
                        Log.i(TAG, "onReceiveLocation :无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    }
                }
            }

        };
        client.registerLocationListener(locationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        client.setLocOption(option);
        client.start();
    }
}
