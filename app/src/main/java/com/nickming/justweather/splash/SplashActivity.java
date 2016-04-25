package com.nickming.justweather.splash;

import android.os.Bundle;

import com.nickming.justweather.R;
import com.nickming.justweather.base.BaseActivity;
import com.nickming.justweather.utils.ShareWechatUtil;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        registWxApi();
        ShareWechatUtil.shareText(mWxApi,"测试",false);
    }

}
