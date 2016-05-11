package com.nickming.justweather.common;

import android.util.Log;

import com.nickming.justweather.base.BaseApplication;
import com.nickming.justweather.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * desc:
 *
 * @author:nickming date:2016/4/20
 * time: 11:23
 * e-mail：962570483@qq.com
 */

public class ServiceFactory {
    private static final String TAG = ServiceFactory.class.getSimpleName();

    private static OkHttpClient mOkhttpclient;

    private static final long CACHE_MAX_SIZE = 20 * 1024 * 1024;

    /**
     * log拦截器
     */
    private static final Interceptor LoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.v(TAG, "request:" + request.toString());
            long t1 = System.nanoTime();
            okhttp3.Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
//            Log.v(TAG, String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
//                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
//            Log.v(TAG, String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
//                    response.request().url(), (t2 - t1) / 1e6d));
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            Log.i(TAG, "response body:" + content);
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    };

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor networkInterceptor =new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if(!NetworkUtils.hasNetwork(BaseApplication.mAppContext)){
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Log.i(TAG, "no network ");
            }
            Response originalResponse = chain.proceed(request);
            if(NetworkUtils.hasNetwork(BaseApplication.mAppContext)){
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            }else{
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };


    private static OkHttpClient getmOkhttpclient() {
        if (mOkhttpclient == null) {
            File file = new File(BaseApplication.cacheDir, "HttpCache");
            Cache cache = new Cache(file, CACHE_MAX_SIZE);
            mOkhttpclient = new OkHttpClient.Builder()
                    .addInterceptor(LoggingInterceptor)
                    .addNetworkInterceptor(networkInterceptor)
                    .addInterceptor(networkInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .cache(cache)
                    .build();
        }
        return mOkhttpclient;
    }

    public static <T> T createServiceFrom(Class<T> serviceClass, String endPoint) {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(endPoint)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getmOkhttpclient())
                .build();
        return adapter.create(serviceClass);
    }


}
