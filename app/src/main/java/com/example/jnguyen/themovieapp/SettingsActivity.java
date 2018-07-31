package com.example.jnguyen.themovieapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupThemeSharedPreferences();
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(R.string.pref_theme_key)){
            setupThemeSharedPreferences();
        }
        recreate();
    }

    public void setupThemeSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getString(getString(R.string.pref_theme_key),getString(R.string.pref_theme_light_value)) == getString(R.string.pref_theme_light_value)){
            Log.d("theme","light");
            this.setTheme(R.style.AppTheme);
        } else {
            Log.d("theme","dark");
            this.setTheme(R.style.TheMovieDbDark);
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }
}
