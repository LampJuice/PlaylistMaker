package com.example.playlistmaker.di

import com.example.playlistmaker.data.db.FavoritesRepositoryImpl
import com.example.playlistmaker.data.db.PlaylistRepositoryImpl
import com.example.playlistmaker.data.mappers.PlaylistDbMapper
import com.example.playlistmaker.data.mappers.SongDbMapper
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.SongsRepositoryImpl
import com.example.playlistmaker.data.settings.ThemeRepositoryImpl
import com.example.playlistmaker.domain.db.FavoritesRepository
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.SongsRepository
import com.example.playlistmaker.domain.settings.ThemeRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<ThemeRepository> { ThemeRepositoryImpl(get(named("themeStorage"))) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get(named("historyStorage"))) }
    factory<PlayerRepository> { PlayerRepositoryImpl(get()) }
    single<SongsRepository> { SongsRepositoryImpl(get(), get()) }
    factory { SongDbMapper() }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get(), get()) }
    single { PlaylistDbMapper(get()) }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()

        )
    }
}