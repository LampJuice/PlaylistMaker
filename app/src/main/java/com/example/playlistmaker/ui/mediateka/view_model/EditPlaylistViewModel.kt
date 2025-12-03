package com.example.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.ui.search.models.SongUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(interactor: PlaylistInteractor) : CreatePlaylistViewModel(interactor) {
    private var playlist: Playlist? = null

    fun initData(playlist: Playlist) {
        this.playlist = playlist
        plLiveData.value = CreatePlaylistState(
            name = playlist.name,
            description = playlist.description.toString(),
            coverPath = playlist.coverPath,
            isCreateEnabled = playlist.name.isNotBlank()
        )
    }

    override fun savePlaylist(song: SongUi?, onSaved: (String) -> Unit) {
        val state = plLiveData.value ?: return
        val newPlaylist = playlist ?: return

        val updated = newPlaylist.copy(
            name = state.name,
            description = state.description,
            coverPath = state.coverPath
        )

        viewModelScope.launch(Dispatchers.IO) {
            interactor.updatePlaylist(updated)

            launch(Dispatchers.Main) { onSaved(state.name) }
        }
    }

}