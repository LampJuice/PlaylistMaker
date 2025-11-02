package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.data.player.PlayerTimer
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.models.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerInteractorImpl(
    private val repository: PlayerRepository,
    private val coroutineScope: CoroutineScope
) : PlayerInteractor {

    private val timer = PlayerTimer(repository, coroutineScope = coroutineScope)
    override var playerState: PlayerState = PlayerState.DEFAULT
        private set

    override var onUpdateTime: ((Int) -> Unit)? = null
    override var onStateChange: ((PlayerState) -> Unit)? = null

    init {
        timer.onTick = { ms -> onUpdateTime?.invoke(ms) }
    }

    private var isPreparing = false

    override fun prepare(url: String) {
        if (isPreparing) return
        isPreparing = true

        playerState = PlayerState.DEFAULT
        onStateChange?.invoke(playerState)
        onUpdateTime?.invoke(0)
        repository.onPreparedListener = {
            playerState = PlayerState.PREPARED
            onStateChange?.invoke(playerState)
            isPreparing = false
            play()
        }
        repository.onCompletionListener = {
            timer.stop()
            playerState = PlayerState.DEFAULT
            onStateChange?.invoke(playerState)
            coroutineScope.launch {
                delay(50)
                onUpdateTime?.invoke(0)
            }
        }
        repository.setDataSource(url)
    }

    override fun play() {
        if (playerState == PlayerState.PREPARED || playerState == PlayerState.PAUSED) {
            repository.start()
            playerState = PlayerState.PLAYING
            onStateChange?.invoke(playerState)
            timer.start()
        }
    }

    override fun pause() {
        if (playerState == PlayerState.PLAYING) {
            repository.pause()
            playerState = PlayerState.PAUSED
            onStateChange?.invoke(playerState)
            timer.stop()
        }
    }

    override fun release() {
        timer.stop()

        repository.release()
        playerState = PlayerState.DEFAULT
        onStateChange?.invoke(playerState)
        isPreparing = false
    }

    override fun playbackControl(url: String) {
        when (playerState) {
            PlayerState.DEFAULT -> prepare(url)
            PlayerState.PREPARED, PlayerState.PAUSED -> play()
            PlayerState.PLAYING -> pause()
        }
    }


}