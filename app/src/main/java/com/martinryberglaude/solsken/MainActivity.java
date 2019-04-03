package com.martinryberglaude.solsken;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.martinryberglaude.solsken.data.DayItem;
import com.martinryberglaude.solsken.data.HourItem;
import com.martinryberglaude.solsken.data.WindDirection;
import com.martinryberglaude.solsken.database.Locations;
import com.martinryberglaude.solsken.database.WeatherDatabase;
import com.martinryberglaude.solsken.database.Weathers;
import com.martinryberglaude.solsken.interfaces.MainContract;
import com.martinryberglaude.solsken.data.Coordinate;
import com.martinryberglaude.solsken.model.RemoveDatabaseLocationsAsyncTask;
import com.martinryberglaude.solsken.model.RemoveDatabaseWeathersAsyncTask;
import com.martinryberglaude.solsken.model.RequestLocationModel;
import com.martinryberglaude.solsken.model.RequestSMHIWeatherModel;
import com.martinryberglaude.solsken.model.RequestYRWeatherModel;
import com.martinryberglaude.solsken.model.RetrieveDatabaseLocationsAsyncTask;
import com.martinryberglaude.solsken.model.RetrieveDatabaseWeathersAsyncTask;
import com.martinryberglaude.solsken.presenter.MainPresenter;
import com.martinryberglaude.solsken.utils.OnSwipeTouchListener;
import com.martinryberglaude.solsken.view.RecyclerViewAdapterDays;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements MainContract.View,
        MainContract.RequestLocationIntractor.OnFinishedListerner,
        MainContract.DayItemClickListener,
        MainContract.RetrieveDatabaseLocationsIntractor.OnFinishedListener,
        MainContract.RemoveDatabaseLocationsIntractor.OnFinishedListener,
        MainContract.RetrieveDatabaseWeathersIntractor.OnFinishedListener,
        MainContract.RemoveDatabaseWeathersIntractor.OnFinishedListener{

    private RecyclerViewAdapterDays adapter;
    private RecyclerView recyclerView;
    private MainPresenter mainPresenter;
    private Toolbar toolbar;
    private Window window;
    private FrameLayout mainView;
    private Drawer drawer;
    private boolean isStart = true;

    private int cacheTotalValidTime = 1000 * 60 * 60 * 6;
    private int cacheValidTime = 1000 * 60 * 60;
    private int reloadValidTime = 1000 * 60 * 5;

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
    private TextView locationErrorTitle;
    private TextView locationErrorBody;

    private SwipeRefreshLayout pullToRefresh;
    private BottomSheetBehavior sheetBehavior;
    private RelativeLayout sheetLayout;
    private RelativeLayout fadeView;
    private List<DayItem> recyclerViewList = new ArrayList<>();

    private int statusBarColor;
    private int statusBarColorDark;
    private int toolbarColor;
    private String theme;

    private List<Locations> locList = new ArrayList<>();
    private boolean autoLocation = true;
    private Locations selectedLocation;
    private int selectedDrawerPosition;

    public Coordinate getCurrentCoordinate() {
        return currentCoordinate;
    }
    private Coordinate currentCoordinate;
    private MainContract.RequestLocationIntractor getLocationIntractor;

    private List<DayItem> currentDayList;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sets theming things
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        theme = sharedPreferences.getString("theme", "day");
        applyTheme();

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

        locationErrorTitle = findViewById(R.id.error_title);
        locationErrorBody = findViewById(R.id.error_body);

        pullToRefresh = findViewById(R.id.refresh);
        cityText = findViewById(R.id.title_city);
        sheetLayout = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(sheetLayout);
        fadeView = findViewById(R.id.fade_view);
        fadeView.getBackground().setAlpha(0);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        mainPresenter = new MainPresenter(this, new RequestSMHIWeatherModel(), new RequestYRWeatherModel(), getResources().getString(R.string.weather_error));
        mainPresenter.updateLocationAndUI();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SharedPreferences preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                // Check if content was refreshed less than five minutes ago
                if (System.currentTimeMillis() - preferences.getLong("reloadTime", 0) > reloadValidTime) {
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
        PrimaryDrawerItem settingsItem = new PrimaryDrawerItem().withName(getString(R.string.settings)).withIcon(R.drawable.ic_settings).withIconTintingEnabled(true).withIdentifier(-20);
        PrimaryDrawerItem aboutItem = new PrimaryDrawerItem().withName(getString(R.string.about)).withIcon(R.drawable.ic_info).withIconTintingEnabled(true).withIdentifier(-30);
        List<IDrawerItem> drawerItems = new ArrayList<>();
        drawerItems.add(settingsItem);
        drawerItems.add(aboutItem);
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.nav_header)
                .withTranslucentStatusBar(false)
                .withDisplayBelowStatusBar(false)
                .withDrawerGravity(Gravity.START)
                .withStickyDrawerItems(drawerItems)
                .withStickyFooterShadow(false)
                .withStickyFooterDivider(true)
                .withActionBarDrawerToggle(false)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case -10:
                                selectedDrawerPosition = -10;
                                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                autoLocation = true;
                                mainPresenter.updateLocationAndUI();
                                break;
                            case -20:
                                Intent intent= new Intent(MainActivity.this, SettingsActivity.class);
                                startActivityForResult(intent, 2);
                                return true;
                            case -30:
                                Intent intent2= new Intent(MainActivity.this, AboutActivity.class);
                                startActivityForResult(intent2,2);
                                return true;
                            default:
                                for (Locations location : locList) {
                                    if (drawerItem.getIdentifier() == location.getLocationId()) {
                                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                        autoLocation = false;
                                        selectedLocation = location;
                                        selectedDrawerPosition = location.getLocationId();
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
        updateDrawerItems();

        ImageButton btnMenu = findViewById(R.id.btn_drawer);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer();
            }
        });

        ImageButton btnSearch = drawer.getHeader().findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        fadeView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            public void onSwipeRight() {
                drawer.openDrawer();
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

        Weathers weathers = new Weathers();
        weathers.setWeatherId(location.getLocationName() + "smhi");
        Weathers weathers2 = new Weathers();
        weathers2.setWeatherId(location.getLocationName() + "yr");
        List<Weathers> weatherList = new ArrayList<>();
        weatherList.add(weathers);
        weatherList.add(weathers2);
        RemoveDatabaseWeathersAsyncTask removeWeathersTask = new RemoveDatabaseWeathersAsyncTask(this, weatherList);
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
    public void setColorTheme() {
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
            case "aurora":
                mainView.setBackground(ContextCompat.getDrawable(this, R.drawable.background_aurora));
                window.setStatusBarColor(getResources().getColor(R.color.auroraColor1));
                toolbar.setBackgroundColor(getResources().getColor(R.color.auroraColor1));
                statusBarColor = getResources().getColor(R.color.auroraColor1);
                statusBarColorDark = getResources().getColor(R.color.auroraSecondary);
                toolbarColor = getResources().getColor(R.color.auroraPrimary);
                break;
        }
        fadeView.setBackgroundColor(toolbarColor);
        fadeView.getBackground().setAlpha(0);
    }

    @Override
    public void updateWeatherUI(final List<DayItem> dayList, final String city, final boolean initRecyclerview) {
        currentDayList = new ArrayList<>(dayList);

        // Wait 200ms for refresh animation to finish
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainView.setVisibility(View.VISIBLE);
                sheetLayout.setVisibility(View.VISIBLE);
                locationErrorTitle.setVisibility(View.INVISIBLE);
                locationErrorBody.setVisibility(View.INVISIBLE);

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
                icVisibilityText.setText(hourItem.getVisibilityString());
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
        preferences.edit().putLong("reloadTime", System.currentTimeMillis()).commit();
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

    private boolean cacheExists = false;
    // Get location asynchronously, has to be called from activity as it needs context
    @Override
    public void updateLocationAndUI() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean isWithinTimeLimit = System.currentTimeMillis() - prefs.getLong("cacheTime", 999999999) < cacheTotalValidTime;
        final String provider = prefs.getString("data_src", "smhi");
        setCacheExists(false);
        if (autoLocation) {
            getLocationIntractor.getLocation(this,this);
        } else {
            currentCoordinate = new Coordinate(selectedLocation.getLocationLon(), selectedLocation.getLocationLat());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (isWithinTimeLimit) {
                        WeatherDatabase weatherDatabase = Room.databaseBuilder(getApplicationContext(),
                                WeatherDatabase.class, "weathers_db")
                                .fallbackToDestructiveMigration()
                                .build();
                        Weathers weathers = weatherDatabase.daoAccess().fetchWeathersById(requestAdressString(currentCoordinate) + provider);
                        if (weathers != null && System.currentTimeMillis() - weathers.getExpirationTime() < cacheValidTime) {
                            setCacheExists(true);
                        }
                        weatherDatabase.close();
                    }
                }
            }).start();

            // Use cached data
            if (cacheExists) {
                setCacheExists(false);
                loadCachedData();
            }
            // Do not use cached data
            else {
                mainPresenter.loadColorTheme();
                String dataSource = prefs.getString("data_src", "smhi");
                if (dataSource.equals("smhi")) {
                    mainPresenter.requestWeatherData(true);
                } else {
                    mainPresenter.requestWeatherData(false);
                }
            }
        }
    }
    private void setCacheExists(boolean b) {
        cacheExists = b;
    }

    private void loadCachedData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String provider = prefs.getString("data_src","smhi");
        String id = requestAdressString(currentCoordinate) + provider;
        mainPresenter.loadColorTheme();

        RetrieveDatabaseWeathersAsyncTask retrieveTask = new RetrieveDatabaseWeathersAsyncTask(this, id);
        retrieveTask.delegate = this;
        retrieveTask.execute();
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void setIsStart(boolean b) {
        isStart = b;
    }
    // When location has been retrieved, launch all other tasks.
    @Override
    public void onFinishedRetrieveLocation(Coordinate coordinate) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isWithinTimeLimit = System.currentTimeMillis() - prefs.getLong("cacheTime", 0) < cacheTotalValidTime;
        final String dataSource = prefs.getString("data_src", "smhi");
        setCacheExists(false);
        if (currentCoordinate == null) {
            this.currentCoordinate = coordinate;
        }

        if (isWithinTimeLimit) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    WeatherDatabase weatherDatabase = Room.databaseBuilder(getApplicationContext(),
                            WeatherDatabase.class, "weathers_db")
                            .fallbackToDestructiveMigration()
                            .build();
                    Weathers weathers = weatherDatabase.daoAccess().fetchWeathersById(requestAdressString(currentCoordinate) + dataSource);
                    if (weathers != null && System.currentTimeMillis() - weathers.getExpirationTime() < cacheValidTime) {
                        setCacheExists(true);
                    }
                    weatherDatabase.close();
                }
            }).start();
            // Found cached data, load it
            if (cacheExists) {
                setCacheExists(false);
                loadCachedData();
            } else {
                // No cached data, load new data.
                this.currentCoordinate = coordinate;
                mainPresenter.loadColorTheme();

                if (dataSource.equals("smhi")) {
                    mainPresenter.requestWeatherData(true);
                } else {
                    mainPresenter.requestWeatherData(false);
                }
            }
        // Time ran out, load new data.
        } else {
            this.currentCoordinate = coordinate;
            mainPresenter.loadColorTheme();

            if (dataSource.equals("smhi")) {
                mainPresenter.requestWeatherData(true);
            } else {
                mainPresenter.requestWeatherData(false);
            }
        }
    }
    @Override
    public void resetWeathers() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (System.currentTimeMillis() - prefs.getLong("cacheTime", 0) > cacheTotalValidTime) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    WeatherDatabase weatherDatabase = Room.databaseBuilder(getApplicationContext(),
                            WeatherDatabase.class, "weathers_db")
                            .fallbackToDestructiveMigration()
                            .build();
                    weatherDatabase.daoAccess().deleteAllWeathers();
                    weatherDatabase.close();
                    prefs.edit().putLong("cacheTime", System.currentTimeMillis()).commit();
                }
            }).start();
        }
    }

    @Override
    public void addWeather(final List<DayItem> dayList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String id = requestAdressString(currentCoordinate) + prefs.getString("data_src", "smhi");
                Weathers weathers = new Weathers();
                weathers.setWeatherId(id);
                weathers.setDayList(dayList);
                weathers.setExpirationTime(System.currentTimeMillis());

                WeatherDatabase weatherDatabase = Room.databaseBuilder(getApplicationContext(),
                        WeatherDatabase.class, "weathers_db")
                        .fallbackToDestructiveMigration()
                        .build();
                weatherDatabase.daoAccess().insertSingleWeather(weathers);
                weatherDatabase.close();
            }
        }).start();
    }

    @Override
    public void onFailureRetrieveLocationn() {
        showToast(getResources().getString(R.string.weather_error));
    }

    @Override
    public void showLocationError() {
        cityText.setText("");
        mainView.setVisibility(View.INVISIBLE);
        sheetLayout.setVisibility(View.INVISIBLE);
        locationErrorTitle.setVisibility(View.VISIBLE);
        locationErrorBody.setVisibility(View.VISIBLE);
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
    public void onBackPressed() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else {
            finish();
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
        if (System.currentTimeMillis() - preferences.getLong("reloadTime", 0) > reloadValidTime) {
            mainPresenter.updateLocationAndUI();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                updateDrawerItems();
                drawer.setSelection(selectedDrawerPosition);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case 2:
                 drawer.setSelection(selectedDrawerPosition);
                break;
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_stats:
                Intent intent= new Intent(MainActivity.this, GraphsActivity.class);
                intent.putExtra("dayList", (Serializable) currentDayList);
                startActivity(intent);
                break;
            case R.id.menu_map:
                Intent intent2 = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent2);
                break;
        }
        return true;
    }

    @Override
    public void onFinishedRetrieveDatabaseLocations(List<Locations> locationList) {
        locList.clear();
        locList.addAll(locationList);
        getDrawerMenuItems();
    }

    @Override
    public void onFailureRetrieveDatabaseLocations() {
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

    @Override
    public void onFinishedRetrieveDatabaseWeathers(Weathers result) {
        showRefresh(false);
        if (Calendar.getInstance().getTime().getTime() - result.getDayList().get(0).getHourList().get(0).getDate().getTime() > 30 * 60 * 1000) {
            result.getDayList().get(0).getHourList().remove(0);
        }
        if (isStart) updateWeatherUI(result.getDayList(), requestAdressString(getCurrentCoordinate()), true);
        else updateWeatherUI(result.getDayList(), requestAdressString(getCurrentCoordinate()), false);
        isStart = false;
    }

    @Override
    public void onFailureRetrieveDatabaseWeathers() {

    }

    @Override
    public void onFinishedRemoveDatabaseWeathers(String identifier) {

    }

    @Override
    public void onFailureRemoveDatabaseWeathers() {

    }
}

