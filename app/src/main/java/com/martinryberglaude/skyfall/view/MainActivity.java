package com.martinryberglaude.skyfall.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.martinryberglaude.skyfall.PermissionActivity;
import com.martinryberglaude.skyfall.interfaces.MainContract;
import com.martinryberglaude.skyfall.R;
import com.martinryberglaude.skyfall.data.TimeOfDay;
import com.martinryberglaude.skyfall.data.Coordinate;
import com.martinryberglaude.skyfall.data.EventItem;
import com.martinryberglaude.skyfall.data.ListItem;
import com.martinryberglaude.skyfall.model.RequestWeatherModel;
import com.martinryberglaude.skyfall.presenter.MainPresenter;

import java.io.IOException;
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

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private MainPresenter mainPresenter;
    private ActionBar actionBar;
    private LinearLayout layout;
    private ScrollView scrollView;
    private Window window;
    private TextView temperatureText;
    private TextView wsymb2Text;
    private SwipeRefreshLayout pullToRefresh;
    private List<ListItem> recyclerViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if GPS permission has been granted
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasPermission()) {
            // Launch permission activity
            Intent intent = new Intent(MainActivity.this, PermissionActivity.class);
            this.startActivity(intent);
            finish();
            return;
        }

        layout = findViewById(R.id.display);
        scrollView = findViewById(R.id.scroll_view);
        window = getWindow();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        temperatureText = findViewById(R.id.title_temperature);
        wsymb2Text = findViewById(R.id.title_wsymb2);
        pullToRefresh = findViewById(R.id.refresh);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        mainPresenter = new MainPresenter(this, new RequestWeatherModel());

        mainPresenter.loadColorTheme();
        mainPresenter.requestWeatherData();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainPresenter.requestWeatherData();
            }
        });
    }
    @Override
    public void setColorTheme(TimeOfDay timeOfDay) {
        // Is sunset
        if (timeOfDay == TimeOfDay.SUNSET) {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.background_sunrise));
            window.setStatusBarColor(getResources().getColor(R.color.sunriseColor1));
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.sunriseColor1)));
        }
        // Is sunrise
        else if (timeOfDay == TimeOfDay.SUNRISE) {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.background_sunrise));
            window.setStatusBarColor(getResources().getColor(R.color.sunriseColor1));
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.sunriseColor1)));
        }
        // Is night
        else if (timeOfDay == TimeOfDay.NIGHT){
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.background_night));
            window.setStatusBarColor(getResources().getColor(R.color.nightColor1));
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.nightColor1)));
        }
        // Is day
        else if (timeOfDay == TimeOfDay.DAY) {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.background_day));
            window.setStatusBarColor(getResources().getColor(R.color.dayColor1));
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dayColor1)));
        }
    }

    @Override
    public void showProgressBar(boolean b) {

    }

    @Override
    public void setToolbarTitle(String toolbarTitle) {
        actionBar.setTitle(toolbarTitle);
    }

    @Override
    public void initWeatherUI(final List<ListItem> itemList) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                EventItem event = (EventItem) itemList.get(1);
                wsymb2Text.setText(event.getWsymb2String());
                temperatureText.setText(event.getTemperatureString());
                recyclerViewList.addAll(itemList);
                adapter = new RecyclerViewAdapter(MainActivity.this, recyclerViewList);
                adapter.setHasStableIds(true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                recyclerView.setAdapter(adapter);
            }
        }, 160);
    }

    @Override
    public void updateWeatherUI(final List<ListItem> itemList) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                EventItem event = (EventItem) itemList.get(1);
                wsymb2Text.setText(event.getWsymb2String());
                temperatureText.setText(event.getTemperatureString());
                recyclerViewList.clear();
                recyclerViewList.addAll(itemList);
                adapter.notifyDataSetChanged();
            }
        }, 160);

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(MainActivity.this, message,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public Coordinate requestCoordinate() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return new Coordinate(location.getLongitude(),location.getLatitude());
    }
    @Override
    public String requestAdressString(Coordinate coordinate) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(coordinate.getLat(), coordinate.getLon(), 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else returns null
            if (city == null) return knownName;
            else return city + ", " + state;
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
}

