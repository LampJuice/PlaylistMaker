package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        if (prefs.contains(THEME_KEY)) {
            darkTheme = prefs.getBoolean(THEME_KEY, false)
        } else {
            darkTheme = false
            //darkTheme = isSysInDark()

            prefs.edit().putBoolean(THEME_KEY, darkTheme).apply()
        }
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

//        private fun isSysInDark(): Boolean {
//
//        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
//        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
//    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .edit()
            .putBoolean(THEME_KEY, darkThemeEnabled)
            .apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        private const val PREFS_NAME = "app_prefs"
        private const val THEME_KEY = "dark_theme"
    }

}