package com.nickming.justweather.utils;

import android.support.v7.widget.RecyclerView;

/**
 * desc:recycleview隐藏监听
 *
 * @author:nickming date:2016/4/22
 * time: 17:05
 * e-mail：962570483@qq.com
 */

public abstract class HiddingScrollListener extends RecyclerView.OnScrollListener{

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;


    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        }
        else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }
        if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
            scrolledDistance += dy;
        }

    }


    public abstract void onHide();

    public abstract void onShow();
}
