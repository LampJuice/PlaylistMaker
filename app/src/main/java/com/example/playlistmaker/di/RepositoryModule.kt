package com.example.playlistmaker.di

import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.SongsRepositoryImpl
import com.example.playlistmaker.data.settings.ThemeRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.SongsRepository
import com.example.playlistmaker.domain.settings.ThemeRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<ThemeRepository> { ThemeRepositoryImpl(get(named("themeStorage"))) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get(named("historyStorage"))) }
    single<PlayerRepository> { PlayerRepositoryImpl(get()) }
    single<SongsRepository> { SongsRepositoryImpl(get()) }
}