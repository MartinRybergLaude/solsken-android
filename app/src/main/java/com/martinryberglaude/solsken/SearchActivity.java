package com.martinryberglaude.solsken;

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
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.martinryberglaude.solsken.data.LocationItem;
import com.martinryberglaude.solsken.database.LocationDatabase;
import com.martinryberglaude.solsken.database.Locations;
import com.martinryberglaude.solsken.interfaces.GetPhotonDataService;
import com.martinryberglaude.solsken.interfaces.SearchContract;
import com.martinryberglaude.solsken.model.FormatPhotonDataAsyncTask;
import com.martinryberglaude.solsken.networkPHOTON.PhotonRetroLocations;
import com.martinryberglaude.solsken.networkPHOTON.PhotonRetrofitClientInstance;
import com.martinryberglaude.solsken.view.RecyclerViewAdapterLocations;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchContract.FormatLocationIntractor.OnFinishedListener, SearchContract.LocationItemClickListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private Toolbar toolbar;
    private EditText searchEdittext;
    private Call<PhotonRetroLocations> call;
    private RecyclerViewAdapterLocations adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<LocationItem> recyclerViewList = new ArrayList<>();
    private boolean initRecyclerview = true;

    private static final String DATABASE_NAME = "locations_db";

    private String searchString;

    private TextView noLocTitle;
    private TextView noLocBody;

    private boolean hasCancelled;

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
                    hasCancelled = false;
                    if (call != null) {
                        call.cancel();
                    }
                    noLocTitle.setVisibility(View.INVISIBLE);
                    noLocBody.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    searchString = s.toString();
                    handler.removeCallbacks(dataTask);
                    handler.postDelayed(dataTask, 2000);
                } else {
                    if (call != null) {
                        call.cancel();
                        Log.d("cancel", "done");
                    }
                    hasCancelled = true;
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
    }

    Runnable dataTask = new Runnable() {
        @Override
        public void run() {
            if (!hasCancelled) {
                getPhotonData(searchString);
            }
            hasCancelled = false;
        }
    };

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

    @Override
    public void onBackPressed() {
        finish();
    }
    private void getPhotonData(String searchString) {
        GetPhotonDataService service = PhotonRetrofitClientInstance.getRetrofitInstance().create(GetPhotonDataService.class);
        call = service.getData(searchString, 5);
        call.enqueue(new Callback<PhotonRetroLocations>() {
            @Override
            public void onResponse(Call<PhotonRetroLocations> call, Response<PhotonRetroLocations> response) {
                if (response.body() != null) {
                    if (!call.isCanceled()) {
                        FormatPhotonDataAsyncTask formatAsyncTask = new FormatPhotonDataAsyncTask(SearchActivity.this);
                        formatAsyncTask.execute(response);
                    }
                } else {
                    showToast(getResources().getString(R.string.search_error));
                }
            }
            @Override
            public void onFailure(Call<PhotonRetroLocations> call, Throwable t) {
                if (!call.isCanceled()) {
                    showToast(getResources().getString(R.string.search_error));
                    progressBar.setVisibility(View.INVISIBLE);

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
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
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
    private void setAlreadyExists(boolean b) {
        alreadyExists = b;
    }
    private boolean alreadyExists = false;
    @Override
    public void onItemClick(final LocationItem locationItem) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Locations location = new Locations();
                location.setLocationName(locationItem.getCityString());
                location.setLocationCountry(locationItem.getCountryString());
                location.setLocationLat(locationItem.getLat());
                location.setLocationLon(locationItem.getLon());
                LocationDatabase locationDatabase = Room.databaseBuilder(getApplicationContext(),
                        LocationDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
                List<Locations> locations = locationDatabase.daoAccess().fetchAllLocations();

                for (Locations loc : locations) {
                    if (loc.getLocationName().equals(location.getLocationName())) {
                        setAlreadyExists(true);
                    }
                }
                if (!alreadyExists) {
                    locationDatabase.daoAccess().insertSingleLocation(location);
                    locationDatabase.close();
                    finish();
                } else {
                    locationDatabase.close();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (alreadyExists) {
            showToast(getString(R.string.location_already_exists));
            setAlreadyExists(false);
        }
    }

}
