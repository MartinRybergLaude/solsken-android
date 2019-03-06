package com.martinryberglaude.solsken.data;

import java.util.Objects;

public class LocationItem {
    private String cityString;
    private String countryString;
    private double lon;
    private double lat;


    public String getCityString() {
        return cityString;
    }

    public void setCityString(String cityString) {
        this.cityString = cityString;
    }

    public String getCountryString() {
        return countryString;
    }

    public void setCountryString(String countryString) {
        this.countryString = countryString;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof LocationItem))return false;
        if (((LocationItem) other).getCityString().equals(this.cityString) && ((LocationItem) other).getCountryString().equals(this.countryString)) return true;
        else return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityString, countryString);
    }
}
