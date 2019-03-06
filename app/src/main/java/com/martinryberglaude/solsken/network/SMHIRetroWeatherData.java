package com.martinryberglaude.solsken.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SMHIRetroWeatherData {

    @SerializedName("approvedTime")
    @Expose
    private String approvedTime;
    @SerializedName("referenceTime")
    @Expose
    private String referenceTime;
    @SerializedName("geometry")
    @Expose
    private SMHIRetroGeometry geometry;
    @SerializedName("timeSeries")
    @Expose
    private List<SMHIRetroTimeSeries> timeSeries = null;

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

    public SMHIRetroGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(SMHIRetroGeometry geometry) {
        this.geometry = geometry;
    }

    public List<SMHIRetroTimeSeries> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(List<SMHIRetroTimeSeries> timeSeries) {
        this.timeSeries = timeSeries;
    }

}
