package com.martinryberglaude.solsken.model;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.martinryberglaude.solsken.R;
import com.martinryberglaude.solsken.data.Coordinate;
import com.martinryberglaude.solsken.data.DayItem;
import com.martinryberglaude.solsken.data.HourItem;
import com.martinryberglaude.solsken.data.TimeOfDay;
import com.martinryberglaude.solsken.data.WindDirection;
import com.martinryberglaude.solsken.interfaces.MainContract;
import com.martinryberglaude.solsken.networkYR.Location;
import com.martinryberglaude.solsken.networkYR.Symbol;
import com.martinryberglaude.solsken.networkYR.Time;
import com.martinryberglaude.solsken.networkYR.YRRetroWeatherData;
import com.martinryberglaude.solsken.utils_weather.YRWeatherSymbol;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import retrofit2.Response;

public class FormatYRDataAsyncTask extends AsyncTask<Object, Integer, List<DayItem>> implements MainContract.FormatYRWeatherIntractor {


    public MainContract.FormatYRWeatherIntractor.OnFinishedListener delegate = null;
    private SharedPreferences sharedPreferences;

    @Override
    protected List<DayItem> doInBackground(Object... params) {

        Response<YRRetroWeatherData> response = (Response<YRRetroWeatherData>) params[0];
        sharedPreferences = (SharedPreferences) params[1];
        Coordinate coordinate = (Coordinate) params[2];

        List<DayItem> dayList = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        String currentDate;
        if (response.body() == null) {
            return null;
        }

        for (Time time : response.body().getProduct().getTime()) {
            currentDate = time.getDateString();
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
                DateFormat dateFormatDay = new SimpleDateFormat("EEEE", Locale.getDefault());

                DayItem dayItem = new DayItem();
                dayItem.setDayString(capitalizeFirstLetter(dateFormatDay.format(date)));
                dayItem.setDate(date);

                Calendar cal = Calendar.getInstance();
                cal.setTime(dayItem.getDate());
                boolean sameDay = cal.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) &&
                        cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);

