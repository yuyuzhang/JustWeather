package com.nickming.justweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * desc:
 *
 * @author:nickming date:2016/4/22
 * time: 01:29
 * e-mail：962570483@qq.com
 */

public class NetworkUtil {

    protected  Context context;

    public NetworkUtil(Context context) {
        this.context = context;
    }

    /**
     * 是否有网络连接
     *
     * @return
     */
    public  boolean hasNetwork() {
        try {
            ConnectivityManager localConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
            if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable()))
                return true;
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
        return false;
    }

    /**
     * {@link android.Manifest.permission#ACCESS_NETWORK_STATE}.
     *
     * @return
     */
    public  boolean isWifi() {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info == null)
            return false;
        return info.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
