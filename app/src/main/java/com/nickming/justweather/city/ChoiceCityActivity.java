package com.nickming.justweather.city;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nickming.justweather.R;
import com.nickming.justweather.base.BaseActivity;
import com.nickming.justweather.common.Constants;
import com.nickming.justweather.common.SettingManager;
import com.nickming.justweather.db.City;
import com.nickming.justweather.db.DBManager;
import com.nickming.justweather.db.Province;
import com.nickming.justweather.db.WeatherDB;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * desc:基类
 *
 * @author:nickming date:2016/4/21
 * time: 09:56
 * e-mail：962570483@qq.com
 */
public class ChoiceCityActivity extends BaseActivity {
    private static String TAG = ChoiceCityActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private DBManager mDBManager;
    private WeatherDB mWeatherDB;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private ArrayList<String> dataList = new ArrayList<>();
    private Province selectedProvince;
    private City selectedCity;
    private List<Province> provincesList;
    private List<City> cityList;
    private CityAdapter mAdapter;

    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_CITY = 2;
    private int currentLevel;



    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_city);

        Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                mDBManager = new DBManager(ChoiceCityActivity.this);
                mDBManager.openDatabase();
                mWeatherDB = new WeatherDB(ChoiceCityActivity.this);
                return Observable.just(1);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        initView();
                        initRecyclerView();
                        queryProvinces();
                    }
                });


    }


    private void initView() {
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("选择城市");
        //setSupportActionBar(toolbar);
        ImageView banner = (ImageView) findViewById(R.id.banner);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setStatusBarColorForKitkat(R.color.colorSunrise);
//        if (SettingManager.getInstance().getCurrentHour()< 6 || mSetting.getCurrentHour() > 18) {
//            collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this, R.color.colorSunset));
//            Glide.with(this).load(R.mipmap.city_night).diskCacheStrategy(DiskCacheStrategy.ALL).into(banner);
//            setStatusBarColorForKitkat(R.color.colorSunset);
//        }
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }


    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CityAdapter(this, dataList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provincesList.get(pos);
                    mRecyclerView.scrollTo(0, 0);
                    queryCities();
                }
                else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(pos);
                    Intent intent = new Intent();
                    String cityName = selectedCity.CityName;
                    intent.putExtra(SettingManager.CITY_NAME, cityName);
                    setResult(Constants.CHOICE_CITY_RESULT_CODE, intent);
                    finish();
                }
            }
        });

    }


    /**
     * 查询全国所有的省，从数据库查询
     */
    private void queryProvinces() {
        collapsingToolbarLayout.setTitle("选择省份");
        Observer<Province> observer = new Observer<Province>() {
            @Override public void onCompleted() {
                currentLevel = LEVEL_PROVINCE;
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
                //PLog.i(TAG,"省份");
            }


            @Override public void onError(Throwable e) {

            }


            @Override public void onNext(Province province) {
                //在这里做 RV 的动画效果 使用 Item 的更新
                dataList.add(province.ProName);
                //PLog.i(TAG,province.ProSort+"");
                //mAdapter.notifyItemInserted(province.ProSort-1);


            }
        };

        Observable.defer(new Func0<Observable<Province>>() {
            @Override
            public Observable<Province> call() {
                provincesList = mWeatherDB.loadProvinces(mDBManager.getDatabase());
                dataList.clear();
                mAdapter.notifyDataSetChanged();
                return Observable.from(provincesList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    /**
     * 查询选中省份的所有城市，从数据库查询
     */
    private void queryCities() {
        dataList.clear();
        mAdapter.notifyDataSetChanged();
        collapsingToolbarLayout.setTitle(selectedProvince.ProName);
        Observer<City> observer = new Observer<City>() {
            @Override public void onCompleted() {
                currentLevel = LEVEL_CITY;
                mAdapter.notifyDataSetChanged();
                //定位到第一个item
                mRecyclerView.smoothScrollToPosition(0);
                //PLog.i(TAG,"城市");
            }


            @Override public void onError(Throwable e) {

            }


            @Override public void onNext(City city) {
                dataList.add(city.CityName);
                //mAdapter.notifyItemInserted(city.CitySort);
            }
        };


        Observable.defer(new Func0<Observable<City>>() {
            @Override
            public Observable<City> call() {
                cityList = mWeatherDB.loadCities(mDBManager.getDatabase(), selectedProvince.ProSort);
                return Observable.from(cityList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentLevel == LEVEL_PROVINCE) {
                finish();
            }
            else {
                queryProvinces();
                mRecyclerView.smoothScrollToPosition(0);
            }
        }
        return false;
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        mDBManager.closeDatabase();
    }
}
