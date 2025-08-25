package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.toMinutesAndSeconds

class PlayerViewModel(private val playerInteractor: PlayerInteractor): ViewModel() {
    private val  playerUiState = MutableLiveData(PlayerUiState())
    val observeUiState: LiveData<PlayerUiState> = playerUiState

    private var currentTrackUrl: String? = null

    init {
        playerInteractor.onStateChange = { state ->
            playerUiState.postValue(playerUiState.value?.copy(playerState = state))
        }
        playerInteractor.onUpdateTime = { time ->

            playerUiState.postValue(playerUiState.value?.copy(playTime = time.toMinutesAndSeconds()))
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

    fun omPause(){
        playerInteractor.pause()
    }

    companion object{
        fun getFactory(playerInteractor: PlayerInteractor): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(playerInteractor)
            }
        }
    }
}