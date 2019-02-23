package com.martinryberglaude.skyfall.interfaces;

import android.content.Context;
import android.content.SharedPreferences;

import com.martinryberglaude.skyfall.data.Coordinate;
import com.martinryberglaude.skyfall.data.DayItem;
import com.martinryberglaude.skyfall.data.HourItem;
import com.martinryberglaude.skyfall.data.ListItem;
import com.martinryberglaude.skyfall.data.TimeOfDay;
import com.martinryberglaude.skyfall.network.RetroWeatherData;

import java.util.List;

import retrofit2.Response;

public interface MainContract {

    interface  View {
        void updateWeatherUI(List<DayItem> itemList, String city, boolean initRecyclerview);
        void setColorTheme(TimeOfDay timeOfDay);
        void showToast(String message);
        String requestAdressString(Coordinate coordinate);
        void showRefresh(boolean b);
        Coordinate getCurrentCoordinate();
        void updateLocationAndUI();
        SharedPreferences getSharedPreferences();
    }

    interface Presenter {
        void updateLocationAndUI();
        TimeOfDay getTimeOfDay();
        void loadColorTheme();
        void requestWeatherData();
    }

    interface RequestWeatherIntractor {
        interface OnFinishedListerner {
            void onFinishedRetrieveData(Response<RetroWeatherData> response);
            void onFailureRetrieveData(Throwable t);
        }
        void getWeatherData(OnFinishedListerner onFinishedListerner, Coordinate coordinate);
    }
    interface RequestLocationIntractor {
        interface OnFinishedListerner {
            void onFinishedRetrieveLocation(Coordinate coordinate);
            void onFailureRetrieveLocationn();
        }
        void getLocation(OnFinishedListerner onFinishedListerner, Context context);
    }

    interface FormatDayWeatherIntractor {
        interface OnFinishedListener {
            void onFinishedFormatDays(List<DayItem> dayList);
            void onFailureFormatDays();
        }
    }
    interface DayItemClickListener {
        void onItemClick(DayItem dayItem);
    }
    interface HourItemOnClickListener {
        void onItemClick (HourItem hourItem);
    }

}
