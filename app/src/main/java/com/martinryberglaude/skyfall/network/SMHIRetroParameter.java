package com.martinryberglaude.skyfall.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SMHIRetroParameter {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("levelType")
    @Expose
    private String levelType;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("values")
    @Expose
    private List<Double> values = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

}
