package com.martinryberglaude.skyfall;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.martinryberglaude.skyfall.data.DayItem;
import com.martinryberglaude.skyfall.data.HourItem;
import com.martinryberglaude.skyfall.interfaces.MainContract;
import com.martinryberglaude.skyfall.view.RecyclerViewAdapterHours;

import java.io.Serializable;
import java.util.Calendar;

public class HoursActivity extends AppCompatActivity implements MainContract.HourItemOnClickListener {

    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_hours);

        DayItem dayItem = (DayItem) getIntent().getSerializableExtra("day");
        toolbar = findViewById(R.id.toolbar_hrs);
        applyToolbarTheme();

        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        TextView toolbarTitle = findViewById(R.id.title_day);

        Calendar currentCal = Calendar.getInstance();
        Calendar tomorrowCal = Calendar.getInstance();
        tomorrowCal.add(Calendar.DAY_OF_MONTH, 1);
        currentCal.setTime(dayItem.getDate());
        boolean sameDay = currentCal.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) &&
                currentCal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);

        boolean nextDay = currentCal.get(Calendar.DAY_OF_YEAR) == tomorrowCal.get(Calendar.DAY_OF_YEAR) &&
                currentCal.get(Calendar.YEAR) == tomorrowCal.get(Calendar.YEAR);

        if (sameDay) {
            toolbarTitle.setText(R.string.today);
        } else if (nextDay) {
            toolbarTitle.setText(R.string.tomorrow);
        } else {
            toolbarTitle.setText(dayItem.getDayString());
        }
        recyclerView = findViewById(R.id.recycler_view_hours);
        recyclerView.setLayoutManager(new LinearLayoutManager(HoursActivity.this));
        initRecyclerView(dayItem);
    }

    public void initRecyclerView(DayItem dayItem) {
        RecyclerViewAdapterHours adapter = new RecyclerViewAdapterHours(HoursActivity.this, dayItem.getHourList(), HoursActivity.this);
        adapter.setHasStableIds(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(HourItem hourItem) {
        Intent intent = new Intent(HoursActivity.this, DetailsActivity.class);
        intent.putExtra("hour", (Serializable) hourItem);
        startActivity(intent);
    }

    private void applyTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String colorTheme = sharedPreferences.getString("theme", "auto");
        String colorThemeActual = sharedPreferences.getString("themeActual", "day");
        boolean darkTheme = sharedPreferences.getBoolean("dark_theme", false);
        if (!darkTheme) {
            switch (colorTheme) {
                case "auto":
                    switch (colorThemeActual) {
                        case "day":
                            setTheme(R.style.AppThemeDay);
                            break;
                        case "sunset":
                            setTheme(R.style.AppThemeSunrise);
                            break;
                        case "night":
                            setTheme(R.style.AppThemeNight);
                            break;
                        default:
                            setTheme(R.style.AppThemeDay);
                            break;
                    }
                    break;
                case "day":
                    setTheme(R.style.AppThemeDay);
                    break;
                case "sunset":
                    setTheme(R.style.AppThemeSunrise);
                    break;
                case "night":
                    setTheme(R.style.AppThemeNight);
                    break;
                default:
                    setTheme(R.style.AppThemeDay);
                    break;
            }
        } else {
            switch (colorTheme) {
                case "auto":
                    switch (colorThemeActual) {
                        case "day":
                            setTheme(R.style.DarkThemeDay);
                            break;
                        case "sunset":
                            setTheme(R.style.DarkThemeSunrise);
                            break;
                        case "night":
                            setTheme(R.style.DarkThemeNight);
                            break;
                        default:
                            setTheme(R.style.DarkThemeDay);
                            break;
                    }
                    break;
                case "day":
                    setTheme(R.style.DarkThemeDay);
                    break;
                case "sunset":
                    setTheme(R.style.DarkThemeSunrise);
                    break;
                case "night":
                    setTheme(R.style.DarkThemeNight);
                    break;
                default:
                    setTheme(R.style.DarkThemeDay);
                    break;
            }
        }
    }

    private void applyToolbarTheme() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.primaryColor, typedValue, true);
        @ColorInt int color = typedValue.data;
        toolbar.setBackgroundColor(color);
    }
}
