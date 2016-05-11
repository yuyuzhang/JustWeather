package com.nickming.justweather.dagger.component;

import android.content.Context;

import com.nickming.justweather.dagger.module.AppModule;
import com.nickming.justweather.utils.NetworkUtil;
import com.nickming.justweather.utils.ToastUtil;

import javax.inject.Singleton;

import dagger.Component;

/**
 * desc:
 *
 * @author:nickming date:2016/4/22
 * time: 01:31
 * e-mail：962570483@qq.com
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    Context getContext();

    ToastUtil getToastUtil();

    NetworkUtil getNetworkUtil();
}
