package com.example.playlistmaker.domain.settings

interface ThemeInteractor {
    fun hasSavedTheme(): Boolean
    fun isDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)
    fun applyTheme(darkTheme: Boolean)
}