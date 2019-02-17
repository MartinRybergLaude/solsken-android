package com.martinryberglaude.skyfall.model;

import android.os.AsyncTask;
import android.util.Log;

import com.martinryberglaude.skyfall.R;
import com.martinryberglaude.skyfall.data.DayItem;
import com.martinryberglaude.skyfall.data.TimeOfDay;
import com.martinryberglaude.skyfall.data.WindDirection;
import com.martinryberglaude.skyfall.interfaces.MainContract;
import com.martinryberglaude.skyfall.network.RetroParameter;
import com.martinryberglaude.skyfall.network.RetroTimeSeries;
import com.martinryberglaude.skyfall.network.RetroWeatherData;
import com.martinryberglaude.skyfall.utils_smhi.SMHIWeatherSymbol;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import retrofit2.Response;

public class FormatDaysAsyncTaskModel extends AsyncTask<Object, Integer, List<DayItem>> implements MainContract.FormatDayWeatherIntractor {

    public MainContract.FormatDayWeatherIntractor.OnFinishedListener delegate = null;
    @Override
    protected List<DayItem> doInBackground(Object... params) {

        Response<RetroWeatherData> response = (Response<RetroWeatherData>) params[0];
        TimeOfDay timeOfDay = (TimeOfDay) params[1];

        List<DayItem> dayList = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        String currentDate;
        String targetHour = "12:00:00Z";

        for (RetroTimeSeries timeSeries : response.body().getTimeSeries()) {
            currentDate = timeSeries.getDateString();
            int temperatureC = 0;
            int windSpeedMS = 0;
            int humidity = 0;
            if (!dateList.contains(currentDate)) {
                dateList.add(currentDate);

                DateFormat format = new SimpleDateFormat("yy-MM-dd", Locale.FRENCH);
                Date date = null;
                try {
                    date = format.parse(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat dateFormatDay = new SimpleDateFormat("EEEE", Locale.forLanguageTag("sv"));

                DayItem dayItem = new DayItem();
                dayItem.setDayString(capitalizeFirstLetter(dateFormatDay.format(date)));
                dayItem.setDate(date);

                Calendar cal = Calendar.getInstance();
                cal.setTime(dayItem.getDate());
                boolean sameDay = cal.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) &&
                        cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);
                for (RetroTimeSeries timeSeries2 : response.body().getTimeSeries()) {
                    if (timeSeries2.getDateString().equals(currentDate) && timeSeries2.getHourString().equals(targetHour)) {
                        for (RetroParameter parameter : timeSeries2.getParameters()) {
                            if (parameter.getName().equals("t")) {
                                dayItem.setTemperatureString(String.valueOf(Math.round(parameter.getValues().get(0))) + "°");
                                temperatureC = (int) Math.round(parameter.getValues().get(0));
                            }
                            if (parameter.getName().equals("ws")) {
                                dayItem.setWindSpeedString(String.valueOf(Math.round(parameter.getValues().get(0))) + " m/s");
                                windSpeedMS = (int) Math.round(parameter.getValues().get(0));
                            }
                            if (parameter.getName().equals("vis")) {
                                dayItem.setVisbilityString(String.valueOf(Math.round(parameter.getValues().get(0))) + " km");
                            }
                            if (parameter.getName().equals("gust")) {
                                dayItem.setGustSpeedString(String.valueOf(Math.round(parameter.getValues().get(0))) + " m/s");
                            }
                            if (parameter.getName().equals("r")) {
                                dayItem.setHumidityString(String.valueOf(Math.round(parameter.getValues().get(0))) + "%");
                                humidity = (int)  Math.round(parameter.getValues().get(0));
                            }
                            if (parameter.getName().equals("pmean")) {
                                dayItem.setRainAmountString(String.valueOf(Math.round(parameter.getValues().get(0))) + " mm/h");
                            }
                            if (parameter.getName().equals("tcc_mean")) {
                                dayItem.setCloudCoverString(String.valueOf(Math.round((parameter.getValues().get(0) / 8) * 100)) + "%");
                            }
                            if (parameter.getName().equals("msl")) {
                                dayItem.setPressureString(String.valueOf(Math.round(parameter.getValues().get(0)) + " hpa"));
                            }
                            if (parameter.getName().equals("wd")) {
                                int wdInt = (int) Math.round(parameter.getValues().get(0));
                                 @WindDirection.Direction int currentWindDirection = WindDirection.N;
                                if (isBetween(wdInt, 337.5f, 360 ) || isBetween(wdInt, 0f, 22.5f)) currentWindDirection = WindDirection.N;
                                else if (isBetween(wdInt, 22.5f, 67.5f )) currentWindDirection = WindDirection.NE;
                                else if (isBetween(wdInt, 67.5f, 112.5f )) currentWindDirection = WindDirection.E;
                                else if (isBetween(wdInt, 112.5f, 157.5f )) currentWindDirection = WindDirection.SE;
                                else if (isBetween(wdInt, 157.5f, 202.5f )) currentWindDirection = WindDirection.S;
                                else if (isBetween(wdInt, 202.5f, 247.5f )) currentWindDirection = WindDirection.SW;
                                else if (isBetween(wdInt, 247.5f, 292.5f )) currentWindDirection = WindDirection.W;
                                else if (isBetween(wdInt, 292.5f, 337.5f )) currentWindDirection = WindDirection.NW;

                                dayItem.setWindDirection(currentWindDirection);
                            }

                            if (parameter.getName().equals("Wsymb2")) {
                                String symbol = new SMHIWeatherSymbol().getSymbolList().get((int)Math.round(parameter.getValues().get(0)) - 1);
                                dayItem.setWsymb2String(symbol);
                                if (sameDay) {
                                    dayItem.setWsymb2Drawable(getWeatherIconToday(parameter, timeOfDay));
                                } else {
                                    dayItem.setWsymb2Drawable(getWeatherIcon(parameter));
                                }
                            }
                        }
                    }
                }
                dayItem.setFeelsLikeString(String.valueOf(getFeelsLikeTemperature(temperatureC, humidity, windSpeedMS)) + "°");
                dayList.add(dayItem);
            }
        }
        ListIterator<DayItem> iter = dayList.listIterator();
        while(iter.hasNext()){
            if(iter.next().getTemperatureString() == null){
                iter.remove();
            }
        }
        return dayList;
    }

