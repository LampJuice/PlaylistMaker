package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.ThemeInteractor
import com.example.playlistmaker.domain.settings.ThemeRepository

class ThemeInteractorImpl(private val repository: ThemeRepository) : ThemeInteractor {
    override fun hasSavedTheme(): Boolean = repository.hasSavedTheme()
    override fun isDarkTheme() = repository.isDarkTheme()
    override fun setDarkTheme(enabled: Boolean) = repository.storeTheme(enabled)
    override fun applyTheme(darkTheme: Boolean) = repository.applyTheme(darkTheme)
}