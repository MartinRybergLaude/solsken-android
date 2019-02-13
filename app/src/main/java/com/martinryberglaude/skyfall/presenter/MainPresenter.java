package com.martinryberglaude.skyfall.presenter;


import com.martinryberglaude.skyfall.interfaces.MainContract;
import com.martinryberglaude.skyfall.data.TimeOfDay;
import com.martinryberglaude.skyfall.data.ListItem;
import com.martinryberglaude.skyfall.model.FormatDataAsyncTask;
import com.martinryberglaude.skyfall.model.MainModel;
import com.martinryberglaude.skyfall.network.RetroWeatherData;

import java.util.List;

import retrofit2.Response;

public class MainPresenter implements MainContract.Presenter, MainContract.RequestWeatherIntractor.OnFinishedListerner,
        MainContract.FormatWeatherIntractor.OnFinishedListener {

    private MainContract.View view;
    private MainContract.RequestWeatherIntractor getWeatherIntractor;
    private MainModel model;

    private boolean isStart = true;

    public MainPresenter(MainContract.View view, MainContract.RequestWeatherIntractor getWeatherList) {
        this.view = view;
        this.getWeatherIntractor = getWeatherList;
        model = new MainModel();
    }
    @Override
    public TimeOfDay getTimeOfDay() {
        return model.getTimeOfDay(view.requestCoordinate());
    }

    @Override
    public void loadColorTheme() {
        view.setColorTheme(model.getTimeOfDay(view.requestCoordinate()));
    }

    @Override
    public void requestWeatherData() {
        view.showRefresh(true);
        getWeatherIntractor.getWeatherData(this, view.requestCoordinate());
    }

    @Override
    public void setAdressString() {
        view.setToolbarTitle(view.requestAdressString(view.requestCoordinate()));
    }

    @Override
    public void onFinishedRetrieveData(Response<RetroWeatherData> response) {
        FormatDataAsyncTask formatAsyncTask = new FormatDataAsyncTask();
        formatAsyncTask.delegate = this;
        formatAsyncTask.execute(response);
    }

    @Override
    public void onFailureRetrieveData(Throwable t) {
        view.showRefresh(false);
        view.showToast("Vädret kunde inte laddas in!");
    }

    @Override
    public void onFinishedFormatData(List<ListItem> itemList) {
        view.showRefresh(false);
        setAdressString();
        if (isStart) view.initWeatherUI(itemList);
        else view.updateWeatherUI(itemList);
        isStart = false;
    }

    @Override
    public void onFailureFormatData() {
        view.showRefresh(false);
        view.showToast("Vädret kunde inte laddas in!");
    }
}
