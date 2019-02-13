package com.martinryberglaude.skyfall.data;

import com.martinryberglaude.skyfall.network.RetroTimeSeries;

public class EventItem extends ListItem {

    private String temperatureString;
    private String hourString;
    private String windSpeedString;
    private String wsymb2String;

    public String getTemperatureString() {
        return temperatureString;
    }

    public void setTemperatureString(String temperatureString) {
        this.temperatureString = temperatureString;
    }

    public String getHourString() {
        return hourString;
    }

    public void setHourString(String hourString) {
        this.hourString = hourString;
    }

    public String getWindSpeedString() {
        return windSpeedString;
    }

    public void setWindSpeedString(String windSpeedString) {
        this.windSpeedString = windSpeedString;
    }

    public String getWsymb2String() {
        return wsymb2String;
    }

    public void setWsymb2String(String wsymb2String) {
        this.wsymb2String = wsymb2String;
    }

    @Override
    public int getType() {
        return TYPE_EVENT;
    }

}
