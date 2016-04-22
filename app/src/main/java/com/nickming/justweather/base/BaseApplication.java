package com.nickming.justweather.base;

import android.app.Application;
import android.content.Context;

import com.nickming.justweather.dagger.component.AppComponent;
import com.nickming.justweather.dagger.component.DaggerAppComponent;
import com.nickming.justweather.dagger.module.AppModule;

/**
 * desc:
 *
 * @author:nickming date:2016/4/21
 * time: 10:43
 * e-mail：962570483@qq.com
 */

public class BaseApplication extends Application{

    private AppComponent appComponent;
    public static String cacheDir = "";
    public static Context mAppContext = null;


    @Override public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();

        /**
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
         */
        appComponent=DaggerAppComponent.builder().appModule(new AppModule(this)).build();

        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
            cacheDir = getApplicationContext().getExternalCacheDir().getAbsolutePath().toString();
        }
        else {
            cacheDir = getApplicationContext().getCacheDir().getAbsolutePath().toString();
        }
    }

    private boolean ExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        else {
            return false;
        }
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
