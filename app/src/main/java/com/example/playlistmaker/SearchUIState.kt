package com.example.playlistmaker

sealed class SearchUIState {
    object Initial : SearchUIState()
    object ShowHistory : SearchUIState()
    object ShowEmptyPlaceholder  : SearchUIState()
    object ShowNoNetworkPlaceholder  : SearchUIState()
    object ShowSearchResults : SearchUIState()
    object Typing : SearchUIState()
}