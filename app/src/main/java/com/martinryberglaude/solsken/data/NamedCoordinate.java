package com.martinryberglaude.solsken.data;

public class NamedCoordinate {
    private double lon;
    private double lat;
    private String name;

    public NamedCoordinate(double lon, double lat, String name) {
        this.lon = lon;
        this.lat = lat;
        this.name = name;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public String getName() {
        return name;
    }
}
