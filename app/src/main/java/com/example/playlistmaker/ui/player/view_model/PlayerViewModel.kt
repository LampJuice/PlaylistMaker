package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.utils.toMinutesAndSeconds

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {
    private val playerUiState = MutableLiveData(PlayerUiState())
    val observeUiState: LiveData<PlayerUiState> = playerUiState

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
    }

    fun setTrackUrl(url: String) {
        currentTrackUrl = url
    }

    fun onPlayPauseClick() {
        currentTrackUrl?.let { playerInteractor.playbackControl(it) }
    }

    fun onLikeClick() {
        val current = playerUiState.value ?: PlayerUiState()
        playerUiState.postValue((current.copy(isLiked = !current.isLiked)))
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    fun onPause() {
        playerInteractor.pause()
    }
}