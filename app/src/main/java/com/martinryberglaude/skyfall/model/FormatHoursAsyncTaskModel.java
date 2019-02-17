package com.martinryberglaude.skyfall.model;

import android.os.AsyncTask;

import com.martinryberglaude.skyfall.data.EventItem;
import com.martinryberglaude.skyfall.data.HeaderItem;
import com.martinryberglaude.skyfall.data.ListItem;
import com.martinryberglaude.skyfall.utils_smhi.SMHIWeatherSymbol;
import com.martinryberglaude.skyfall.interfaces.MainContract;
import com.martinryberglaude.skyfall.network.RetroParameter;
import com.martinryberglaude.skyfall.network.RetroTimeSeries;
import com.martinryberglaude.skyfall.network.RetroWeatherData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

public class FormatHoursAsyncTaskModel extends AsyncTask<Response<RetroWeatherData>, Integer, List<ListItem>> implements MainContract.FormatHourWeatherIntractor {
    public MainContract.FormatHourWeatherIntractor.OnFinishedListener delegate = null;
    // Background thread
    @Override
    protected List<ListItem> doInBackground(Response<RetroWeatherData>... params) {

        Response<RetroWeatherData> response = params[0];

        List<ListItem> itemList = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        String currentDate;

        for (RetroTimeSeries timeSeries : response.body().getTimeSeries()) {
            currentDate = timeSeries.getDateString();
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
                DateFormat dateFormatDate = new SimpleDateFormat("d MMMM yyyy", Locale.forLanguageTag("sv"));

                HeaderItem header = new HeaderItem();
                header.setDateString(dateFormatDate.format(date));
                header.setDayString(dateFormatDay.format(date));
                header.setDate(date);
                itemList.add(header);

                for (RetroTimeSeries timeSeries2 : response.body().getTimeSeries()) {
                    if (timeSeries2.getDateString().equals(currentDate)) {
                        EventItem item = new EventItem();
                        // Find parameters
                        for (RetroParameter parameter : timeSeries2.getParameters()) {
                            if (parameter.getName().equals("t")) {
                                item.setTemperatureString(String.valueOf(Math.round(parameter.getValues().get(0))) + "°");
                            }
                            if (parameter.getName().equals("ws")) {
                                item.setWindSpeedString(String.valueOf(Math.round(parameter.getValues().get(0))) + " m/s");
                            }
                            if (parameter.getName().equals("Wsymb2")) {
                                String symbol = new SMHIWeatherSymbol().getSymbolList().get((int)Math.round(timeSeries2.getParameters().get(18).getValues().get(0)) - 1);
                                item.setWsymb2String(symbol);
                            }
                        }
                        item.setHourString(timeSeries2.getHourString().substring(0,5));
                        itemList.add(item);
                    }
                }
            }
        }
        return itemList;
    }

    // Formatting finished
    @Override
    protected void onPostExecute(List<ListItem> result) {
        super.onPostExecute(result);
        delegate.onFinishedFormatHours(result);
    }
}