                List<HourItem> hourList = new ArrayList<>();
                boolean isFirst = true;
                for (int i = 0; i < response.body().getProduct().getTime().size() - 5; i = i + 5) {
                        Time time2 = response.body().getProduct().getTime().get(i);
                    if (time2.getDateString().equals(currentDate)) {
                        Time timeNext = response.body().getProduct().getTime().get(i + 1);

                        HourItem hourItem = new HourItem();
                        SimpleDateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH':00:00Z'");
                        try {
                            Date hourDate = hourFormat.parse(time2.getTo());
                            hourItem.setDate(hourDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (time2.getLocation().getTemperature() != null) {
                            hourItem.setTemperatureString(getTemperatureString(Double.parseDouble(time2.getLocation().getTemperature().getValue())));
                            temperatureC = (int) Math.round(Double.parseDouble(time2.getLocation().getTemperature().getValue()));
                        }
                        if (time2.getLocation().getWindSpeed() != null) {
                            hourItem.setWindSpeedString(getWindString(Double.parseDouble(time2.getLocation().getWindSpeed().getMps())));
                            windSpeedMS = (int) Math.round(Double.parseDouble(time2.getLocation().getWindSpeed().getMps()));
                        }
                        hourItem.setVisibilityString("N/A");

                        if (time2.getLocation().getWindGust() != null) {
                            hourItem.setGustSpeedString(getWindString(Double.parseDouble(time2.getLocation().getWindGust().getMps())));
                        }
                        if (time2.getLocation().getHumidity() != null) {
                            hourItem.setHumidityString(Math.round(Double.parseDouble(time2.getLocation().getHumidity().getValue())) + "%");
                        }
                        if (timeNext.getLocation().getPrecipitation() != null) {
                            hourItem.setRainAmountString(getPrecipitationString(Double.parseDouble(timeNext.getLocation().getPrecipitation().getValue())));
                            if (timeNext.getLocation().getPrecipitation().getMaxvalue() != null) {
                                hourItem.setRainAmountStringHigh(getPrecipitationString(Double.parseDouble(timeNext.getLocation().getPrecipitation().getMaxvalue())));
                            }
                            if (timeNext.getLocation().getPrecipitation().getMinvalue() != null) {
                                hourItem.setRainAmountStringLow(getPrecipitationString(Double.parseDouble(timeNext.getLocation().getPrecipitation().getMinvalue())));
                            }
                        }
                        if (time2.getLocation().getCloudiness() != null) {
                            hourItem.setCloudCoverString(time2.getLocation().getCloudiness().getPercent() + "%");
                        }
                        if (time2.getLocation().getPressure() != null) {
                            hourItem.setPressureString(getPressureString(Double.parseDouble(time2.getLocation().getPressure().getValue())));
                        }
                        if (time2.getLocation().getWindDirection() != null) {
                            int wdInt = (int) Math.round(Double.parseDouble(time2.getLocation().getWindDirection().getDeg()));
                            @WindDirection.Direction int currentWindDirection = WindDirection.N;
                            int windDrawable = R.drawable.ic_wi_direction_s;
                            if (isBetween(wdInt, 337.5f, 360 ) || isBetween(wdInt, 0f, 22.5f)) {
                                currentWindDirection = WindDirection.N;
                                windDrawable = R.drawable.ic_wi_direction_n;
                            }
                            else if (isBetween(wdInt, 22.5f, 67.5f )) {
                                currentWindDirection = WindDirection.NE;
                                windDrawable = R.drawable.ic_wi_direction_ne;
                            }
                            else if (isBetween(wdInt, 67.5f, 112.5f )) {
                                currentWindDirection = WindDirection.E;
                                windDrawable = R.drawable.ic_wi_direction_e;
                            }
                            else if (isBetween(wdInt, 112.5f, 157.5f )) {
                                currentWindDirection = WindDirection.SE;
                                windDrawable = R.drawable.ic_wi_direction_se;
                            }
                            else if (isBetween(wdInt, 157.5f, 202.5f )) {
                                currentWindDirection = WindDirection.S;
                                windDrawable = R.drawable.ic_wi_direction_s;
                            }
                            else if (isBetween(wdInt, 202.5f, 247.5f )) {
                                currentWindDirection = WindDirection.SW;
                                windDrawable = R.drawable.ic_wi_direction_sw;
                            }
                            else if (isBetween(wdInt, 247.5f, 292.5f )) {
                                currentWindDirection = WindDirection.W;
                                windDrawable = R.drawable.ic_wi_direction_w;
                            }
                            else if (isBetween(wdInt, 292.5f, 337.5f )) {
                                currentWindDirection = WindDirection.NW;
                                windDrawable = R.drawable.ic_wi_direction_nw;
                            }
                            hourItem.setWindDrawable(windDrawable);
                            hourItem.setWindDirection(currentWindDirection);
                        }
                        if (timeNext.getLocation().getSymbol() != null) {
                            String symbol = new YRWeatherSymbol().getSymbolString(Integer.parseInt(timeNext.getLocation().getSymbol().getNumber()));
                            hourItem.setWsymb2String(symbol);
                            Calendar hourCal = Calendar.getInstance();
                            hourCal.setTime(hourItem.getDate());
                            hourItem.setWsymb2Drawable(getWeatherIconToday(timeNext.getLocation().getSymbol(), coordinate, hourCal));
                        }
                        hourItem.setFeelsLikeString(String.valueOf(getFeelsLikeTemperature(temperatureC, humidity, windSpeedMS)) + "°");
                        hourItem.setDayString(capitalizeFirstLetter(dateFormatDay.format(date)));
                        hourItem.setHourString(getClockString(time2.getHourString()));

                        hourList.add(hourItem);

                        if (isFirst) {
                            isFirst = false;
                        }
                    }
                }
                ListIterator<HourItem> iter = hourList.listIterator();
                while(iter.hasNext()){
                    if(iter.next().getTemperatureString() == null){
                        iter.remove();
                    }
                }
                dayItem.setHourList(hourList);
                // Sets sunrise and sunset strings to dayItem
                Calendar dayCal = Calendar.getInstance();
                dayCal.setTime(dayItem.getDate());
                String[] sunriseSunsetString = getSunriseSunsetStrings(coordinate, dayCal);
                dayItem.setSunriseString(sunriseSunsetString[0]);
                dayItem.setSunsetString(sunriseSunsetString[1]);

                // Add dayItem to the list
                dayList.add(dayItem);
            }
        }

        if (Calendar.getInstance().getTime().getTime() - dayList.get(0).getHourList().get(0).getDate().getTime() > 30 * 60 * 1000) {
            dayList.get(0).getHourList().remove(0);
        }

