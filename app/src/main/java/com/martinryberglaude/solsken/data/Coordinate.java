package com.martinryberglaude.solsken.data;

public class Coordinate {
    private double lon;
    private double lat;

    public Coordinate(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