    // Formatting finished
    @Override
    protected void onPostExecute(List<DayItem> result) {
        super.onPostExecute(result);
        delegate.onFinishedFormatDays(result);
    }

    private static boolean isBetween(int x, float lower, float upper) {
        return lower <= x && x <= upper;
    }
    private String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    private int getWeatherIconToday(RetroParameter parameter, TimeOfDay timeOfDay) {

        int symbolInt = (int) Math.round(parameter.getValues().get(0));
        int drawableInt;
        switch (symbolInt) {
            case 1:
                if (timeOfDay == TimeOfDay.DAY) {
                    drawableInt = (R.drawable.wi_day_sunny);
                }else {
                    drawableInt = (R.drawable.wi_night_clear);
                }
                break;
            case 2:
                if (timeOfDay == TimeOfDay.DAY) {
                    drawableInt = (R.drawable.wi_day_sunny);
                } else {
                    drawableInt = (R.drawable.wi_night_clear);
                }
                break;
            case 3:
                if (timeOfDay == TimeOfDay.DAY) {
                    drawableInt = (R.drawable.wi_day_cloudy);
                } else {
                    drawableInt = (R.drawable.wi_night_alt_cloudy);
                }
                break;
            case 4:
                if (timeOfDay == TimeOfDay.DAY) {
                    drawableInt = (R.drawable.wi_day_cloudy);
                } else {
                    drawableInt = (R.drawable.wi_night_alt_cloudy);
                }
                break;
            case 5:
                drawableInt = (R.drawable.wi_cloudy);
                break;
            case 6:
                if (timeOfDay == TimeOfDay.DAY) {
                    drawableInt = (R.drawable.wi_cloudy);
                } else {
                    drawableInt = (R.drawable.wi_night_alt_cloudy);
                }
                break;
            case 7:
                drawableInt = (R.drawable.wi_fog);
                break;
            case 8:
                drawableInt = (R.drawable.wi_showers);
                break;
            case 9:
                drawableInt = (R.drawable.wi_showers);
                break;
            case 10:
                drawableInt = (R.drawable.wi_showers);
                break;
            case 11:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 12:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 13:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 14:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 15:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 16:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 17:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 18:
                drawableInt = (R.drawable.wi_rain);
                break;
            case 19:
                drawableInt = (R.drawable.wi_rain);
                break;
            case 20:
                drawableInt = (R.drawable.wi_rain);
                break;
            case 21:
                drawableInt = (R.drawable.wi_lightning);
                break;
            case 22:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 23:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 24:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 25:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 26:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 27:
                drawableInt = (R.drawable.wi_snow);
                break;
            default:
                drawableInt = (R.drawable.wi_cloudy);
        }
        return drawableInt;
    }

