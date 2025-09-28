package com.example.playlistmaker.di

import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SongsInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.SongsInteractorImpl
import com.example.playlistmaker.domain.settings.ThemeInteractor
import com.example.playlistmaker.domain.settings.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val interactorModule = module {
    factory<ThemeInteractor> { ThemeInteractorImpl(get()) }
    factory<SearchHistoryInteractor> { SearchHistoryInteractorImpl(get()) }
    factory<SongsInteractor> { SongsInteractorImpl(get(), get()) }
    factory<PlayerInteractor> { PlayerInteractorImpl(get(), get()) }
    factory<SharingInteractor> { SharingInteractorImpl(get()) }

    single<Executor> { Executors.newCachedThreadPool() }
}