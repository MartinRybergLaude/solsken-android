package com.martinryberglaude.solsken.utils_weather;

import com.martinryberglaude.solsken.App;
import com.martinryberglaude.solsken.R;

import java.util.ArrayList;
import java.util.List;

public class SMHIWeatherSymbol {
    public List<String> getSymbolList() {
        return symbolList;
    }
    private List<String> symbolList = new ArrayList<String>() {{
        add(App.getResourses().getString(R.string.clear));
        add(App.getResourses().getString(R.string.almost_clear));
        add(App.getResourses().getString(R.string.varying_cloudiness));
        add(App.getResourses().getString(R.string.half_clear));
        add(App.getResourses().getString(R.string.cloudy));
        add(App.getResourses().getString(R.string.overcast));
        add(App.getResourses().getString(R.string.fog));
        add(App.getResourses().getString(R.string.light_rain_showers));
        add(App.getResourses().getString(R.string.moderate_rain_showers));
        add(App.getResourses().getString(R.string.heavy_rain_showers));
        add(App.getResourses().getString(R.string.thunder_storm));
        add(App.getResourses().getString(R.string.light_sleet_showers));
        add(App.getResourses().getString(R.string.moderate_sleet_showers));
        add(App.getResourses().getString(R.string.heavy_sleet_showers));
        add(App.getResourses().getString(R.string.light_snow_showers));
        add(App.getResourses().getString(R.string.moderate_sleet_showers));
        add(App.getResourses().getString(R.string.heavy_sleet_showers));
        add(App.getResourses().getString(R.string.light_rain));
        add(App.getResourses().getString(R.string.moderate_rain));
        add(App.getResourses().getString(R.string.heavy_rain));
        add(App.getResourses().getString(R.string.thunder));
        add(App.getResourses().getString(R.string.light_sleet));
        add(App.getResourses().getString(R.string.moderate_sleet));
        add(App.getResourses().getString(R.string.heavy_sleet));
        add(App.getResourses().getString(R.string.light_snow));
        add(App.getResourses().getString(R.string.moderate_snow));
        add(App.getResourses().getString(R.string.heavy_snow));
    }};

}
