
package com.martinryberglaude.solsken.networkYR;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("cloudiness")
    @Expose
    private Cloudiness cloudiness;
    @SerializedName("humidity")
    @Expose
    private Humidity humidity;
    @SerializedName("altitude")
    @Expose
    private String altitude;
    @SerializedName("fog")
    @Expose
    private Fog fog;
    @SerializedName("areaMaxWindSpeed")
    @Expose
    private AreaMaxWindSpeed areaMaxWindSpeed;
    @SerializedName("highClouds")
    @Expose
    private HighClouds highClouds;
    @SerializedName("mediumClouds")
    @Expose
    private MediumClouds mediumClouds;
    @SerializedName("windGust")
    @Expose
    private WindGust windGust;
    @SerializedName("lowClouds")
    @Expose
    private LowClouds lowClouds;
    @SerializedName("pressure")
    @Expose
    private Pressure pressure;
    @SerializedName("windSpeed")
    @Expose
    private WindSpeed windSpeed;
    @SerializedName("dewpointTemperature")
    @Expose
    private DewpointTemperature dewpointTemperature;
    @SerializedName("windDirection")
    @Expose
    private WindDirection windDirection;
    @SerializedName("temperature")
    @Expose
    private Temperature temperature;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("symbol")
    @Expose
    private Symbol symbol;
    @SerializedName("precipitation")
    @Expose
    private Precipitation precipitation;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Cloudiness getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(Cloudiness cloudiness) {
        this.cloudiness = cloudiness;
    }

    public Humidity getHumidity() {
        return humidity;
    }

    public void setHumidity(Humidity humidity) {
        this.humidity = humidity;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public Fog getFog() {
        return fog;
    }

    public void setFog(Fog fog) {
        this.fog = fog;
    }

    public AreaMaxWindSpeed getAreaMaxWindSpeed() {
        return areaMaxWindSpeed;
    }

    public void setAreaMaxWindSpeed(AreaMaxWindSpeed areaMaxWindSpeed) {
        this.areaMaxWindSpeed = areaMaxWindSpeed;
    }

    public HighClouds getHighClouds() {
        return highClouds;
    }

    public void setHighClouds(HighClouds highClouds) {
        this.highClouds = highClouds;
    }

    public MediumClouds getMediumClouds() {
        return mediumClouds;
    }

    public void setMediumClouds(MediumClouds mediumClouds) {
        this.mediumClouds = mediumClouds;
    }

    public WindGust getWindGust() {
        return windGust;
    }

    public void setWindGust(WindGust windGust) {
        this.windGust = windGust;
    }

    public LowClouds getLowClouds() {
        return lowClouds;
    }

    public void setLowClouds(LowClouds lowClouds) {
        this.lowClouds = lowClouds;
    }

    public Pressure getPressure() {
        return pressure;
    }

    public void setPressure(Pressure pressure) {
        this.pressure = pressure;
    }

    public WindSpeed getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(WindSpeed windSpeed) {
        this.windSpeed = windSpeed;
    }

    public DewpointTemperature getDewpointTemperature() {
        return dewpointTemperature;
    }

    public void setDewpointTemperature(DewpointTemperature dewpointTemperature) {
        this.dewpointTemperature = dewpointTemperature;
    }

    public WindDirection getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(WindDirection windDirection) {
        this.windDirection = windDirection;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public Precipitation getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Precipitation precipitation) {
        this.precipitation = precipitation;
    }

}
