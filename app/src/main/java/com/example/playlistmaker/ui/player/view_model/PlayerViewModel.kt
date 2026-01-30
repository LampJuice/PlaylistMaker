package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.data.ResourceProvider
import com.example.playlistmaker.domain.db.FavoritesInteractor
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.service.PlayerController
import com.example.playlistmaker.ui.search.mappers.toDomain
import com.example.playlistmaker.ui.search.models.SongUi
import com.example.playlistmaker.ui.settings.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private var isPlayerScreenVisible = false

    private val playerUiState = MutableLiveData(PlayerUiState())
    val observeUiState: LiveData<PlayerUiState> = playerUiState

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlist: LiveData<List<Playlist>> = _playlists

    private val _addToPlaylistStatus = MutableLiveData<String>()
    val addToPlaylistStatus: LiveData<String> = _addToPlaylistStatus

    private val _closeBottomSheet = SingleLiveEvent<Unit>()
    val closeBottomSheet: LiveData<Unit> = _closeBottomSheet

    private var playerController: PlayerController? = null

    var currentTrack: SongUi? = null
    private var currentTrackUrl: String? = null


    init {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { list ->
                _playlists.postValue(list)
            }
        }
    }

    fun setPlayerController(controller: PlayerController) {
        playerController = controller

        viewModelScope.launch {
            controller.playerState.collect { state ->
                if (state != PlayerState.PLAYING) {
                    playerController?.stopForegroundMode()
                }
            }
        }

        if (isPlayerScreenVisible) {
            playerController?.stopForegroundMode()
        }
    }

    fun onPlayPauseClick() {
        when (playerController?.playerState?.value) {
            PlayerState.DEFAULT -> currentTrack?.let {
                playerController?.prepareAndPlay(
                    it.previewUrl.orEmpty()
                )
            }

            PlayerState.PLAYING -> playerController?.pause()
            PlayerState.PAUSED, PlayerState.PREPARED -> playerController?.play()
            else -> Unit
        }
    }

    fun onScreenResumed() {
        isPlayerScreenVisible = true
        playerController?.stopForegroundMode()


    }

    fun onScreenStopped() {
        isPlayerScreenVisible = false

        if (playerController?.playerState?.value == PlayerState.PLAYING) {
            playerController?.startForegroundMode()
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


    fun onScreenClosed() {
        playerController?.pause()
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