package com.martinryberglaude.skyfall.data;

import java.util.Date;

public class HeaderItem extends ListItem{

    private Date date;
    private String dayString;
    private String dateString;

    public String getDayString() {
        return dayString;
    }

    public void setDayString(String dayString) {
        this.dayString = dayString;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
