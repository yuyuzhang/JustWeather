package com.nickming.justweather.dagger.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * desc:
 *
 * @author:nickming date:2016/4/22
 * time: 01:24
 * e-mail：962570483@qq.com
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
