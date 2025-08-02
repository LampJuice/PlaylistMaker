package com.example.playlistmaker.ui.search

sealed class SearchUIState {
    object Initial : SearchUIState()
    object ShowHistory : SearchUIState()
    object ShowEmptyPlaceholder  : SearchUIState()
    object ShowNoNetworkPlaceholder  : SearchUIState()
    object ShowSearchResults : SearchUIState()
    object Typing : SearchUIState()
}