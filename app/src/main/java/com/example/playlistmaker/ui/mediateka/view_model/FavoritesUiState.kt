package com.example.playlistmaker.ui.mediateka.view_model

import com.example.playlistmaker.ui.search.models.SongUi

sealed class FavoritesUiState {
    object ShowEmptyPlaceholder : FavoritesUiState()
    data class ShowFavorites(val favorites: List<SongUi>) : FavoritesUiState()
}


