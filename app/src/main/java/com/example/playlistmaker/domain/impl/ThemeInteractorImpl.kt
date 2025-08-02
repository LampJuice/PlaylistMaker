package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeInteractorImpl(private val repository: ThemeRepository) : ThemeInteractor {
    override fun hasSavedTheme(): Boolean = repository.hasSavedTheme()
    override fun isDarkTheme() = repository.isDarkTheme()
    override fun setDarkTheme(enabled: Boolean) = repository.setDarkTheme(enabled)
}