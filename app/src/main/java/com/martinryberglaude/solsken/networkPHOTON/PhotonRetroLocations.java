package com.martinryberglaude.solsken.networkPHOTON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotonRetroLocations {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("features")
    @Expose
    private List<PhotonRetroFeature> features = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PhotonRetroFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<PhotonRetroFeature> features) {
        this.features = features;
    }
}
