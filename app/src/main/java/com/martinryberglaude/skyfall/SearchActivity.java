package com.martinryberglaude.skyfall;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.martinryberglaude.skyfall.data.LocationItem;
import com.martinryberglaude.skyfall.database.LocationDatabase;
import com.martinryberglaude.skyfall.database.Locations;
import com.martinryberglaude.skyfall.interfaces.GetPhotonDataService;
import com.martinryberglaude.skyfall.interfaces.SearchContract;
import com.martinryberglaude.skyfall.model.FormatPhotonDataAsyncTask;
import com.martinryberglaude.skyfall.network.PhotonRetroLocations;
import com.martinryberglaude.skyfall.network.PhotonRetrofitClientInstance;
import com.martinryberglaude.skyfall.view.RecyclerViewAdapterLocations;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchContract.FormatLocationIntractor.OnFinishedListener, SearchContract.LocationItemClickListener {

    private Toolbar toolbar;
    private EditText searchEdittext;
    private Call<PhotonRetroLocations> call;
    private RecyclerViewAdapterLocations adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<LocationItem> recyclerViewList = new ArrayList<>();
    private boolean initRecyclerview = true;

    private static final String DATABASE_NAME = "locations_db";
    private LocationDatabase locationDatabase;

    private String searchString;

    private TextView noLocTitle;
    private TextView noLocBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_search);
        toolbar = findViewById(R.id.toolbar_sch);
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
        noLocTitle = findViewById(R.id.no_location_title_text);
        noLocBody = findViewById(R.id.no_location_body_text);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView = findViewById(R.id.recycler_view_locations);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        searchEdittext = findViewById(R.id.edittext_search);
        searchEdittext.setHintTextColor(getResources().getColor(R.color.textSecondaryInverse));
        searchEdittext.setCursorVisible(true);
        searchEdittext.requestFocus();

        searchEdittext.addTextChangedListener(new TextWatcher() {
            private Handler handler = new Handler();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    noLocTitle.setVisibility(View.INVISIBLE);
                    noLocBody.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    if (call != null) {
                        call.cancel();
                    }
                    searchString = s.toString();
                    handler.removeCallbacks(dataTask);
                    handler.postDelayed(dataTask, 2000);
                } else {
                    noLocTitle.setText(getResources().getString(R.string.search_location_title));
                    noLocBody.setText(getResources().getString(R.string.search_location_body));
                    progressBar.setVisibility(View.GONE);
                    noLocTitle.setVisibility(View.VISIBLE);
                    noLocBody.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        locationDatabase = Room.databaseBuilder(getApplicationContext(),
                LocationDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    Runnable dataTask = new Runnable() {
        @Override
        public void run() {
            getPhotonData(searchString);
        }
    };

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

    private void getPhotonData(String searchString) {
        System.out.println(searchString);
        GetPhotonDataService service = PhotonRetrofitClientInstance.getRetrofitInstance().create(GetPhotonDataService.class);
        call = service.getData(searchString, 5);
        call.enqueue(new Callback<PhotonRetroLocations>() {
            @Override
            public void onResponse(Call<PhotonRetroLocations> call, Response<PhotonRetroLocations> response) {
                if (response.body() != null) {
                    FormatPhotonDataAsyncTask formatAsyncTask = new FormatPhotonDataAsyncTask();
                    formatAsyncTask.delegate = SearchActivity.this;
                    formatAsyncTask.execute(response);
                } else {
                    showToast(getResources().getString(R.string.search_error));
                }
            }
            @Override
            public void onFailure(Call<PhotonRetroLocations> call, Throwable t) {
                if (!call.isCanceled()) {
                    showToast(getResources().getString(R.string.search_error));
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    private void updateRecyclerView(List<LocationItem> locationList) {
        if (locationList.isEmpty()) {
            noLocTitle.setText(getResources().getString(R.string.no_location_title));
            noLocBody.setText(getResources().getString(R.string.no_location_body));
            noLocBody.setVisibility(View.VISIBLE);
            noLocTitle.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        } else if (initRecyclerview) {
            recyclerViewList.addAll(locationList);
            adapter = new RecyclerViewAdapterLocations(SearchActivity.this, recyclerViewList, SearchActivity.this);
            adapter.setHasStableIds(true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            recyclerView.setAdapter(adapter);
            initRecyclerview = false;
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            noLocTitle.setVisibility(View.INVISIBLE);
            noLocBody.setVisibility(View.INVISIBLE);
        } else {
            recyclerViewList.clear();
            recyclerViewList.addAll(locationList);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            noLocTitle.setVisibility(View.INVISIBLE);
            noLocBody.setVisibility(View.INVISIBLE);
        }

    }
    private void showToast(String message) {
        Toast.makeText(SearchActivity.this, message,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFinishedFormatLocations(List<LocationItem> locationList) {
        updateRecyclerView(locationList);
    }

    @Override
    public void onFailureFormatLocations() {
        showToast(getResources().getString(R.string.search_error));
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(final LocationItem locationItem) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Locations location = new Locations();
                location.setLocationName(locationItem.getCityString());
                location.setLocationLat(locationItem.getLat());
                location.setLocationLon(locationItem.getLon());
                locationDatabase.daoAccess().insertSingleLocation(location);
                finish();
            }
        }).start();
    }
}
