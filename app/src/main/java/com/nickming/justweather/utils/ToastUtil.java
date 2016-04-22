package com.nickming.justweather.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * desc:
 *
 * @author:nickming date:2016/4/19
 * time: 14:53
 * e-mail：962570483@qq.com
 */
public class ToastUtil {

    private Context mContext;

    public ToastUtil(Context context){
        this.mContext = context;
    }

    public void showToast(String message){
        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }


}
