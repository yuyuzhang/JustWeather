package com.nickming.justweather.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nickming.justweather.R;
import com.nickming.justweather.base.BaseFragment;
import com.nickming.justweather.dagger.component.WeatherFragmentComponent;
import com.nickming.justweather.utils.NetworkUtil;
import com.nickming.justweather.utils.ToastUtil;
import com.nickming.justweather.weather.data.Weather;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * desc:
 *
 * @author:nickming date:2016/4/21
 * time: 11:21
 * e-mail：962570483@qq.com
 */

public class WeatherFragment extends BaseFragment implements WeatherContract.View {

    protected WeatherContract.Presenter mPresenter;
    protected WeatherFragmentComponent component;

    @Inject
    ToastUtil toastUtil;
    @Inject
    NetworkUtil networkUtil;

    @Bind(R.id.recycleview_weather_list)
    protected RecyclerView mRecycleView;

    protected WeatherAdapter madapter;


    public WeatherFragment() {
    }

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof WeatherActivity)
        {
            component=((WeatherActivity) getActivity()).getmComponent().weatherFragmentComponent();
            component.inject(this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        madapter=new WeatherAdapter(getActivity(),null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_list, container, false);
        ButterKnife.bind(this,view);
        // TODO: 2016/4/21 set up views
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(manager);
        mPresenter.requestWeatherData("");
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
    public void showSelectCity() {

    }

    @Override
    public void showSnackBarMessage(String msg) {
        toastUtil.showToast(msg);
    }

    @Override
    public void showWeatherList(Weather bean) {
        madapter=new WeatherAdapter(getActivity(),bean);
        mRecycleView.setAdapter(madapter);
    }

    @Override
    public void setPresenter(WeatherContract.Presenter presenter) {
        if (presenter != null)
            this.mPresenter = presenter;
    }
}
