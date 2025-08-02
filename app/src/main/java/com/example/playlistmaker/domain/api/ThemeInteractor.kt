package com.example.playlistmaker.domain.api

interface ThemeInteractor {
    fun hasSavedTheme(): Boolean
    fun isDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)
}