    private int getWeatherIcon(RetroParameter parameter) {

        int symbolInt = (int) Math.round(parameter.getValues().get(0));
        int drawableInt;
        switch (symbolInt) {
            case 1:
                drawableInt = (R.drawable.wi_day_sunny);
                break;
            case 2:
                drawableInt = (R.drawable.wi_day_sunny);
                break;
            case 3:
                drawableInt = (R.drawable.wi_day_cloudy);
                break;
            case 4:
                drawableInt = (R.drawable.wi_day_cloudy);
                break;
            case 5:
                drawableInt = (R.drawable.wi_cloudy);
                break;
            case 6:
                drawableInt = (R.drawable.wi_cloudy);
                break;
            case 7:
                drawableInt = (R.drawable.wi_fog);
                break;
            case 8:
                drawableInt = (R.drawable.wi_showers);
                break;
            case 9:
                drawableInt = (R.drawable.wi_showers);
                break;
            case 10:
                drawableInt = (R.drawable.wi_showers);
                break;
            case 11:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 12:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 13:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 14:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 15:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 16:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 17:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 18:
                drawableInt = (R.drawable.wi_rain);
                break;
            case 19:
                drawableInt = (R.drawable.wi_rain);
                break;
            case 20:
                drawableInt = (R.drawable.wi_rain);
                break;
            case 21:
                drawableInt = (R.drawable.wi_lightning);
                break;
            case 22:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 23:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 24:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 25:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 26:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 27:
                drawableInt = (R.drawable.wi_snow);
                break;
            default:
                drawableInt = (R.drawable.wi_cloudy);
        }
        return drawableInt;
    }

    private int getFeelsLikeTemperature(int temperatureC, int humidity, int windspeed) {
        if (temperatureC >= 27 && humidity >= 40) {
            int tF = (int) Math.round((1.8 * temperatureC) + 32);
            double feelsLikeDewF = -42.379 + (2.04901523 * tF) + (10.14333127 * humidity)
                    - (0.22475541 * tF * humidity) - (6.83783 * Math.pow(10, -3) * tF * tF)
                    - (5.481717 * Math.pow(10, -2) * humidity * humidity) + (1.22874 * Math.pow(10,-3) * tF * tF * humidity)
                    + (8.5282 * Math.pow(10,-4) * tF * humidity * humidity) - (1.99 * Math.pow(10, -6) * tF * tF * humidity * humidity);
            double feelsLikeDewC = (feelsLikeDewF -32) / 1.8;
            return (int) Math.round(feelsLikeDewC);
        } else if (temperatureC <= 10 && windspeed >= 1.34) {
            double wKM = windspeed * 3.6;
            double feelsLikeWindC = 13.12 + (0.6215 * temperatureC) - (11.37 * Math.pow(wKM, 0.16)) + (0.3965 * temperatureC * Math.pow(wKM, 0.16));
            return (int) Math.round(feelsLikeWindC);
        } else {
            return temperatureC;
        }
    }
}

