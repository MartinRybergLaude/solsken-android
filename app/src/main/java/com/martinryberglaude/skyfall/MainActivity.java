package com.martinryberglaude.skyfall;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.martinryberglaude.skyfall.data.DayItem;
import com.martinryberglaude.skyfall.data.HourItem;
import com.martinryberglaude.skyfall.data.WindDirection;
import com.martinryberglaude.skyfall.database.Locations;
import com.martinryberglaude.skyfall.interfaces.MainContract;
import com.martinryberglaude.skyfall.data.TimeOfDay;
import com.martinryberglaude.skyfall.data.Coordinate;
import com.martinryberglaude.skyfall.model.RemoveDatabaseLocationsAsyncTask;
import com.martinryberglaude.skyfall.model.RequestLocationModel;
import com.martinryberglaude.skyfall.model.RequestWeatherModel;
import com.martinryberglaude.skyfall.model.RetrieveDatabaseLocationsAsyncTask;
import com.martinryberglaude.skyfall.presenter.MainPresenter;
import com.martinryberglaude.skyfall.view.RecyclerViewAdapterDays;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements MainContract.View,
        MainContract.RequestLocationIntractor.OnFinishedListerner,
        MainContract.DayItemClickListener,
        MainContract.RetrieveDatabaseLocationsIntractor.OnFinishedListener,
        MainContract.RemoveDatabaseLocationsIntractor.OnFinishedListener{

    private RecyclerViewAdapterDays adapter;
    private RecyclerView recyclerView;
    private MainPresenter mainPresenter;
    private Toolbar toolbar;
    private Window window;
    private FrameLayout mainView;
    private Drawer drawer;

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
    private TextView sunriseText;
    private TextView sunsetText;

    private TextView cityText;

    private ImageButton btnSettings;
    private ImageButton btnSearch;
    private ImageButton btnMenu;
    private SwipeRefreshLayout pullToRefresh;
    private BottomSheetBehavior sheetBehavior;
    private RelativeLayout sheetLayout;
    private RelativeLayout fadeView;
    private List<DayItem> recyclerViewList = new ArrayList<>();

    private int statusBarColor;
    private int statusBarColorDark;
    private int toolbarColor;
    private boolean automaticTheme = true;
    private String theme;

    private List<Locations> locList = new ArrayList<>();
    private boolean autoLocation = true;
    private Locations selectedLocation;

    public Coordinate getCurrentCoordinate() {
        return currentCoordinate;
    }
    private Coordinate currentCoordinate;
    private MainContract.RequestLocationIntractor getLocationIntractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sets theming things
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        theme = sharedPreferences.getString("theme", "auto");
        applyTheme();
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
        sunriseText = findViewById(R.id.text_sunrise);
        sunsetText = findViewById(R.id.text_sunset);

        btnMenu = findViewById(R.id.btn_drawer);
        btnSettings = findViewById(R.id.btn_settings);

        pullToRefresh = findViewById(R.id.refresh);
        cityText = findViewById(R.id.title_city);
        sheetLayout = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(sheetLayout);
        fadeView = findViewById(R.id.fade_view);
        fadeView.getBackground().setAlpha(0);

        toolbar = findViewById(R.id.toolbar);
        mainView = findViewById(R.id.main_view);

        toolbar.animate().alpha(0.0f).setDuration(0);
        mainView.animate().alpha(0.0f).setDuration(0);

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
                SharedPreferences preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                // Check if content was refreshed less than five minutes ago
                if (System.currentTimeMillis() - preferences.getLong("cacheTime", 0) > 1000 * 60 * 5) {
                    // Actually refresh
                    mainPresenter.updateLocationAndUI();
                } else {
                    // Fake a refresh
                    pullToRefresh.setRefreshing(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            pullToRefresh.setRefreshing(false);
                        }
                    }, 500);
                }
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.nav_header)
                .withTranslucentStatusBar(false)
                .withDisplayBelowStatusBar(false)
                .withDrawerGravity(Gravity.START)
                .withActionBarDrawerToggle(false)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case -10:
                                autoLocation = true;
                                mainPresenter.updateLocationAndUI();
                                break;
                            default:
                                for (Locations location : locList) {
                                    if (drawerItem.getIdentifier() == location.getLocationId()) {
                                        autoLocation = false;
                                        selectedLocation = location;
                                        mainPresenter.updateLocationAndUI();
                                    }
                                }
                        }
                        return false;
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case -10:
                                break;
                            default:
                                for (final Locations location : locList) {
                                    if (drawerItem.getIdentifier() == location.getLocationId()) {
                                        new AlertDialog.Builder(MainActivity.this)
                                                .setTitle(getString(R.string.remove_location_title))
                                                .setMessage(getString(R.string.remove_location_body))
                                                .setPositiveButton(getString(R.string.remove_location_confirm), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        removeDrawerMenuItem(location);
                                                    }
                                                })
                                                .setNegativeButton(getString(R.string.remove_location_cancel), null)
                                                .show();
                                    }
                                }
                        }
                        return false;
                    }
                })
                .build();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer();
            }
        });

        btnSearch = drawer.getHeader().findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updateDrawerItems() {
        RetrieveDatabaseLocationsAsyncTask retrieveTask = new RetrieveDatabaseLocationsAsyncTask(this);
        retrieveTask.delegate = this;
        retrieveTask.execute();
    }
    private void removeDrawerMenuItem(Locations location) {
        RemoveDatabaseLocationsAsyncTask removeTask = new RemoveDatabaseLocationsAsyncTask(this, location);
        removeTask.delegate = this;
        removeTask.execute();
    }
    private void getDrawerMenuItems() {
        drawer.removeAllItems();

        drawer.addItem(new PrimaryDrawerItem().withIdentifier(-10).withName(getString(R.string.current_location)).withIcon(R.drawable.ic_my_location).withIconTintingEnabled(true));

        for (Locations location : locList) {
            drawer.addItem(new PrimaryDrawerItem().withIdentifier(location.getLocationId()).withName(location.getLocationName()).withIcon(R.drawable.ic_location).withIconTintingEnabled(true));
        }
        drawer.setSelection(-10);
    }

    @Override
    public void setColorTheme(TimeOfDay timeOfDay) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (automaticTheme) {
            switch (timeOfDay) {
                case SUNSET:
                case SUNRISE:
                    sharedPreferences.edit().putString("time", "sunset").putString("themeActual","sunset").apply();
                    mainView.setBackground(ContextCompat.getDrawable(this, R.drawable.background_sunrise));
                    window.setStatusBarColor(getResources().getColor(R.color.sunriseColor1));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.sunriseColor1));
                    statusBarColor = getResources().getColor(R.color.sunriseColor1);
                    statusBarColorDark = getResources().getColor(R.color.sunriseSecondary);
                    toolbarColor = getResources().getColor(R.color.sunrisePrimary);
                    break;
                case NIGHT:
                    sharedPreferences.edit().putString("time", "night").putString("themeActual","night").apply();
                    mainView.setBackground(ContextCompat.getDrawable(this, R.drawable.background_night));
                    window.setStatusBarColor(getResources().getColor(R.color.nightColor1));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.nightColor1));
                    statusBarColor = getResources().getColor(R.color.nightColor1);
                    statusBarColorDark = getResources().getColor(R.color.nightSecondary);
                    toolbarColor = getResources().getColor(R.color.nightPrimary);
                    break;
                case DAY:
                    sharedPreferences.edit().putString("time", "day").putString("themeActual","day").apply();
                    mainView.setBackground(ContextCompat.getDrawable(this, R.drawable.background_day));
                    window.setStatusBarColor(getResources().getColor(R.color.dayColor1));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.dayColor1));
                    statusBarColor = getResources().getColor(R.color.dayColor1);
                    statusBarColorDark = getResources().getColor(R.color.daySecondary);
                    toolbarColor = getResources().getColor(R.color.dayPrimary);
                    break;
            }
        } else {
            // Is sunset
            switch (theme) {
                case "sunset":
                    mainView.setBackground(ContextCompat.getDrawable(this, R.drawable.background_sunrise));
                    window.setStatusBarColor(getResources().getColor(R.color.sunriseColor1));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.sunriseColor1));
                    statusBarColor = getResources().getColor(R.color.sunriseColor1);
                    statusBarColorDark = getResources().getColor(R.color.sunriseSecondary);
                    toolbarColor = getResources().getColor(R.color.sunrisePrimary);
                    break;
                // Is night
                case "night":
                    mainView.setBackground(ContextCompat.getDrawable(this, R.drawable.background_night));
                    window.setStatusBarColor(getResources().getColor(R.color.nightColor1));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.nightColor1));
                    statusBarColor = getResources().getColor(R.color.nightColor1);
                    statusBarColorDark = getResources().getColor(R.color.nightSecondary);
                    toolbarColor = getResources().getColor(R.color.nightPrimary);
                    break;
                // Is day
                case "day":
                    mainView.setBackground(ContextCompat.getDrawable(this, R.drawable.background_day));
                    window.setStatusBarColor(getResources().getColor(R.color.dayColor1));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.dayColor1));
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
                sunriseText.setText(dayItem.getSunriseString());
                sunsetText.setText(dayItem.getSunsetString());

                toolbar.animate().alpha(1.0f).setDuration(200);
                mainView.animate().alpha(1.0f).setDuration(200);

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

        SharedPreferences preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putLong("cacheTime", System.currentTimeMillis()).commit();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(MainActivity.this, message,
                Toast.LENGTH_LONG).show();
    }
    @Override
    public String requestAdressString(Coordinate coordinate) {
        if (autoLocation) {
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
        } else {
            return selectedLocation.getLocationName();
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
        if (autoLocation) {
            getLocationIntractor.getLocation(this,this);
        } else {
            this.currentCoordinate = new Coordinate(selectedLocation.getLocationLon(), selectedLocation.getLocationLat());
            mainPresenter.loadColorTheme();
            mainPresenter.requestWeatherData();
        }
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

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        if (System.currentTimeMillis() - preferences.getLong("cacheTime", 0) > 1000 * 60 * 5) {
            mainPresenter.updateLocationAndUI();
        }
        updateDrawerItems();
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

    @Override
    public void onFinishedFormatDatabaseLocations(List<Locations> locationList) {
        locList.clear();
        locList.addAll(locationList);
        getDrawerMenuItems();
    }

    @Override
    public void onFailureFormatDatabaseLocations() {
        showToast(getResources().getString(R.string.database_error));
    }

    @Override
    public void onFinishedRemoveDatabaseLocations(long identifier) {
        // Remove visible drawer item
        drawer.removeItem(identifier);
        drawer.setSelection(-10);
    }

    @Override
    public void onFailureRemoveDatabaseLocations() {
        showToast(getString(R.string.database_remove_error));
    }
}

