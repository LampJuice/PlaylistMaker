package com.example.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoritesInteractor
import com.example.playlistmaker.domain.search.models.Song
import com.example.playlistmaker.ui.search.mappers.toUi
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {

    private val favoritesLiveData = MutableLiveData<FavoritesUiState>()
    val stateFavoritesLiveData: LiveData<FavoritesUiState> = favoritesLiveData

    private val songs = mutableListOf<Song>()


    fun showFavorites() {
        viewModelScope.launch {
            try {
                favoritesInteractor.favoriteSongs()
                    .collect { favorites ->
                        songs.clear()
                        songs.addAll(favorites)
                        val songUi = favorites.map { it.toUi() }
                        if (songs.isEmpty()) {
                            favoritesLiveData.value = FavoritesUiState.ShowEmptyPlaceholder
                        } else {
                            favoritesLiveData.value =
                                FavoritesUiState.ShowFavorites(songUi.toList())
                        }
                    }
            } catch (e: Exception) {
                favoritesLiveData.value = FavoritesUiState.ShowEmptyPlaceholder
            }
        }
    }
}