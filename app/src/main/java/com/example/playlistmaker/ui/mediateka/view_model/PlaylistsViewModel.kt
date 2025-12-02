package com.example.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistInteractor) : ViewModel() {
    private val _state = MutableLiveData<PlaylistsState>()
    val state: LiveData<PlaylistsState> = _state

    init {
        observePlaylists()
    }

    private fun observePlaylists() {
        viewModelScope.launch {
            interactor.getPlaylists().collect { list ->
                _state.postValue(
                    if (list.isEmpty()) PlaylistsState.Empty
                    else PlaylistsState.Content(list)
                )
            }
        }
    }

}

