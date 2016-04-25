package com.nickming.justweather.weather;

import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.nickming.justweather.R;
import com.nickming.justweather.base.BaseFragment;
import com.nickming.justweather.common.WeatherHeaderViewFactory;
import com.nickming.justweather.dagger.component.WeatherFragmentComponent;
import com.nickming.justweather.setting.SettingManager;
import com.nickming.justweather.utils.HiddingScrollListener;
import com.nickming.justweather.utils.NetworkUtil;
import com.nickming.justweather.utils.ToastUtil;
import com.nickming.justweather.weather.data.Weather;
import com.nickming.justweather.weather.data.source.CityType;
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
        , PullToRefreshView.OnRefreshListener, FabSpeedDial.MenuListener {

    protected WeatherContract.Presenter mPresenter;
    protected WeatherFragmentComponent component;

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
    public void showDrawer() {

    }

    @Override
    public void showSetting() {

    }

    @Override
    public void showAbout() {

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
        madapter = new WeatherAdapter(getActivity(), bean);
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
            case R.id.action_favourite:
                break;
            case R.id.action_message:
                break;
            case R.id.action_weixin:
                break;
        }
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
