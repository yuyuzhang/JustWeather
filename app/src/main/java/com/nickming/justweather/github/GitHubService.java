package com.nickming.justweather.github;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

/**
 * desc:
 *
 * @author:nickming date:2016/4/19
 * time: 20:14
 * e-mail：962570483@qq.com
 */

public interface GitHubService {

    String ENDPOINT = "https://api.github.com";

    @Headers("Cache-Control: public, max-age=3600")
    @GET("/users/{user}")
    Observable<UserBean> requestUserInfo(@Path(("user")) String username);


}
