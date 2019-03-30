package com.martinryberglaude.solsken.interfaces;

import android.content.Context;
import android.content.SharedPreferences;

import com.martinryberglaude.solsken.data.Coordinate;
import com.martinryberglaude.solsken.data.DayItem;
import com.martinryberglaude.solsken.data.TimeOfDay;
import com.martinryberglaude.solsken.database.Locations;
import com.martinryberglaude.solsken.database.Weathers;
import com.martinryberglaude.solsken.networkSMHI.SMHIRetroWeatherData;
import com.martinryberglaude.solsken.networkYR.YRRetroWeatherData;

import java.util.List;

import retrofit2.Response;

public interface MainContract {

    interface  View {
        void updateWeatherUI(List<DayItem> itemList, String city, boolean initRecyclerview);
        void setColorTheme();
        void showToast(String message);
        void showLocationError();
        String requestAdressString(Coordinate coordinate);
        void showRefresh(boolean b);
        Coordinate getCurrentCoordinate();
        void updateLocationAndUI();
        SharedPreferences getSharedPreferences();
        void setIsStart(boolean b);
        void resetWeathers();
        void addWeather(List<DayItem> dayList);
    }

    interface Presenter {
        void updateLocationAndUI();
        TimeOfDay getTimeOfDay();
        void loadColorTheme();
        void requestWeatherData(boolean smhi);
    }

    interface RequestSMHIWeatherIntractor {
        interface OnFinishedListerner {
            void onFinishedRetrieveSMHIData(Response<SMHIRetroWeatherData> response);
            void onFailureRetrieveSMHIData(Throwable t);
        }
        void getSMHIWeatherData(OnFinishedListerner onFinishedListerner, Coordinate coordinate);
    }

    interface RequestYRWeatherIntractor {
        interface OnFinishedListerner {
            void onFinishedRetrieveYRData(Response<YRRetroWeatherData> response);
            void onFailureRetrieveYRData(Throwable t);
        }
        void getYRWeatherData(OnFinishedListerner onFinishedListerner, Coordinate coordinate);
    }

    interface RequestLocationIntractor {
        interface OnFinishedListerner {
            void onFinishedRetrieveLocation(Coordinate coordinate);
            void onFailureRetrieveLocationn();
        }
        void getLocation(OnFinishedListerner onFinishedListerner, Context context);
    }

    interface FormatSMHIWeatherIntractor {
        interface OnFinishedListener {
            void onFinishedFormatSMHIDays(List<DayItem> dayList);
            void onFailureFormatSMHIDays();
        }
    }

    interface FormatYRWeatherIntractor {
        interface OnFinishedListener {
            void onFinishedFormatYRDays(List<DayItem> dayList);
            void onFailureFormatYRDays();
        }
    }
    interface RetrieveDatabaseLocationsIntractor {
        interface OnFinishedListener {
            void onFinishedRetrieveDatabaseLocations(List<Locations> locationList);
            void onFailureRetrieveDatabaseLocations();
        }
    }
    interface RemoveDatabaseLocationsIntractor {
        interface OnFinishedListener {
            void onFinishedRemoveDatabaseLocations(long identifier);
            void onFailureRemoveDatabaseLocations();
        }
    }
    interface RetrieveDatabaseWeathersIntractor {
        interface OnFinishedListener {
            void onFinishedRetrieveDatabaseWeathers(Weathers result);
            void onFailureRetrieveDatabaseWeathers();
        }
    }
    interface RemoveDatabaseWeathersIntractor {
        interface OnFinishedListener {
            void onFinishedRemoveDatabaseWeathers(String identifier);
            void onFailureRemoveDatabaseWeathers();
        }
    }
    interface DayItemClickListener {
        void onItemClick(DayItem dayItem);
    }

}
