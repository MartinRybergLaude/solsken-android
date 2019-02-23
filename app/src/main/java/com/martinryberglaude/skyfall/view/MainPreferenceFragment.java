package com.martinryberglaude.skyfall.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.martinryberglaude.skyfall.R;
import com.martinryberglaude.skyfall.SettingsActivity;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

public class MainPreferenceFragment extends androidx.preference.PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        ListPreference preference = findPreference("theme");
        ListPreference preference2 = findPreference("wind");
        ListPreference preference3 = findPreference("rain");
        ListPreference preference4 = findPreference("vis");
        ListPreference preference5 = findPreference("pressure");
        ListPreference preference6 = findPreference("temperature");
        ListPreference preference7 = findPreference("hour");

        preference.setSummary(preference.getEntry());
        preference2.setSummary(preference2.getEntry());
        preference3.setSummary(preference3.getEntry());
        preference4.setSummary(preference4.getEntry());
        preference5.setSummary(preference5.getEntry());
        preference6.setSummary(preference6.getEntry());
        preference7.setSummary(preference7.getEntry());
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
            sharedPreferences.edit().putBoolean("requiresRefresh", true).commit();
        }

        if (key.equals("theme") || key.equals("dark_theme")) {
            sharedPreferences.edit().putBoolean("requiresRefresh", true).commit();
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            getActivity().finish();
            startActivity(intent);
        }
    }
}
