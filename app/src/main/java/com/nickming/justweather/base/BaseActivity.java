package com.nickming.justweather.base;

import android.support.v7.app.AppCompatActivity;

import com.nickming.justweather.common.Constants;
import com.nickming.justweather.dagger.component.AppComponent;
import com.nickming.justweather.dagger.module.ActivityModule;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * desc:基类
 *
 * @author:nickming date:2016/4/21
 * time: 09:56
 * e-mail：962570483@qq.com
 */

public class BaseActivity extends AppCompatActivity{

    protected IWXAPI mWxApi;

    protected void registWxApi()
    {
        mWxApi= WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID,false);
        mWxApi.registerApp(Constants.WECHAT_APP_ID);
    }

    protected AppComponent getAppComponent() {
        return ((BaseApplication) getApplication()).getAppComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

}
