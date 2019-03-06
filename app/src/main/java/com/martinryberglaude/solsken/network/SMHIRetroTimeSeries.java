package com.martinryberglaude.solsken.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SMHIRetroTimeSeries {
    @SerializedName("validTime")
    @Expose
    private String validTime;
    @SerializedName("parameters")
    @Expose
    private List<SMHIRetroParameter> parameters = null;

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public List<SMHIRetroParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<SMHIRetroParameter> parameters) {
        this.parameters = parameters;
    }

    public String getDateString() {
        return validTime.split("T")[0];
    }
    public String getHourString() {
        String longHourString = validTime.split("T") [1];
        return longHourString.substring(0, 5);
    }
}
