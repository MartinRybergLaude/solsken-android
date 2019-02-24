package com.martinryberglaude.skyfall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.martinryberglaude.skyfall.view.MainActivity;
import com.martinryberglaude.skyfall.view.MainPreferenceFragment;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.preference_layout);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new MainPreferenceFragment()).commit();
        toolbar = findViewById(R.id.toolbar_prefs);
        applyToolbarTheme();
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleBackPress();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        handleBackPress();
    }

    private void handleBackPress() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean requiresRefresh = preferences.getBoolean("requiresRefresh", false);
        if (requiresRefresh) {
            preferences.edit().putBoolean("requiresRefresh", false).commit();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
        } else {
            finish();
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
