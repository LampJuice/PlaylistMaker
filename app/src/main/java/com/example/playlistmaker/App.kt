package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import com.example.playlistmaker.domain.api.ThemeInteractor

class App : Application() {

    lateinit var themeInteractor: ThemeInteractor
        private set

    override fun onCreate() {
        super.onCreate()

        Creator.init(this)


        themeInteractor = Creator.provideThemeInteractor()
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
        themeInteractor.applyTheme(darkTheme)
    }
}