package com.nickming.justweather.weather;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.nickming.justweather.R;
import com.nickming.justweather.base.BaseFragment;
import com.nickming.justweather.common.SettingManager;
import com.nickming.justweather.common.WeatherHeaderViewFactory;
import com.nickming.justweather.dagger.component.WeatherFragmentComponent;
import com.nickming.justweather.utils.HiddingScrollListener;
import com.nickming.justweather.utils.NetworkUtil;
import com.nickming.justweather.utils.ToastUtil;
import com.nickming.justweather.weather.data.Weather;
import com.nickming.justweather.weather.data.source.CityType;
import com.nickming.justweather.weather.data.source.WeatherRepository;
import com.yalantis.phoenix.PullToRefreshView;

import javax.inject.Inject;

import butterknife.Bind;
import io.github.yavski.fabspeeddial.FabSpeedDial;

/**
 * desc:天气具体列表fragment
 *
 * @author:nickming date:2016/4/21
 * time: 11:21
 * e-mail：962570483@qq.com
 */

public class WeatherFragment extends BaseFragment implements WeatherContract.View
        , PullToRefreshView.OnRefreshListener, FabSpeedDial.MenuListener,NavigationView.OnNavigationItemSelectedListener {

    protected WeatherContract.Presenter mPresenter;
    protected WeatherFragmentComponent component;

    protected static final int STATUS_BAR_ICON=1011;

    @Inject
    ToastUtil toastUtil;
    @Inject
    NetworkUtil networkUtil;

    protected CollapsingToolbarLayout collapsingToolbarLayout;
    protected Toolbar toolbar;
    protected DrawerLayout drawer;
    protected NavigationView mNavigationView;
    protected ImageView mDrawerBackGround;
    protected ImageView mWeahterHeadView;
    protected SwitchCompat mNightMode;


    @Bind(R.id.recycleview_weather_list)
    protected RecyclerView mRecycleView;
    @Bind(R.id.pull_to_refresh_view)
    protected PullToRefreshView mRefreshView;

    protected FabSpeedDial mFab;

    protected WeatherAdapter madapter;


    public WeatherFragment() {
    }

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weather_list;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (getActivity() instanceof WeatherActivity) {
            component = ((WeatherActivity) getActivity()).getmComponent().weatherFragmentComponent();
            component.inject(this);
        }

        mFab = (FabSpeedDial) getActivity().findViewById(R.id.fb_weather);
        collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mWeahterHeadView= (ImageView) getActivity().findViewById(R.id.toolbar_iv_image);
        initDrawer();
        initRecycleleView();

        mFab.setMenuListener(this);

        mRefreshView.setOnRefreshListener(this);

        mPresenter.start();

        mPresenter.requestLocation(getActivity());
//        initLocation();

    }


    private void initDrawer() {
        mNavigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mDrawerBackGround = (ImageView) mNavigationView.inflateHeaderView(R.layout.view_drawer_header)
                .findViewById(R.id.header_background);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNightMode= (SwitchCompat) mNavigationView.getMenu().getItem(2).getActionView();
        mNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i(TAG, "onCheckedChanged: "+String.valueOf(b));
                mPresenter.changeNightMode(b);
            }
        });
        toggle.syncState();
    }

    /**
     * 初始化reycleview
     */
    private void initRecycleleView() {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mFab.getLayoutParams();
        final int fabBottomMargin = lp.bottomMargin;
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.addOnScrollListener(new HiddingScrollListener() {
            @Override
            public void onHide() {
                mFab.animate()
                        .translationY(mFab.getHeight() + fabBottomMargin)
                        .setInterpolator(new AccelerateInterpolator(2))
                        .start();
            }

            @Override
            public void onShow() {
                mFab.animate()
                        .translationY(0)
                        .setInterpolator(new DecelerateInterpolator(2))
                        .start();
            }
        });
    }

    @Override
    protected void onCreate() {
//        madapter=new WeatherAdapter(getActivity(),null);

    }



    @Override
    public void showSelectCity(String city) {
        collapsingToolbarLayout.setTitle(city);
    }

    @Override
    public void showSnackBarMessage(String msg) {
//        toastUtil.showToast(msg);
        Snackbar.make(mFab, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showWeather(Weather bean) {
        if (SettingManager.getInstance().getBoolean(SettingManager.NIGHT_MODE,false))
            mRecycleView.setBackgroundColor(Color.rgb(26,26,26));
        madapter = new WeatherAdapter(getActivity(), bean,SettingManager.getInstance().getBoolean(SettingManager.NIGHT_MODE,false));
        mRecycleView.setAdapter(madapter);
    }

    @Override
    public void showRefresh() {
        mRefreshView.setRefreshing(true);
    }

    @Override
    public void showCompleteRefresh() {
        mRefreshView.setRefreshing(false);
    }

    @Override
    public void showHeaderAndDrawerView(WeatherHeaderViewFactory.HeaderViewType type) {
        WeatherHeaderViewFactory.setWeatherHeaderView(type,mWeahterHeadView);
        WeatherHeaderViewFactory.setWeatherHeaderView(type,mDrawerBackGround);
    }

    @Override
    public void showFinishedActivity() {
        getActivity().finish();
    }

    @Override
    public void showNormalModeView() {
        SettingManager.getInstance().putBoolean(SettingManager.NIGHT_MODE,false);
        mNavigationView.setBackgroundColor(Color.WHITE);
        madapter=new WeatherAdapter(getActivity(), WeatherRepository.getInstance().loadCurrentWeatherApi().mHeWeatherDataService30s.get(0),false);
        mRecycleView.setAdapter(madapter);
        mRecycleView.setBackgroundColor(Color.WHITE);
        mNavigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
    }

    @Override
    public void showNightModeView() {
        SettingManager.getInstance().putBoolean(SettingManager.NIGHT_MODE,true);
        mNavigationView.setBackgroundColor(Color.rgb(26,26,26));
//        mNavigationView.setBackgroundColor(Color.rgb(23,24,26));
        mRecycleView.setBackgroundColor(Color.rgb(26,26,26));
        madapter=new WeatherAdapter(getActivity(), WeatherRepository.getInstance().loadCurrentWeatherApi().mHeWeatherDataService30s.get(0),true);
        mRecycleView.setAdapter(madapter);
        mNavigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
    }

    @Override
    public void showStatusBarIcon() {
        NotificationManager manager= (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification=new Notification();
//        notification.icon=R.drawable.ic_menu_send;
//        // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
//        notification.flags |= Notification.FLAG_ONGOING_EVENT;
//        // 表明在点击了通知栏中的"清除通知"后，此通知不清除， 经常与FLAG_ONGOING_EVENT一起使用
//        notification.flags |=Notification.FLAG_NO_CLEAR;
//        PendingIntent intent=PendingIntent.getActivity(getActivity(),0,getActivity().getIntent(),0);
//        notification.contentIntent=intent;
//        notification.tickerText="test";
//        manager.notify(12,notification);
        Intent intent=new Intent(getActivity(),WeatherActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(getActivity(),0,intent,0);
        Weather.NowEntity entity=WeatherRepository.getInstance().loadCurrentWeatherApi().mHeWeatherDataService30s.get(0).now;
        Notification notification=new Notification.Builder(getActivity())
                .setSmallIcon(R.drawable.ic_menu_send)
                .setContentTitle("JustWeather")
                .setTicker("")
                .setContentText("现在天气情况 "+entity.tmp+"°")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
//        // 表明在点击了通知栏中的"清除通知"后，此通知不清除， 经常与FLAG_ONGOING_EVENT一起使用
        notification.flags |=Notification.FLAG_NO_CLEAR;

        manager.notify(STATUS_BAR_ICON,notification);
    }

    @Override
    public void hideStatusBarIcon() {
        NotificationManager manager= (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(STATUS_BAR_ICON);
    }

    @Override
    public void setPresenter(WeatherContract.Presenter presenter) {
        if (presenter != null)
            this.mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        mPresenter.requestWeatherData(SettingManager.getInstance().getString(SettingManager.CITY_NAME, "佛山"), CityType.CITY_NAME);
    }

    @Override
    public boolean onPrepareMenu(NavigationMenu navigationMenu) {
        return true;
    }

    @Override
    public boolean onMenuItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_share_to_friends:
                mPresenter.shareToWechat(getActivity(),true);
                break;
            case R.id.action_share_to_moments:
                mPresenter.shareToWechat(getActivity(),false);
                break;
            case R.id.action_message:
                mPresenter.sendMessage(getActivity());
                break;
        }
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        hideStatusBarIcon();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_city:
                mPresenter.startSelectCityActivity((WeatherActivity) getActivity());
                break;
            case R.id.nav_about:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityRersult(requestCode,resultCode,data);
    }




}
