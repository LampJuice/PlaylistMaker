package com.example.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.ui.search.mappers.toDomain
import com.example.playlistmaker.ui.search.models.SongUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(protected val interactor: PlaylistInteractor) : ViewModel() {

    protected val plLiveData: MutableLiveData<CreatePlaylistState> =
        MutableLiveData(CreatePlaylistState())
    val statePlLiveData: LiveData<CreatePlaylistState> = plLiveData


    fun updateName(name: String) {
        plLiveData.value = plLiveData.value?.copy(name = name, isCreateEnabled = name.isNotBlank())
    }

    fun updateDescription(desc: String) {
        plLiveData.value = plLiveData.value?.copy(description = desc)
    }

    fun updateCover(path: String?) {
        val path = path ?: return
        viewModelScope.launch {
            val savedPath = interactor.saveCover(path)
            savedPath?.let {
                plLiveData.postValue(plLiveData.value?.copy(coverPath = it))
            }
        }
    }

    open fun savePlaylist(song: SongUi? = null, onSaved: (String) -> Unit) {
        val s = plLiveData.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val newPlaylistId = interactor.createPlaylist(s.name, s.description, s.coverPath)
            song?.let {
                interactor.addTrackToPlaylist(newPlaylistId, it.toDomain())
            }
            launch(Dispatchers.Main) { onSaved(s.name) }
        }
    }
}
