package com.nickming.justweather.dagger.module;

import android.content.Context;

import com.nickming.justweather.utils.NetworkUtil;
import com.nickming.justweather.utils.ToastUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * desc:
 *
 * @author:nickming date:2016/4/22
 * time: 01:24
 * e-mail：962570483@qq.com
 */
@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return this.context;
    }

    @Provides
    @Singleton
    public ToastUtil provideToastUtil() {
        return new ToastUtil(context);
    }

    @Provides
    @Singleton
    public NetworkUtil provideNetWorkUtils()
    {
        return new NetworkUtil(context);
    }
}
