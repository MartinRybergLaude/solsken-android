package com.martinryberglaude.solsken.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.martinryberglaude.solsken.R;
import com.martinryberglaude.solsken.SettingsActivity;

import java.util.Locale;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

public class MainPreferenceFragment extends androidx.preference.PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        ListPreference preference = findPreference("data_src");
        ListPreference preference2 = findPreference("theme");
        ListPreference preference3 = findPreference("wind");
        ListPreference preference4 = findPreference("rain");
        ListPreference preference5 = findPreference("vis");
        ListPreference preference6 = findPreference("pressure");
        ListPreference preference7 = findPreference("temperature");
        ListPreference preference8 = findPreference("hour");

        preference.setSummary(preference.getEntry());
        preference2.setSummary(preference2.getEntry());
        preference3.setSummary(preference3.getEntry());
        preference4.setSummary(preference4.getEntry());
        preference5.setSummary(preference5.getEntry());
        preference6.setSummary(preference6.getEntry());
        preference7.setSummary(preference7.getEntry());
        preference8.setSummary(preference8.getEntry());

        Preference invalidate = findPreference("invalidate");
        if (invalidate != null) {
            invalidate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    prefs.edit().putBoolean("requiresRefresh", true).putBoolean("requiresUIRefresh", true).commit();
                    Toast.makeText(getActivity(), getResources().getString(R.string.cache_invalidated),
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        SwitchPreference compassPref = findPreference("relative_wind");
        PackageManager manager = getContext().getPackageManager();
        boolean hasAccelerometer = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        boolean hasMagnetometer = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);

        if (!hasAccelerometer || !hasMagnetometer) {
            if (compassPref != null) {
                compassPref.setEnabled(false);
                compassPref.setVisible(false);
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());
        }
        if (key.equals("temperature") || key.equals("wind") || key.equals("rain") || key.equals("pressure") || key.equals("vis") || key.equals("hour")) {
            sharedPreferences.edit().putBoolean("requiresRefresh", true).putBoolean("requiresUIRefresh", true).commit();
        }
        if (key.equals("theme") || key.equals("dark_theme")) {
            sharedPreferences.edit().putBoolean("requiresUIRefresh", true).commit();
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            getActivity().finish();
            startActivity(intent);
        }
        if (key.equals("data_src")) {
            sharedPreferences.edit().putBoolean("requiresUIRefresh", true).commit();
        }
    }
}
