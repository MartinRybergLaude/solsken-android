
package com.martinryberglaude.solsken.networkYR;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Time {

    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("datatype")
    @Expose
    private String datatype;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getDateString() {
        return to.split("T")[0];
    }
    public String getDateStringFrom() {
        return from.split("T")[0];
    }

    public String getHourString() {
        String longHourString = to.split("T") [1];
        return longHourString.substring(0, 5);
    }

}
