package com.martinryberglaude.solsken.data;

import java.io.Serializable;
import java.util.Date;

public class HourItem implements Serializable {
    private String temperatureString;
    private String dayString;
    private String hourString;
    private String windSpeedString;
    @WindDirection.Direction int windDirection;
    private String windDirectionString;
    private int windDrawable;
    private String wsymb2String;
    private String pressureString;
    private String rainAmountString;
    private String visibilityString;
    private String humidityString;
    private String gustSpeedString;
    private String cloudCoverString;
    private String feelsLikeString;
    private Date date;
    private int wsymb2Drawable;
    private boolean isExpanded;

    public @WindDirection.Direction int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(@WindDirection.Direction int windDirection) {
        this.windDirection = windDirection;
    }

    public String getHourString() {
        return hourString;
    }

    public void setHourString(String hourString) {
        this.hourString = hourString;
    }

    public int getWsymb2Drawable() {
        return wsymb2Drawable;
    }

    public void setWsymb2Drawable(int wsymb2Drawable) {
        this.wsymb2Drawable = wsymb2Drawable;
    }

    public String getTemperatureString() {
        return temperatureString;
    }

    public void setTemperatureString(String temperatureString) {
        this.temperatureString = temperatureString;
    }

    public String getDayString() {
        return dayString;
    }

    public void setDayString(String dayString) {
        this.dayString = dayString;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPressureString() {
        return pressureString;
    }

    public void setPressureString(String pressureString) {
        this.pressureString = pressureString;
    }

    public String getRainAmountString() {
        return rainAmountString;
    }

    public void setRainAmountString(String rainAmountString) {
        this.rainAmountString = rainAmountString;
    }

    public String getVisibilityString() {
        return visibilityString;
    }

    public void setVisibilityString(String visibilityString) {
        this.visibilityString = visibilityString;
    }

    public String getHumidityString() {
        return humidityString;
    }

    public void setHumidityString(String humidityString) {
        this.humidityString = humidityString;
    }

    public String getGustSpeedString() {
        return gustSpeedString;
    }

    public void setGustSpeedString(String gustSpeedString) {
        this.gustSpeedString = gustSpeedString;
    }

    public String getCloudCoverString() {
        return cloudCoverString;
    }

    public void setCloudCoverString(String cloudCoverString) {
        this.cloudCoverString = cloudCoverString;
    }

    public String getFeelsLikeString() {
        return feelsLikeString;
    }

    public void setFeelsLikeString(String feelsLikeString) {
        this.feelsLikeString = feelsLikeString;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getWindDirectionString() {
        return windDirectionString;
    }

    public void setWindDirectionString(String windDirectionString) {
        this.windDirectionString = windDirectionString;
    }

    public int getWindDrawable() {
        return windDrawable;
    }

    public void setWindDrawable(int windDrawable) {
        this.windDrawable = windDrawable;
    }
}
