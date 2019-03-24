package com.martinryberglaude.solsken.database;

import com.martinryberglaude.solsken.data.DayItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Weathers {
    @NonNull
    @PrimaryKey
    private double weatherId;
    private List<DayItem> dayList;

    public Weathers() {

    }

    @NonNull
    public double getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(double weatherId) {
        this.weatherId = weatherId;
    }

    public List<DayItem> getDayList() {
        return dayList;
    }

    public void setDayList(List<DayItem> dayList) {
        this.dayList = dayList;
    }
}
