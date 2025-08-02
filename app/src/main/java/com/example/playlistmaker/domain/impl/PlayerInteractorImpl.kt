package com.example.playlistmaker.domain.impl

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.models.PlayerState

class PlayerInteractorImpl(
    private val repository: PlayerRepository
) : PlayerInteractor {

    private val handler = Handler(Looper.getMainLooper())
    private var updateTimerRunnable: Runnable? = null

    override var playerState: PlayerState = PlayerState.DEFAULT
        private set

    override var onUpdateTime: ((Int) -> Unit)? = null
    override var onStateChange: ((PlayerState) -> Unit)? = null

    override fun prepare(url: String) {
        playerState = PlayerState.DEFAULT
        onStateChange?.invoke(playerState)
        repository.onPreparedListener = {
            playerState = PlayerState.PREPARED
            onStateChange?.invoke(playerState)
        }
        repository.onCompletionListener = {
            stopTimer()
            playerState = PlayerState.PREPARED
            onStateChange?.invoke(playerState)
            onUpdateTime?.invoke(0)
        }
        repository.setDataSource(url)
    }

    override fun play() {
        if (playerState == PlayerState.PREPARED || playerState == PlayerState.PAUSED) {
            repository.start()
            playerState = PlayerState.PLAYING
            onStateChange?.invoke(playerState)
            startTimer()
        }
    }

    override fun pause() {
        if (playerState == PlayerState.PLAYING) {
            repository.pause()
            playerState = PlayerState.PAUSED
            onStateChange?.invoke(playerState)
            stopTimer()
        }
    }

    override fun release() {
        stopTimer()
        repository.release()
        playerState = PlayerState.DEFAULT
        onStateChange?.invoke(playerState)
    }

    override fun playbackControl(url: String) {
        when (playerState) {
            PlayerState.DEFAULT -> prepare(url)
            PlayerState.PREPARED, PlayerState.PAUSED -> play()
            PlayerState.PLAYING -> pause()
        }
    }

    private fun startTimer() {
        updateTimerRunnable = object : Runnable {
            override fun run() {
                onUpdateTime?.invoke(repository.getCurrentPosition())
                handler.postDelayed(this, 300)
            }

        }
        handler.post(updateTimerRunnable!!)
    }

    private fun stopTimer() {
        updateTimerRunnable?.let { handler.removeCallbacks(it) }
        updateTimerRunnable = null
    }
}