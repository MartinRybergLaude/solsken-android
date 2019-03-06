package com.martinryberglaude.solsken.presenter;

import com.martinryberglaude.solsken.data.DayItem;
import com.martinryberglaude.solsken.interfaces.MainContract;
import com.martinryberglaude.solsken.data.TimeOfDay;
import com.martinryberglaude.solsken.model.FormatSMHIDataAsyncTask;
import com.martinryberglaude.solsken.model.MainModel;
import com.martinryberglaude.solsken.network.SMHIRetroWeatherData;

import java.util.List;

import retrofit2.Response;

public class MainPresenter implements MainContract.Presenter, MainContract.RequestWeatherIntractor.OnFinishedListerner,
        MainContract.FormatDayWeatherIntractor.OnFinishedListener {

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
    public void onFinishedRetrieveData(Response<SMHIRetroWeatherData> response) {
      FormatSMHIDataAsyncTask formatAsyncTask = new FormatSMHIDataAsyncTask();
      formatAsyncTask.delegate = this;
      formatAsyncTask.execute(response, model.getTimeOfDay(view.getCurrentCoordinate()), view.getSharedPreferences(), view.getCurrentCoordinate());
    }

    @Override
    public void onFailureRetrieveData(Throwable t) {
        view.showRefresh(false);
        view.showToast(weatherErrorString);
    }

    @Override
    public void onFinishedFormatDays(List<DayItem> dayList) {
        view.showRefresh(false);
        if (isStart) view.updateWeatherUI(dayList, view.requestAdressString(view.getCurrentCoordinate()), true);
        else view.updateWeatherUI(dayList, view.requestAdressString(view.getCurrentCoordinate()), false);
        isStart = false;
    }

    @Override
    public void onFailureFormatDays() {
        view.showRefresh(false);
        view.showLocationError();
        view.showToast(weatherErrorString);
    }
}
