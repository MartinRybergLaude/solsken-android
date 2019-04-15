
package com.martinryberglaude.solsken.networkYR;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WindSpeed {

    @SerializedName("beaufort")
    @Expose
    private String beaufort;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("mps")
    @Expose
    private String mps;
    @SerializedName("name")
    @Expose
    private String name;

    public String getBeaufort() {
        return beaufort;
    }

    public void setBeaufort(String beaufort) {
        this.beaufort = beaufort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMps() {
        return mps;
    }

    public void setMps(String mps) {
        this.mps = mps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
