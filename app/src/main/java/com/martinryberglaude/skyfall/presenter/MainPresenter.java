package com.martinryberglaude.skyfall.presenter;

import com.martinryberglaude.skyfall.interfaces.MainContract;
import com.martinryberglaude.skyfall.data.TimeOfDay;
import com.martinryberglaude.skyfall.data.ListItem;
import com.martinryberglaude.skyfall.model.FormatDataAsyncTaskModel;
import com.martinryberglaude.skyfall.model.MainModel;
import com.martinryberglaude.skyfall.network.RetroWeatherData;

import java.util.List;

import retrofit2.Response;

public class MainPresenter implements MainContract.Presenter, MainContract.RequestWeatherIntractor.OnFinishedListerner,
        MainContract.FormatWeatherIntractor.OnFinishedListener {

    private MainContract.View view;
    private MainContract.RequestWeatherIntractor getWeatherIntractor;
    private MainModel model;
    private String weatherErrorString;

    private boolean isStart = true;

    public MainPresenter(MainContract.View view, MainContract.RequestWeatherIntractor getWeatherIntractor, String weatherErrorString) {
        this.view = view;
        this.getWeatherIntractor = getWeatherIntractor;
        this.weatherErrorString = weatherErrorString;
        model = new MainModel();
    }

    @Override
    public void updateLocationAndUI() {
        view.showRefresh(true);
        view.updateLocationAndUI();
    }

    @Override
    public TimeOfDay getTimeOfDay() {
        return model.getTimeOfDay(view.getCurrentCoordinate());
    }

    @Override
    public void loadColorTheme() {
        view.setColorTheme(model.getTimeOfDay(view.getCurrentCoordinate()));
    }

    @Override
    public void requestWeatherData() {
        getWeatherIntractor.getWeatherData(this, view.getCurrentCoordinate());
    }

    @Override
    public void setAdressString() {
        view.setToolbarTitle(view.requestAdressString(view.getCurrentCoordinate()));
    }

    @Override
    public void onFinishedRetrieveData(Response<RetroWeatherData> response) {
        FormatDataAsyncTaskModel formatAsyncTask = new FormatDataAsyncTaskModel();
        formatAsyncTask.delegate = this;
        formatAsyncTask.execute(response);
    }

    @Override
    public void onFailureRetrieveData(Throwable t) {
        view.showRefresh(false);
        view.showToast(weatherErrorString);
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
        view.showToast(weatherErrorString);
    }
}
