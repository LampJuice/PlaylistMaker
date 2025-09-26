package com.example.playlistmaker.di

import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val UiModule = module {
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { PlayerViewModel(get()) }
}