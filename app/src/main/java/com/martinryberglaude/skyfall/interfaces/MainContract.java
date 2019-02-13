package com.martinryberglaude.skyfall.interfaces;

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
        Coordinate requestCoordinate();
        String requestAdressString(Coordinate coordinate);
        void showRefresh(boolean b);
    }

    interface Presenter {
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

    interface FormatWeatherIntractor {
        interface OnFinishedListener {
            void onFinishedFormatData(List<ListItem> itemList);
            void onFailureFormatData();
        }
    }
}
