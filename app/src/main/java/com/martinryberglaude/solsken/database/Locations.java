package com.martinryberglaude.solsken.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Locations {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int locationId;
    private String locationName;
    private String locationCountry;
    private double locationLon;
    private double locationLat;

    public Locations() {

    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    @NonNull
    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(@NonNull int locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLocationLon() {
        return locationLon;
    }

    public void setLocationLon(double locationLon) {
        this.locationLon = locationLon;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }
}
