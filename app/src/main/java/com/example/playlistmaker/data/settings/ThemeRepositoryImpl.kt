package com.example.playlistmaker.data.settings

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.StorageClient
import com.example.playlistmaker.domain.settings.ThemeRepository

class ThemeRepositoryImpl(private val storage: StorageClient<Boolean>) : ThemeRepository {
    override fun isDarkTheme(): Boolean = storage.getData() ?: false

    override fun storeTheme(enabled: Boolean) = storage.storeData(enabled)

    override fun hasSavedTheme(): Boolean = storage.getData() != null

    override fun applyTheme(darkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}