package com.nickming.justweather.github;

import com.nickming.justweather.common.ServiceFactory;

import rx.Observable;

/**
 * desc:
 *
 * @author:nickming date:2016/4/21
 * time: 23:20
 * e-mail：962570483@qq.com
 */

public class GitHubRepository implements GitHubDataSource{

    public static final String[] mUserList = {"nickming", "coffee", "SpikeKing", "JakeWharton", "rock3r", "Takhion", "dextorer", "Mariuxtheone"};

    protected static GitHubRepository mInstance;
    private GitHubRepository()
    {
    }

    public static GitHubRepository getInstance()
    {
        if (mInstance==null)
            mInstance=new GitHubRepository();
        return mInstance;
    }

    @Override
    public Observable<UserBean> requestNetworkInfo(String username) {
        GitHubService service= ServiceFactory.createServiceFrom(GitHubService.class, GitHubService.ENDPOINT);
        return service.requestUserInfo(username);
    }

    //    @Override
//    public Observable<UserBean> requestNetworkInfo(String username) {
//        GitHubService service=ServiceFactory.createServiceFrom(GitHubService.class,GitHubService.ENDPOINT);
//        return service.requestUserInfo(CACHE_CONTROL_CACHE,username);
//    }
//
//    public Observable<UserBean> requestUserCache(String username)
//    {
//        GitHubService service=ServiceFactory.createServiceFrom(GitHubService.class,GitHubService.ENDPOINT);
//        return service.requestUserInfo(CACHE_CONTROL_NETWORK,username);
//    }
}
