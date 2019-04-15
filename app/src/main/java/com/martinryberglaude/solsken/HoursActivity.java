package com.martinryberglaude.solsken;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.martinryberglaude.solsken.data.DayItem;
import com.martinryberglaude.solsken.view.RecyclerViewAdapterHours;

import java.io.Serializable;
import java.util.Calendar;

public class HoursActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private DayItem dayItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_hours);

        dayItem = (DayItem) getIntent().getSerializableExtra("day");
        toolbar = findViewById(R.id.toolbar_hrs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        RecyclerViewAdapterHours adapter = new RecyclerViewAdapterHours(HoursActivity.this, dayItem.getHourList());
        adapter.setHasStableIds(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (dayItem.getHourList().size() > 2) {
            getMenuInflater().inflate(R.menu.menu_hours_toolbar, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_stats:
                Intent intent= new Intent(HoursActivity.this, GraphsActivity.class);
                intent.putExtra("dayItem", (Serializable) dayItem);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void applyTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String colorTheme = sharedPreferences.getString("theme", "day");
        boolean darkTheme = sharedPreferences.getBoolean("dark_theme", false);
        if (!darkTheme) {
            switch (colorTheme) {
                case "day":
                    setTheme(R.style.AppThemeDay);
                    break;
                case "sunset":
                    setTheme(R.style.AppThemeSunrise);
                    break;
                case "night":
                    setTheme(R.style.AppThemeNight);
                    break;
                case "aurora":
                    setTheme(R.style.AppThemeAurora);
                    break;
                default:
                    setTheme(R.style.AppThemeDay);
                    break;
            }
        } else {
            switch (colorTheme) {
                case "day":
                    setTheme(R.style.DarkThemeDay);
                    break;
                case "sunset":
                    setTheme(R.style.DarkThemeSunrise);
                    break;
                case "night":
                    setTheme(R.style.DarkThemeNight);
                    break;
                case "aurora":
                    setTheme(R.style.DarkThemeAurora);
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
