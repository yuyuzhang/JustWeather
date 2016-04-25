package com.nickming.justweather.common;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nickming.justweather.R;

/**
 * desc:
 *
 * @author:nickming date:2016/4/23
 * time: 13:39
 * e-mail：962570483@qq.com
 */

public class WeatherHeaderViewFactory {

    public enum HeaderViewType
    {
        NIGHT_BEFORE,
        NIGHT,
        DAY_SUNNY,
        DAY_CLOUDY,
        DAY_RAINY
    }

    public static void setWeatherHeaderView(HeaderViewType type,ImageView imageView)
    {
        switch (type)
        {
            case NIGHT:
                Glide.with(imageView.getContext()).load(R.mipmap.pic_head_night).into(imageView);
                break;
            case NIGHT_BEFORE:
                Glide.with(imageView.getContext()).load(R.mipmap.pic_head_night_before).into(imageView);
                break;
            case DAY_CLOUDY:
                Glide.with(imageView.getContext()).load(R.mipmap.pic_head_cloudy).into(imageView);
                break;
            case DAY_RAINY:
                Glide.with(imageView.getContext()).load(R.mipmap.pic_head_rainy).into(imageView);
                break;
            case DAY_SUNNY:
                Glide.with(imageView.getContext()).load(R.mipmap.pic_head_sunny).into(imageView);
                break;
            default:
                Glide.with(imageView.getContext()).load(R.mipmap.pic_head_sunny).into(imageView);
                break;
        }
    }

}
