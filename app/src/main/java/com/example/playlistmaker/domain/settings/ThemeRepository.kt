package com.example.playlistmaker.domain.settings

interface ThemeRepository {
    fun isDarkTheme(): Boolean
    fun storeTheme(enabled: Boolean)
    fun hasSavedTheme(): Boolean
    fun applyTheme(darkTheme: Boolean)
}