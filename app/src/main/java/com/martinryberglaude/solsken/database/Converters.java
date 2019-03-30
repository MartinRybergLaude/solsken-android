package com.martinryberglaude.solsken.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.martinryberglaude.solsken.data.DayItem;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static List<DayItem> fromString(String value) {
        Type listType = new TypeToken<List<DayItem>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<DayItem> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
