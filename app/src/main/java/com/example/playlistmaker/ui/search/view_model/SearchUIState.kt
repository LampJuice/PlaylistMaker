package com.example.playlistmaker.ui.search.view_model

import com.example.playlistmaker.domain.search.models.Song

sealed class SearchUIState {
    object Initial : SearchUIState()
    data class ShowHistory(val history: List<Song>) : SearchUIState()
    object ShowEmptyPlaceholder : SearchUIState()
    object ShowNoNetworkPlaceholder : SearchUIState()
    data class ShowSearchResults(val results: List<Song>) : SearchUIState()
    object Typing : SearchUIState()
    object Loading : SearchUIState()
    object ClearInput : SearchUIState()
}