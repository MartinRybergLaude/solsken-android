package com.martinryberglaude.solsken.networkPHOTON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotonRetroGeometry {

    @SerializedName("coordinates")
    @Expose
    private List<Double> coordinates = null;
    @SerializedName("type")
    @Expose
    private String type;

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
