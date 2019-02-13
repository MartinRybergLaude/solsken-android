package com.martinryberglaude.skyfall.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetroWeatherData {

    @SerializedName("approvedTime")
    @Expose
    private String approvedTime;
    @SerializedName("referenceTime")
    @Expose
    private String referenceTime;
    @SerializedName("geometry")
    @Expose
    private RetroGeometry geometry;
    @SerializedName("timeSeries")
    @Expose
    private List<RetroTimeSeries> timeSeries = null;

    public String getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(String approvedTime) {
        this.approvedTime = approvedTime;
    }

    public String getReferenceTime() {
        return referenceTime;
    }

    public void setReferenceTime(String referenceTime) {
        this.referenceTime = referenceTime;
    }

    public RetroGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(RetroGeometry geometry) {
        this.geometry = geometry;
    }

    public List<RetroTimeSeries> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(List<RetroTimeSeries> timeSeries) {
        this.timeSeries = timeSeries;
    }

}
