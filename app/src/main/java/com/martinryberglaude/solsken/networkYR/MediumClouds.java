
package com.martinryberglaude.solsken.networkYR;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediumClouds {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("percent")
    @Expose
    private String percent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

}
