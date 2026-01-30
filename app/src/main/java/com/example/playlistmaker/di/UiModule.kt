package com.example.playlistmaker.di

import com.example.playlistmaker.ui.mediateka.view_model.CreatePlaylistViewModel
import com.example.playlistmaker.ui.mediateka.view_model.EditPlaylistViewModel
import com.example.playlistmaker.ui.mediateka.view_model.FavoriteViewModel
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistScrViewModel
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val UiModule = module {
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { (scope: CoroutineScope) ->
        PlayerViewModel(
            get(),
            get(),
            get()
        )
    }
    viewModel { FavoriteViewModel(get()) }
    viewModel { PlaylistsViewModel(get()) }
    viewModel { CreatePlaylistViewModel(get()) }
    viewModel { PlaylistScrViewModel(get(),get(),get()) }
    viewModel { EditPlaylistViewModel(get()) }
}