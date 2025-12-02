package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.data.ResourceProvider
import com.example.playlistmaker.domain.db.FavoritesInteractor
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.ui.search.mappers.toDomain
import com.example.playlistmaker.ui.search.models.SongUi
import com.example.playlistmaker.ui.settings.SingleLiveEvent
import com.example.playlistmaker.utils.toMinutesAndSeconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private val playerUiState = MutableLiveData(PlayerUiState())
    val observeUiState: LiveData<PlayerUiState> = playerUiState

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlist: LiveData<List<Playlist>> = _playlists

    private val _addToPlaylistStatus = MutableLiveData<String>()
    val addToPlaylistStatus: LiveData<String> = _addToPlaylistStatus

    private val _closeBottomSheet = SingleLiveEvent<Unit>()
    val closeBottomSheet: LiveData<Unit> = _closeBottomSheet

    var currentTrack: SongUi? = null
    private var currentTrackUrl: String? = null


    init {
        playerInteractor.onStateChange = { state ->
            val current = playerUiState.value ?: PlayerUiState()
            playerUiState.postValue(current.copy(playerState = state))
        }
        playerInteractor.onUpdateTime = { time ->
            val current = playerUiState.value ?: PlayerUiState()
            if (time == 0 || current.playerState != PlayerState.DEFAULT) {
                playerUiState.postValue(current.copy(playTime = time.toMinutesAndSeconds()))
            }

        }
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { list ->
                _playlists.postValue(list)
            }
        }
    }

    fun setTrack(track: SongUi) {
        currentTrackUrl = track.previewUrl
        viewModelScope.launch {
            val favoritesIds = favoritesInteractor.favoriteSongsIds()
            val isLiked = favoritesIds.contains(track.trackId)
            currentTrack = track.copy(isLiked = isLiked)
            val current = playerUiState.value ?: PlayerUiState()
            playerUiState.postValue(current.copy(isLiked = isLiked))
        }
    }

    fun onPlayPauseClick() {
        currentTrackUrl?.let { playerInteractor.playbackControl(it) }
    }

    fun onLikeClick() {
        val track = currentTrack ?: return
        val current = playerUiState.value ?: PlayerUiState()
        val isCurrentlyLiked = current.isLiked
        viewModelScope.launch {
            if (!isCurrentlyLiked) {
                favoritesInteractor.addFavoriteSong(track.toDomain())
            } else {
                favoritesInteractor.deleteFavoriteSong(track.toDomain())
            }
        }
        val updated = playerUiState.value ?: PlayerUiState()
        playerUiState.postValue(updated.copy(isLiked = !isCurrentlyLiked))
        currentTrack = track.copy(isLiked = !isCurrentlyLiked)
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    fun onPause() {
        playerInteractor.pause()
    }

    fun addSongToPlaylist(playlistId: Int) {
        val song = currentTrack ?: return
        viewModelScope.launch(Dispatchers.IO) {

            val playlist = playlistInteractor.getPlayListById(playlistId)
            val added = playlistInteractor.addTrackToPlaylist(playlistId, song.toDomain())
            val name = playlist!!.name
            if (added) {
                _addToPlaylistStatus.postValue(
                    resourceProvider.getString(
                        R.string.added_to_pl,
                        name
                    )
                )
                _closeBottomSheet.postValue(Unit)
            } else {
                _addToPlaylistStatus.postValue(
                    resourceProvider.getString(
                        R.string.alrdy_added_to_pl,
                        name
                    )
                )
            }
        }
    }


}