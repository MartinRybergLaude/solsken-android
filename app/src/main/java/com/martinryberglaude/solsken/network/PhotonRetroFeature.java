package com.martinryberglaude.solsken.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotonRetroFeature {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("geometry")
    @Expose
    private PhotonRetroGeometry geometry;
    @SerializedName("properties")
    @Expose
    private PhotonRetroProperties properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PhotonRetroGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(PhotonRetroGeometry geometry) {
        this.geometry = geometry;
    }

    public PhotonRetroProperties getProperties() {
        return properties;
    }

    public void setProperties(PhotonRetroProperties properties) {
        this.properties = properties;
    }
}
