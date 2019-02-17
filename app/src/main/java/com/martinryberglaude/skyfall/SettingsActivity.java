package com.martinryberglaude.skyfall;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.martinryberglaude.skyfall.data.CurrentTimeOfDay;
import com.martinryberglaude.skyfall.data.TimeOfDay;
import com.martinryberglaude.skyfall.model.MainModel;
import com.martinryberglaude.skyfall.view.MainActivity;
import com.martinryberglaude.skyfall.view.MainPreferenceFragment;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.preference_layout);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new MainPreferenceFragment()).commit();
        Toolbar actionbar = findViewById(R.id.toolbar_prefs);
        if (actionbar != null) {
            actionbar.setNavigationIcon(R.drawable.ic_arrow_back);
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        boolean hasChangedTheme = preferences.getBoolean("hasChangedTheme", false);
        if (hasChangedTheme) {
            preferences.edit().putBoolean("hasChangedTheme", false).commit();
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
        String colorTheme = sharedPreferences.getString("theme", "day");
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
            }
        }
    }
}
