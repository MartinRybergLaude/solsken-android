package com.martinryberglaude.skyfall.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.martinryberglaude.skyfall.HoursActivity;
import com.martinryberglaude.skyfall.PermissionActivity;
import com.martinryberglaude.skyfall.SettingsActivity;
import com.martinryberglaude.skyfall.data.DayItem;
import com.martinryberglaude.skyfall.data.HourItem;
import com.martinryberglaude.skyfall.data.WindDirection;
import com.martinryberglaude.skyfall.interfaces.MainContract;
import com.martinryberglaude.skyfall.R;
import com.martinryberglaude.skyfall.data.TimeOfDay;
import com.martinryberglaude.skyfall.data.Coordinate;
import com.martinryberglaude.skyfall.model.RequestLocationModel;
import com.martinryberglaude.skyfall.model.RequestWeatherModel;
import com.martinryberglaude.skyfall.presenter.MainPresenter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements MainContract.View, MainContract.RequestLocationIntractor.OnFinishedListerner, MainContract.DayItemClickListener {

    private RecyclerViewAdapterDays adapter;
    private RecyclerView recyclerView;
    private MainPresenter mainPresenter;
    private ActionBar actionBar;
    private Window window;

    private TextView temperatureText;
    private TextView wsymb2Text;
    private ImageView wsymb2Icon;

    private ImageView windIcon;
    private TextView icWindSpeedText;
    private TextView icWindDirectionText;
    private TextView icPressureText;
    private TextView icRainAmount;
    private TextView icVisibilityText;
    private TextView icHumidityText;
    private TextView icGustSpeedText;
    private TextView icCloudCoverText;
    private TextView icFeelsLikeText;

    private TextView cityText;

    private ImageButton btnSettings;
    private SwipeRefreshLayout pullToRefresh;
    private BottomSheetBehavior sheetBehavior;
    private RelativeLayout sheetLayout;
    private RelativeLayout fadeView;
    private View toolbarShadow;
    private List<DayItem> recyclerViewList = new ArrayList<>();

    private int statusBarColor;
    private int statusBarColorDark;
    private int toolbarColor;
    private boolean automaticTheme = true;
    private String theme;

    public Coordinate getCurrentCoordinate() {
        return currentCoordinate;
    }
    private Coordinate currentCoordinate;
    private MainContract.RequestLocationIntractor getLocationIntractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean darkTheme = sharedPreferences.getBoolean("dark_theme", false);
        theme = sharedPreferences.getString("theme", "auto");
        if (!darkTheme) {
            setTheme(R.style.AppThemeDay);
        } else {
            setTheme(R.style.DarkThemeDay);
        }
        if (!theme.equals("auto")) automaticTheme = false;

        setContentView(R.layout.activity_main);

        // Check if GPS permission has been granted
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasPermission()) {
            // Launch permission activity
            Intent intent = new Intent(MainActivity.this, PermissionActivity.class);
            this.startActivity(intent);
            finish();
            return;
        }

        window = getWindow();
        recyclerView = findViewById(R.id.recycler_view_days);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        temperatureText = findViewById(R.id.title_temperature);
        wsymb2Text = findViewById(R.id.title_wsymb2);
        wsymb2Icon = findViewById(R.id.wi_icon);

        windIcon = findViewById(R.id.ic_wind_icon);
        icWindSpeedText = findViewById(R.id.ic_wind_speed);
        icWindDirectionText = findViewById(R.id.ic_wind_direction);
        icPressureText = findViewById(R.id.ic_air_pressure);
        icRainAmount = findViewById(R.id.ic_rain_amount);
        icVisibilityText = findViewById(R.id.ic_visibility);
        icHumidityText = findViewById(R.id.ic_humidity);
        icGustSpeedText = findViewById(R.id.ic_gust_speed);
        icCloudCoverText = findViewById(R.id.ic_cloud_cover);
        icFeelsLikeText = findViewById(R.id.ic_feels_like);

        btnSettings = findViewById(R.id.btn_settings);
        pullToRefresh = findViewById(R.id.refresh);
        cityText = findViewById(R.id.title_city);
        sheetLayout = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(sheetLayout);
        toolbarShadow = findViewById(R.id.shadow);
        fadeView = findViewById(R.id.fade_view);
        fadeView.getBackground().setAlpha(0);
        toolbarShadow.getBackground().setAlpha(0);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
                int blendedColorStatusBar = blendColors(statusBarColor, statusBarColorDark, slideOffset);
                int blendedColorToolbar = blendColors(statusBarColor, toolbarColor, slideOffset);

                window.setStatusBarColor(blendedColorStatusBar);
                toolbar.setBackgroundColor(blendedColorToolbar);
                fadeView.getBackground().setAlpha(Math.round(slideOffset * 255));
                toolbarShadow.getBackground().setAlpha(Math.round(slideOffset * 255));
            }
        });

        int target = getResources().getDimensionPixelSize(R.dimen.refresh_start);
        int start = getResources().getDimensionPixelSize(R.dimen.refresh_start);
        int end = getResources().getDimensionPixelSize(R.dimen.refresh_end);
        pullToRefresh.setProgressViewEndTarget(true, target);
        pullToRefresh.setProgressViewOffset(true, start, end);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        getLocationIntractor = new RequestLocationModel(locationManager);
        mainPresenter = new MainPresenter(this, new RequestWeatherModel(), getResources().getString(R.string.weather_error));
        mainPresenter.updateLocationAndUI();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainPresenter.updateLocationAndUI();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void setColorTheme(TimeOfDay timeOfDay) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (automaticTheme) {
            switch (timeOfDay) {
                case SUNSET:
                case SUNRISE:
                    sharedPreferences.edit().putString("time", "sunset").putString("themeActual","sunset").apply();
                    pullToRefresh.setBackground(ContextCompat.getDrawable(this, R.drawable.background_sunrise));
                    window.setStatusBarColor(getResources().getColor(R.color.sunriseColor1));
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.sunriseColor1)));
                    statusBarColor = getResources().getColor(R.color.sunriseColor1);
                    statusBarColorDark = getResources().getColor(R.color.sunriseSecondary);
                    toolbarColor = getResources().getColor(R.color.sunrisePrimary);
                    break;
                case NIGHT:
                    sharedPreferences.edit().putString("time", "night").putString("themeActual","night").apply();
                    pullToRefresh.setBackground(ContextCompat.getDrawable(this, R.drawable.background_night));
                    window.setStatusBarColor(getResources().getColor(R.color.nightColor1));
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.nightColor1)));
                    statusBarColor = getResources().getColor(R.color.nightColor1);
                    statusBarColorDark = getResources().getColor(R.color.nightSecondary);
                    toolbarColor = getResources().getColor(R.color.nightPrimary);
                    break;
                case DAY:
                    sharedPreferences.edit().putString("time", "day").putString("themeActual","day").apply();
                    pullToRefresh.setBackground(ContextCompat.getDrawable(this, R.drawable.background_day));
                    window.setStatusBarColor(getResources().getColor(R.color.dayColor1));
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dayColor1)));
                    statusBarColor = getResources().getColor(R.color.dayColor1);
                    statusBarColorDark = getResources().getColor(R.color.daySecondary);
                    toolbarColor = getResources().getColor(R.color.dayPrimary);
                    break;
            }
        } else {
            // Is sunset
            switch (theme) {
                case "sunset":
                    pullToRefresh.setBackground(ContextCompat.getDrawable(this, R.drawable.background_sunrise));
                    window.setStatusBarColor(getResources().getColor(R.color.sunriseColor1));
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.sunriseColor1)));
                    statusBarColor = getResources().getColor(R.color.sunriseColor1);
                    statusBarColorDark = getResources().getColor(R.color.sunriseSecondary);
                    toolbarColor = getResources().getColor(R.color.sunrisePrimary);
                    break;
                // Is night
                case "night":
                    pullToRefresh.setBackground(ContextCompat.getDrawable(this, R.drawable.background_night));
                    window.setStatusBarColor(getResources().getColor(R.color.nightColor1));
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.nightColor1)));
                    statusBarColor = getResources().getColor(R.color.nightColor1);
                    statusBarColorDark = getResources().getColor(R.color.nightSecondary);
                    toolbarColor = getResources().getColor(R.color.nightPrimary);
                    break;
                // Is day
                case "day":
                    pullToRefresh.setBackground(ContextCompat.getDrawable(this, R.drawable.background_day));
                    window.setStatusBarColor(getResources().getColor(R.color.dayColor1));
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dayColor1)));
                    statusBarColor = getResources().getColor(R.color.dayColor1);
                    statusBarColorDark = getResources().getColor(R.color.daySecondary);
                    toolbarColor = getResources().getColor(R.color.dayPrimary);
                    break;
            }
        }
        fadeView.setBackgroundColor(toolbarColor);
        fadeView.getBackground().setAlpha(0);
    }

    @Override
    public void updateWeatherUI(final List<DayItem> dayList, final String city, final boolean initRecyclerview) {
        // Wait 200ms for refresh animation to finish
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DayItem dayItem = dayList.get(0);
                HourItem hourItem = dayItem.getHourList().get(0);
                cityText.setText(city);

                wsymb2Text.setText(hourItem.getWsymb2String());
                temperatureText.setText(hourItem.getTemperatureString());
                wsymb2Icon.setImageResource(hourItem.getWsymb2Drawable());
                setWindDirectionUI(hourItem.getWindDirection());
                icWindSpeedText.setText(hourItem.getWindSpeedString());
                icPressureText.setText(hourItem.getPressureString());
                icRainAmount.setText(hourItem.getRainAmountString());
                icVisibilityText.setText(hourItem.getVisbilityString());
                icHumidityText.setText(hourItem.getHumidityString());
                icGustSpeedText.setText(hourItem.getGustSpeedString());
                icCloudCoverText.setText(hourItem.getCloudCoverString());
                icFeelsLikeText.setText(hourItem.getFeelsLikeString());

                if (initRecyclerview) {
                    recyclerViewList.addAll(dayList);
                    adapter = new RecyclerViewAdapterDays(MainActivity.this, recyclerViewList, MainActivity.this);
                    adapter.setHasStableIds(true);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setItemViewCacheSize(20);
                    recyclerView.setDrawingCacheEnabled(true);
                    recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    recyclerView.setAdapter(adapter);
                } else {
                    recyclerViewList.clear();
                    recyclerViewList.addAll(dayList);
                    adapter.notifyDataSetChanged();
                }
            }
        }, 200);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(MainActivity.this, message,
                Toast.LENGTH_LONG).show();
    }
    @Override
    public String requestAdressString(Coordinate coordinate) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(coordinate.getLat(), coordinate.getLon(), 1);
            String city = addresses.get(0).getLocality();
            String knownName = addresses.get(0).getFeatureName();
            if (city == null) return knownName;
            else return city;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void showRefresh(final boolean b) {
        pullToRefresh.post(new Runnable() {
            @Override
            public void run() {
                pullToRefresh.setRefreshing(b);
            }
        });
    }
    private boolean hasPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Get location asynchronously, has to be called from activity as it needs context
    @Override
    public void updateLocationAndUI() {
        getLocationIntractor.getLocation(this,this);
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences;
    }

    // When location has been retrieved, launch all other tasks.
    @Override
    public void onFinishedRetrieveLocation(Coordinate coordinate) {
        this.currentCoordinate = coordinate;
        mainPresenter.loadColorTheme();
        mainPresenter.requestWeatherData();
    }

    @Override
    public void onFailureRetrieveLocationn() {
        showToast(getResources().getString(R.string.weather_error));
    }
    private int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }

    private void setWindDirectionUI(@WindDirection.Direction int windDirection) {
        String windDirectionString;

        switch (windDirection) {
            case WindDirection.N:
                windDirectionString = getResources().getString(R.string.north);
                icWindDirectionText.setText(windDirectionString);
                windIcon.setRotation(180);
                break;
            case WindDirection.NE:
                windDirectionString = getResources().getString(R.string.northeast);
                icWindDirectionText.setText(windDirectionString);
                windIcon.setRotation(225);
                break;
            case WindDirection.E:
                windDirectionString = getResources().getString(R.string.east);
                icWindDirectionText.setText(windDirectionString);
                windIcon.setRotation(270);
                break;
            case WindDirection.SE:
                windDirectionString = getResources().getString(R.string.southeast);
                icWindDirectionText.setText(windDirectionString);
                windIcon.setRotation(315);
                break;
            case WindDirection.S:
                windDirectionString = getResources().getString(R.string.south);
                icWindDirectionText.setText(windDirectionString);
                windIcon.setRotation(0);
                break;
            case WindDirection.SW:
                windDirectionString = getResources().getString(R.string.southwest);
                icWindDirectionText.setText(windDirectionString);
                windIcon.setRotation(45);
                break;
            case WindDirection.W:
                windDirectionString = getResources().getString(R.string.west);
                icWindDirectionText.setText(windDirectionString);
                windIcon.setRotation(90);
                break;
            case WindDirection.NW:
                windDirectionString = getResources().getString(R.string.northwest);
                icWindDirectionText.setText(windDirectionString);
                windIcon.setRotation(135);
                break;
        }
    }

    @Override
    public void onItemClick(DayItem dayItem) {
        Intent intent = new Intent(MainActivity.this, HoursActivity.class);
        intent.putExtra("day", (Serializable) dayItem);
        startActivity(intent);
    }
}

