package com.example.playlistmaker.ui.search.view_model

import com.example.playlistmaker.ui.search.models.SongUi

sealed class SearchUIState {
    object Initial : SearchUIState()
    data class ShowHistory(val history: List<SongUi>) : SearchUIState()
    object ShowEmptyPlaceholder : SearchUIState()
    object ShowNoNetworkPlaceholder : SearchUIState()
    data class ShowSearchResults(val results: List<SongUi>) : SearchUIState()
    object Typing : SearchUIState()
    object Loading : SearchUIState()
    object ClearInput : SearchUIState()
}