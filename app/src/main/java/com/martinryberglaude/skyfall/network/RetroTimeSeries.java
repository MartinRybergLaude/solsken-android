package com.martinryberglaude.skyfall.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetroTimeSeries {
    @SerializedName("validTime")
    @Expose
    private String validTime;
    @SerializedName("parameters")
    @Expose
    private List<RetroParameter> parameters = null;

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public List<RetroParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<RetroParameter> parameters) {
        this.parameters = parameters;
    }

    public String getDateString() {
        return validTime.split("T")[0];
    }
    public String getHourString() {
        return validTime.split("T")[1];
    }
}
