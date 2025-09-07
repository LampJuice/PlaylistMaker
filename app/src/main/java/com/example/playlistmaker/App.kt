package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import com.example.playlistmaker.di.UiModule
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.domain.settings.ThemeInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private val themeInteractor: ThemeInteractor by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, UiModule)
        }

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

    fun applyTheme(darkTheme: Boolean) {
        themeInteractor.applyTheme(darkTheme)
    }
}