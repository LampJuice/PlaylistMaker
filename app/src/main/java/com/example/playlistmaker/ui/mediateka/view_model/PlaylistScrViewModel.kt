package com.example.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.data.ResourceProvider
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.ui.search.mappers.toUi
import com.example.playlistmaker.ui.search.models.SongUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistScrViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val externalNavigator: ExternalNavigator,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _state = MutableLiveData<PlaylistScrState>()
    val state: LiveData<PlaylistScrState> = _state

    fun setPlaylist(id: Int) {
        _state.postValue(PlaylistScrState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = playlistInteractor.getPlayListById(id)
            if (playlist == null) {
                _state.postValue(PlaylistScrState.Error)
                return@launch
            }

            val songs = playlistInteractor.getSavedSongsByIds(playlist.songIds)
            val songsUi = songs.map { it.toUi() }.reversed()

            val totalMillis = songs.sumOf { it.trackTimeMillis }
            val duration = SimpleDateFormat("mm", Locale.getDefault()).format(totalMillis)

            _state.postValue(
                PlaylistScrState.Content(
                    playlist = playlist,
                    song = songsUi,
                    duration = duration,
                    trackCount = songs.size
                )
            )
        }
    }

    fun removeSongFromPlaylist(songId: Int) {
        val playlist = (state.value as? PlaylistScrState.Content)?.playlist ?: return
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.removeSongFromPlaylist(playlist.id, songId)
            setPlaylist(playlist.id)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            val playlistId =
                (state.value as? PlaylistScrState.Content)?.playlist?.id ?: return@launch
            playlistInteractor.deletePlaylist(playlistId)
        }
    }

    fun sharePlaylist(playlist: Playlist, songs: List<SongUi>) {

        val builder = StringBuilder()

        builder.append(playlist.name).append("\n")
        if (!playlist.description.isNullOrBlank()) {
            builder.append(playlist.description).append("\n")
        }
        val songCountText = resourceProvider.getQuantityString(
            R.plurals.tracks_count,
            playlist.songCount,
            playlist.songCount

        )
        builder.append(songCountText).append("\n\n")
        songs.forEachIndexed { index, song ->
            builder.append("${index + 1}. ${song.artistName} - ${song.trackName} (${song.trackTime})\n")
        }
        externalNavigator.shareText(builder.toString())
    }

}