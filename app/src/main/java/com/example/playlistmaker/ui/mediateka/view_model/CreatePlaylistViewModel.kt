package com.example.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.ui.search.mappers.toDomain
import com.example.playlistmaker.ui.search.models.SongUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    private val plLiveData = MutableLiveData(CreatePlaylistState())
    val statePlLiveData: LiveData<CreatePlaylistState> = plLiveData

    fun updateName(name: String) {
        plLiveData.value = plLiveData.value?.copy(name = name, isCreateEnabled = name.isNotBlank())
    }

    fun updateDescription(desc: String) {
        plLiveData.value = plLiveData.value?.copy(description = desc)
    }

    fun updateCover(path: String?) {
        viewModelScope.launch {
            val savedPath = interactor.saveCover(path!!)
            savedPath?.let {
                plLiveData.postValue(plLiveData.value?.copy(coverPath = it))
            }
        }

    }

    fun savePlaylist(song: SongUi? = null, onSaved: (String) -> Unit) {
        val s = plLiveData.value ?: return

        viewModelScope.launch(Dispatchers.IO) {
            val newPlaylistId = interactor.createPlaylist(s.name, s.description, s.coverPath)
            var newPlaylist: Playlist? = null



            song?.let {

                interactor.addTrackToPlaylist(newPlaylistId, it.toDomain())
                newPlaylist = interactor.getPlayListById(newPlaylistId)
            }

            launch(Dispatchers.Main) { onSaved(s.name) }
        }
    }

}

data class CreatePlaylistState(
    val name: String = "",
    val description: String = "",
    val coverPath: String? = null,
    val isCreateEnabled: Boolean = false
)