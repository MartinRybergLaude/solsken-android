package com.martinryberglaude.solsken.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DayItem implements Serializable {
    private Date date;
    private String dayString;
    private List<HourItem> hourList;

    private int wsymb2Drawable;
    private String wsymb2String;
    private String temperatureHighString;
    private String temperatureLowString;

    private String sunriseString;
    private String sunsetString;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<HourItem> getHourList() {
        return hourList;
    }

    public void setHourList(List<HourItem> hourList) {
        this.hourList = hourList;
    }

    public String getDayString() {
        return dayString;
    }

    public void setDayString(String dayString) {
        this.dayString = dayString;
    }

    public int getWsymb2Drawable() {
        return wsymb2Drawable;
    }

    public void setWsymb2Drawable(int wsymb2Drawable) {
        this.wsymb2Drawable = wsymb2Drawable;
    }

    public String getWsymb2String() {
        return wsymb2String;
    }

    public void setWsymb2String(String wsymb2String) {
        this.wsymb2String = wsymb2String;
    }

    public String getTemperatureHighString() {
        return temperatureHighString;
    }

    public void setTemperatureHighString(String temperatureHighString) {
        this.temperatureHighString = temperatureHighString;
    }

    public String getTemperatureLowString() {
        return temperatureLowString;
    }

    public void setTemperatureLowString(String temperatureLowString) {
        this.temperatureLowString = temperatureLowString;
    }

    public String getSunriseString() {
        return sunriseString;
    }

    public void setSunriseString(String sunriseString) {
        this.sunriseString = sunriseString;
    }

    public String getSunsetString() {
        return sunsetString;
    }

    public void setSunsetString(String sunsetString) {
        this.sunsetString = sunsetString;
    }

}
