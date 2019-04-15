
package com.martinryberglaude.solsken.networkYR;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("runended")
    @Expose
    private String runended;
    @SerializedName("termin")
    @Expose
    private String termin;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("nextrun")
    @Expose
    private String nextrun;
    @SerializedName("from")
    @Expose
    private String from;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRunended() {
        return runended;
    }

    public void setRunended(String runended) {
        this.runended = runended;
    }

    public String getTermin() {
        return termin;
    }

    public void setTermin(String termin) {
        this.termin = termin;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getNextrun() {
        return nextrun;
    }

    public void setNextrun(String nextrun) {
        this.nextrun = nextrun;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

}
