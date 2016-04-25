package com.nickming.justweather.setting;

import android.content.Context;
import android.content.SharedPreferences;

import com.nickming.justweather.base.BaseApplication;

/**
 * desc:程序设置相关
 *
 * @author:nickming date:2016/4/22
 * time: 00:28
 * e-mail：962570483@qq.com
 */

public class SettingManager {

    protected static final String TAG = SettingManager.class.getSimpleName();
    public static final String CHANGE_ICONS = "change_icons";//切换图标
    public static final String CLEAR_CACHE = "clear_cache";//清空缓存
    public static final String AUTO_UPDATE = "change_update_time"; //自动更新时长
    public static final String CITY_NAME = "城市";//选择城市
    public static final String HOUR = "小时";//当前小时
    public static final String HOUR_SELECT = "hour_select"; //设置更新频率的联动-需要改进

    public static final String API_TOKEN = "****";//fir.im api_token
    public static final String KEY = "19713447578c4afe8c12a351d46ea922";// 和风天气 key

    public static int ONE_HOUR = 3600;

    protected static SettingManager mInstance;

    private SharedPreferences mPrefrences;

    public static SettingManager getInstance() {
        if (mInstance == null) {
            synchronized (SettingManager.class) {
                if (mInstance == null)
                    mInstance = new SettingManager(BaseApplication.mAppContext);
            }

        }
        return mInstance;
    }

    public SettingManager(Context context) {
        mPrefrences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public SettingManager putBoolean(String key, boolean value) {
        mPrefrences.edit().putBoolean(key, value).apply();
        return this;
    }

    public boolean getBoolean(String key, boolean def) {
        return mPrefrences.getBoolean(key, def);
    }

    public SettingManager putInt(String key, int value) {
        mPrefrences.edit().putInt(key, value).apply();
        return this;
    }


    public int getInt(String key, int defValue) {
        return mPrefrences.getInt(key, defValue);
    }


    public SettingManager putString(String key, String value) {
        mPrefrences.edit().putString(key, value).apply();
        return this;
    }


    public String getString(String key, String defValue) {
        return mPrefrences.getString(key, defValue);
    }

}
