package com.nickming.justweather.dagger.component;

import android.app.Activity;

import com.nickming.justweather.dagger.module.ActivityModule;
import com.nickming.justweather.dagger.scope.PerActivity;

import dagger.Component;

/**
 * desc:
 *
 * @author:nickming date:2016/4/22
 * time: 01:33
 * e-mail：962570483@qq.com
 */
@PerActivity
@Component(modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();
}
