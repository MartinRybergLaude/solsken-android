package com.martinryberglaude.solsken.presenter;

import android.content.Context;

import com.martinryberglaude.solsken.data.DayItem;
import com.martinryberglaude.solsken.interfaces.MainContract;
import com.martinryberglaude.solsken.model.FormatSMHIDataAsyncTask;
import com.martinryberglaude.solsken.model.FormatYRDataAsyncTask;
import com.martinryberglaude.solsken.networkSMHI.SMHIRetroWeatherData;
import com.martinryberglaude.solsken.networkYR.YRRetroWeatherData;

import java.util.List;

import retrofit2.Response;

public class MainPresenter implements MainContract.Presenter, MainContract.RequestSMHIWeatherIntractor.OnFinishedListerner,
        MainContract.FormatSMHIWeatherIntractor.OnFinishedListener, MainContract.RequestYRWeatherIntractor.OnFinishedListerner, MainContract.FormatYRWeatherIntractor.OnFinishedListener {

    private MainContract.View view;
    private MainContract.RequestSMHIWeatherIntractor getSMHIWeatherIntractor;
    private MainContract.RequestYRWeatherIntractor getYRWeatherIntractor;
    private String weatherErrorString;

    private boolean isStart = true;

    public MainPresenter(MainContract.View view, MainContract.RequestSMHIWeatherIntractor getSMHIWeatherIntractor, MainContract.RequestYRWeatherIntractor getYRWeatherIntractor, String weatherErrorString) {
        this.view = view;
        this.getSMHIWeatherIntractor = getSMHIWeatherIntractor;
        this.getYRWeatherIntractor = getYRWeatherIntractor;
        this.weatherErrorString = weatherErrorString;
    }

    @Override
    public void updateLocationAndUI() {
        view.showRefresh(true);
        view.updateLocationAndUI();
    }

    @Override
    public void loadColorTheme() {
        view.setColorTheme();
    }

    @Override
    public void requestWeatherData(boolean smhi) {
        if (smhi) {
            getSMHIWeatherIntractor.getSMHIWeatherData(this, view.getCurrentCoordinate());
        } else {
            getYRWeatherIntractor.getYRWeatherData(this, view.getCurrentCoordinate());
        }
    }

    @Override
    public void onFinishedRetrieveSMHIData(Response<SMHIRetroWeatherData> response) {
      FormatSMHIDataAsyncTask formatAsyncTask = new FormatSMHIDataAsyncTask();
      formatAsyncTask.delegate = this;
      formatAsyncTask.execute(response, view.getSharedPreferences(), view.getCurrentCoordinate());
    }

    @Override
    public void onFailureRetrieveSMHIData(Throwable t) {
        view.showRefresh(false);
        view.showToast(weatherErrorString);
    }

    @Override
    public void onFinishedFormatSMHIDays(List<DayItem> dayList) {
        view.addWeather(dayList);
        view.showRefresh(false);
        if (isStart) view.updateWeatherUI(dayList, view.requestAdressString(view.getCurrentCoordinate()), true);
        else view.updateWeatherUI(dayList, view.requestAdressString(view.getCurrentCoordinate()), false);
        isStart = false;
        view.setIsStart(false);
    }

    @Override
    public void onFailureFormatSMHIDays() {
        view.showRefresh(false);
        view.showLocationError();
        view.showToast(weatherErrorString);
    }

    @Override
    public void onFinishedRetrieveYRData(Response<YRRetroWeatherData> response) {
        FormatYRDataAsyncTask formatAsyncTask = new FormatYRDataAsyncTask();
        formatAsyncTask.delegate = this;
        formatAsyncTask.execute(response, view.getSharedPreferences(), view.getCurrentCoordinate());
    }

    @Override
    public void onFailureRetrieveYRData(Throwable t) {
        view.showRefresh(false);
        view.showLocationError();
        view.showToast(weatherErrorString);
    }

    @Override
    public void onFinishedFormatYRDays(List<DayItem> dayList) {
        view.addWeather(dayList);
        view.showRefresh(false);
        if (isStart) view.updateWeatherUI(dayList, view.requestAdressString(view.getCurrentCoordinate()), true);
        else view.updateWeatherUI(dayList, view.requestAdressString(view.getCurrentCoordinate()), false);
        isStart = false;
    }

    @Override
    public void onFailureFormatYRDays() {
        view.showRefresh(false);
        view.showLocationError();
        view.showToast(weatherErrorString);
    }
}