        ListIterator<DayItem> iter = dayList.listIterator();
        while(iter.hasNext()){
            if(iter.next().getHourList().size() == 0){
                iter.remove();
            }
        }
        for (DayItem dayItem : dayList) {
            // Find highest and lowest temperature within the day
            List<Integer> dayTemperaturesSet = new ArrayList<>();
            for (HourItem hour : dayItem.getHourList()) {
                String[] parts = hour.getTemperatureString().split("°");
                dayTemperaturesSet.add(Integer.parseInt(parts[0]));
            }
            Collections.sort(dayTemperaturesSet);
            dayItem.setTemperatureHighString(dayTemperaturesSet.get(dayTemperaturesSet.size() - 1).toString() + "°");
            dayItem.setTemperatureLowString(dayTemperaturesSet.get(0).toString() + "°");

            dayItem.setWsymb2Drawable(dayItem.getHourList().get(dayItem.getHourList().size() / 2).getWsymb2Drawable());
            dayItem.setWsymb2String(dayItem.getHourList().get(dayItem.getHourList().size() / 2).getWsymb2String());
        }
        return dayList;

    }
    // Formatting finished
    @Override
    protected void onPostExecute(List<DayItem> result) {
        super.onPostExecute(result);
        if (result != null) {
            delegate.onFinishedFormatYRDays(result);
        } else {
            delegate.onFailureFormatYRDays();
        }
    }

    private String getClockString(String hourString) {
        String clockFormat = sharedPreferences.getString("hour", "24h");
        String clockString;

        if (clockFormat.equals("12h")) {
            SimpleDateFormat hourFormat = new SimpleDateFormat("H:mm");
            Date date;
            try {
                date = hourFormat.parse(hourString);
                SimpleDateFormat hourFormat12 = new SimpleDateFormat("K:mm a", Locale.getDefault());
                clockString = hourFormat12.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                clockString = hourString;
            }
        } else {
            clockString = hourString;
        }
        return clockString;
    }

    private String getTemperatureString(double temperature) {
        String temperatureString;
        switch (sharedPreferences.getString("temperature", "c")) {
            case "c":
                temperatureString = String.valueOf(Math.round(temperature)) + "°";
                break;
            case "f":
                temperatureString = String.valueOf(Math.round(1.8 * temperature + 32)) + "°";
                break;
            default: temperatureString = String.valueOf(Math.round(temperature)) + "°";
        }
        return temperatureString;
    }

    private String getWindString(double wind) {
        String windSpeedString;
        switch (sharedPreferences.getString("wind", "mps")) {
            case "mps":
                windSpeedString = String.valueOf(Math.round(wind)) + " m/s";
                break;
            case "mph":
                windSpeedString = String.valueOf(Math.round(wind * 2.2369)) + " mph";
                break;
            case "kmh":
                windSpeedString = String.valueOf(Math.round(wind * 3.6)) + " km/h";
                break;
            case "kts":
                windSpeedString = String.valueOf(Math.round(wind * 1.94384449)) + " kts";
                break;
            case "b":
                if (wind < 0.3) windSpeedString = "0";
                else if (wind >= 0.3 && wind < 1.6) windSpeedString = "1";
                else if (wind >= 1.6 && wind < 3.4) windSpeedString = "2";
                else if (wind >= 3.4 && wind < 5.5) windSpeedString = "3";
                else if (wind >= 5.5 && wind < 8) windSpeedString = "4";
                else if (wind >= 8 && wind < 10.8) windSpeedString = "5";
                else if (wind >= 10.8 && wind < 13.9) windSpeedString = "6";
                else if (wind >= 13.9 && wind < 17.2) windSpeedString = "7";
                else if (wind >= 17.2 && wind < 20.8) windSpeedString = "8";
                else if (wind >= 20.8 && wind < 24.5) windSpeedString = "9";
                else if (wind >= 24.5 && wind < 28.5) windSpeedString = "10";
                else if (wind >= 28.5 && wind < 32.7) windSpeedString = "11";
                else if (wind >= 32.7) windSpeedString = "12";
                else windSpeedString = "0";
                break;
            default: windSpeedString = String.valueOf(Math.round(wind)) + " m/s";
        }
        return windSpeedString;
    }

    private String getVisString(double vis) {
        String visString;
        switch (sharedPreferences.getString("vis","km")) {
            case "km":
                visString = String.valueOf(Math.round(vis)) + " km";
                break;
            case "miles":
                visString = String.valueOf(Math.round(vis * 0.621371192)) + " miles";
                break;
            default: visString = String.valueOf(Math.round(vis)) + " km";
        }
        return visString;
    }

    private String getPrecipitationString(double prec) {
        String precString;
        switch (sharedPreferences.getString("rain","mm")) {
            case "mm":
                precString = String.valueOf(prec) + " mm";
                break;
            case "cm":
                precString = String.valueOf(prec / 10) + " cm";
                break;
            case "in":
                DecimalFormat df = new DecimalFormat("#.###");
                df.setRoundingMode(RoundingMode.CEILING);
                precString = String.valueOf(df.format(prec / 25.4)) + "\"";
                break;
            default: precString = String.valueOf(prec) + " mm";
        }
        return precString;
    }

    private String getPressureString(double pressure) {
        String pressureString;
        switch (sharedPreferences.getString("pressure","hpa")) {
            case "hpa":
                pressureString = String.valueOf(Math.round(pressure)) + " hPa";
                break;
            case "bar":
                DecimalFormat decimalFormatBar = new DecimalFormat("#.###");
                decimalFormatBar.setRoundingMode(RoundingMode.CEILING);
                pressureString = String.valueOf(decimalFormatBar.format(pressure / 1000)) + " bar";
                break;
            case "at":
                DecimalFormat decimalFormatAt = new DecimalFormat("#.###");
                decimalFormatAt.setRoundingMode(RoundingMode.CEILING);
                pressureString = String.valueOf(decimalFormatAt.format(pressure * 0.00098692326671601)) + " at";
                break;
            default: pressureString = String.valueOf(Math.round(pressure)) + " hPa";
        }
        return pressureString;
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

    private int getWeatherIconToday(Symbol symbol, Coordinate coordinate, Calendar calendar) {
        TimeOfDay timeOfDay = getTimeOfDay(coordinate, calendar);
        int drawableInt;
        switch (Integer.parseInt(symbol.getNumber())) {
            case 1:
                if (timeOfDay == TimeOfDay.DAY) {
                    drawableInt = (R.drawable.wi_day_sunny);
                } else {
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
                drawableInt = (R.drawable.wi_rain);
                break;
            case 6:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 7:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 8:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 9:
                drawableInt = (R.drawable.wi_rain);
                break;
            case 10:
                drawableInt = (R.drawable.wi_rain);
                break;
            case 11:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 12:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 13:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 14:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 15:
                drawableInt = (R.drawable.wi_fog);
                break;
            case 20:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 21:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 22:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 23:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 24:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 25:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 26:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 27:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 28:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 29:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 30:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 31:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 32:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 33:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 34:
                drawableInt = (R.drawable.wi_thunderstorm);
                break;
            case 40:
                drawableInt = (R.drawable.wi_rain);
                break;
            case 41:
                drawableInt = (R.drawable.wi_rain);
                break;
            case 42:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 43:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 44:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 45:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 46:
                drawableInt = (R.drawable.wi_rain);
                break;
            case 47:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 48:
                drawableInt = (R.drawable.wi_sleet);
                break;
            case 49:
                drawableInt = (R.drawable.wi_snow);
                break;
            case 50:
                drawableInt = (R.drawable.wi_snow);
                break;
            default:
                if (timeOfDay == TimeOfDay.DAY) {
                    drawableInt = (R.drawable.wi_day_sunny);
                } else {
                    drawableInt = (R.drawable.wi_night_clear);
                }
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
    private String[] getSunriseSunsetStrings(Coordinate coordinate, Calendar calendar) {
        Calendar[] calendars = ca.rmen.sunrisesunset.SunriseSunset.getSunriseSunset(calendar, coordinate.getLat(), coordinate.getLon());
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        String sunriseString = getClockString(hourFormat.format(calendars[0].getTime()));
        String sunsetString = getClockString(hourFormat.format(calendars[1].getTime()));

        return new String[] {sunriseString, sunsetString};
    }
    private TimeOfDay getTimeOfDay(Coordinate coordinate, Calendar current) {
        if (!ca.rmen.sunrisesunset.SunriseSunset.isDay(current, coordinate.getLat(), coordinate.getLon())) {
            return TimeOfDay.NIGHT;
        }
        else {
            return TimeOfDay.DAY;
        }
    }
}
