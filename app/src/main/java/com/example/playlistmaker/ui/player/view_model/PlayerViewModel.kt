package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.toMinutesAndSeconds

class PlayerViewModel(private val playerInteractor: PlayerInteractor): ViewModel() {
    private val playerStateData = MutableLiveData(PlayerState.DEFAULT)
    val observePlayerState: LiveData<PlayerState> = playerStateData

    private val playTime = MutableLiveData("00:00")
    val observePlayTime: LiveData<String> = playTime

    private val isLiked = MutableLiveData(false)
    val observeIsLiked : LiveData<Boolean> = isLiked

    private var currentTrackUrl: String? = null

    init {
        playerInteractor.onStateChange = { state ->
            playerStateData.postValue(state)
        }
        playerInteractor.onUpdateTime = { currentMs ->

            playTime.postValue(currentMs.toLong().toMinutesAndSeconds())


        }
    }

    fun setTrackUrl(url: String) {
        currentTrackUrl = url
    }
    fun onPlayPauseClick() {
        currentTrackUrl?.let { playerInteractor.playbackControl(it) }
    }
    fun onLikeClick() {
        isLiked.postValue(!(isLiked.value ?: false))
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