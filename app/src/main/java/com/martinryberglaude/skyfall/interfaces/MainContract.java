package com.martinryberglaude.skyfall.interfaces;

import android.content.Context;

import com.martinryberglaude.skyfall.data.Coordinate;
import com.martinryberglaude.skyfall.data.ListItem;
import com.martinryberglaude.skyfall.data.TimeOfDay;
import com.martinryberglaude.skyfall.network.RetroWeatherData;

import java.util.List;

import retrofit2.Response;

public interface MainContract {

    interface  View {
        void showProgressBar(boolean b);
        void setToolbarTitle(String toolbarTitle);
        void initWeatherUI(List<ListItem> itemList);
        void updateWeatherUI(List<ListItem> itemList);
        void setColorTheme(TimeOfDay timeOfDay);
        void showToast(String message);
        String requestAdressString(Coordinate coordinate);
        void showRefresh(boolean b);
        Coordinate getCurrentCoordinate();
        void updateLocationAndUI();
    }

    interface Presenter {
        void updateLocationAndUI();
        TimeOfDay getTimeOfDay();
        void loadColorTheme();
        void requestWeatherData();
        void setAdressString();
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

    interface FormatWeatherIntractor {
        interface OnFinishedListener {
            void onFinishedFormatData(List<ListItem> itemList);
            void onFailureFormatData();
        }
    }
}
