package com.nickming.justweather.github;

import rx.Observable;

/**
 * desc:
 *
 * @author:nickming date:2016/4/21
 * time: 23:20
 * e-mail：962570483@qq.com
 */

public interface GitHubDataSource {

    Observable<UserBean> requestNetworkInfo(String username);
}
