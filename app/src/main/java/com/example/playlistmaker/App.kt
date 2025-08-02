package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.ThemeInteractor

class App : Application() {

    lateinit var themeInteractor: ThemeInteractor
        private set

    override fun onCreate() {
        super.onCreate()


        themeInteractor = Creator.provideThemeInteractor(this)
        val isDark = if (themeInteractor.hasSavedTheme()) {
            themeInteractor.isDarkTheme()
        } else {
            val sysDark = isSysInDark()
            themeInteractor.setDarkTheme(sysDark)
            sysDark
        }
        applyTheme(isDark)

    }

    private fun isSysInDark(): Boolean {

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        themeInteractor.setDarkTheme(darkThemeEnabled)
        applyTheme(darkThemeEnabled)
    }

    fun applyTheme(darkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}