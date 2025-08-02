package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeRepositoryImpl(private val sharedPreferences: SharedPreferences) : ThemeRepository {
    override fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(THEME_KEY, false)
    }

    override fun setDarkTheme(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(THEME_KEY, enabled).apply()
    }

    override fun hasSavedTheme(): Boolean {
        return sharedPreferences.contains(THEME_KEY)
    }

    companion object {
        private const val THEME_KEY = "dark_theme"
    }
}