package com.nickming.justweather.dagger.module;

import android.app.Activity;

import com.nickming.justweather.dagger.scope.PerActivity;

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
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity()
    {
        return this.activity;
    }
}
