package com.martinryberglaude.solsken.utils_weather;

import com.martinryberglaude.solsken.App;
import com.martinryberglaude.solsken.R;

public class YRWeatherSymbol {

    public String getSymbolString(int id) {
        String symbolString;
        switch (id) {
            case 1:
                symbolString = App.getResourses().getString(R.string.clear);
                break;
            case 2:
                symbolString = App.getResourses().getString(R.string.light_cloudiness);
                break;
            case 3:
                symbolString = App.getResourses().getString(R.string.partly_cloudy);
                break;
            case 4:
                symbolString = App.getResourses().getString(R.string.cloudy);
                break;
            case 5:
                symbolString = App.getResourses().getString(R.string.light_rain);
                break;
            case 6:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 7:
                symbolString = App.getResourses().getString(R.string.sleet);
                break;
            case 8:
                symbolString = App.getResourses().getString(R.string.snow);
                break;
            case 9:
                symbolString = App.getResourses().getString(R.string.light_rain);
                break;
            case 10:
                symbolString = App.getResourses().getString(R.string.rain);
                break;
            case 11:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 12:
                symbolString = App.getResourses().getString(R.string.sleet);
                break;
            case 13:
                symbolString = App.getResourses().getString(R.string.snow);
                break;
            case 14:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 15:
                symbolString = App.getResourses().getString(R.string.fog);
                break;
            case 20:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 21:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 22:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 23:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 24:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 25:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 26:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 27:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 28:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 29:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 30:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 31:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 32:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 33:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 34:
                symbolString = App.getResourses().getString(R.string.thunder_storm);
                break;
            case 40:
                symbolString = App.getResourses().getString(R.string.drizzle);
                break;
            case 41:
                symbolString = App.getResourses().getString(R.string.rain);
                break;
            case 42:
                symbolString = App.getResourses().getString(R.string.light_sleet);
                break;
            case 43:
                symbolString = App.getResourses().getString(R.string.heavy_sleet);
                break;
            case 44:
                symbolString = App.getResourses().getString(R.string.light_snow);
                break;
            case 45:
                symbolString = App.getResourses().getString(R.string.heavy_snow);
                break;
            case 46:
                symbolString = App.getResourses().getString(R.string.drizzle);
                break;
            case 47:
                symbolString = App.getResourses().getString(R.string.light_sleet);
                break;
            case 48:
                symbolString = App.getResourses().getString(R.string.heavy_sleet);
                break;
            case 49:
                symbolString = App.getResourses().getString(R.string.light_snow);
                break;
            case 50:
                symbolString = App.getResourses().getString(R.string.heavy_snow);
                break;
            default:
                symbolString = App.getResourses().getString(R.string.clear);
        }
        return symbolString;
    }
}
