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
    private String weatherId;
    private List<DayItem> dayList;
    private long expirationTime;

    public Weathers() {

    }

    @NonNull
    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public List<DayItem> getDayList() {
        return dayList;
    }

    public void setDayList(List<DayItem> dayList) {
        this.dayList = dayList;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }
}
