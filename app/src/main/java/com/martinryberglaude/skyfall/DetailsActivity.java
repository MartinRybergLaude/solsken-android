package com.martinryberglaude.skyfall;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.martinryberglaude.skyfall.data.HourItem;
import com.martinryberglaude.skyfall.data.WindDirection;

public class DetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView windIcon;
    private TextView icWindDirectionText;
    private TextView icWindSpeedText;
    private TextView icPressureText;
    private TextView icRainAmount;
    private TextView icVisibilityText;
    private TextView icHumidityText;
    private TextView icGustSpeedText;
    private TextView icCloudCoverText;
    private TextView icFeelsLikeText;
    private RelativeLayout weatherCard;

    private HourItem hourItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_details);


        hourItem = (HourItem) getIntent().getSerializableExtra("hour");

        toolbar = findViewById(R.id.toolbar_dts);
        applyToolbarTheme();
        TextView toolbarTitle = findViewById(R.id.title_dts);
        toolbarTitle.setText(hourItem.getHourString());
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.primaryColor, typedValue, true);
        @ColorInt int color = typedValue.data;
        weatherCard = findViewById(R.id.weather_card);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            weatherCard.setOutlineAmbientShadowColor(color);
            weatherCard.setOutlineSpotShadowColor(color);
        }

        windIcon = findViewById(R.id.ic_wind_icon);
        icWindDirectionText = findViewById(R.id.ic_wind_direction);
        icWindSpeedText = findViewById(R.id.ic_wind_speed);
        icPressureText = findViewById(R.id.ic_air_pressure);
        icRainAmount = findViewById(R.id.ic_rain_amount);
        icVisibilityText = findViewById(R.id.ic_visibility);
        icHumidityText = findViewById(R.id.ic_humidity);
        icGustSpeedText = findViewById(R.id.ic_gust_speed);
        icCloudCoverText = findViewById(R.id.ic_cloud_cover);
        icFeelsLikeText = findViewById(R.id.ic_feels_like);

        initWeatherUI();
    }
    private void initWeatherUI() {
        setWindDirectionUI(hourItem.getWindDirection());
        icWindSpeedText.setText(hourItem.getWindSpeedString());
        icPressureText.setText(hourItem.getPressureString());
        icRainAmount.setText(hourItem.getRainAmountString());
        icVisibilityText.setText(hourItem.getVisbilityString());
        icHumidityText.setText(hourItem.getHumidityString());
        icGustSpeedText.setText(hourItem.getGustSpeedString());
        icCloudCoverText.setText(hourItem.getCloudCoverString());
        icFeelsLikeText.setText(hourItem.getFeelsLikeString());
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
