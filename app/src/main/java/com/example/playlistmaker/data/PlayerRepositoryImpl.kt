package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {
    override var onCompletionListener: (() -> Unit)? = null
    override var onPreparedListener: (() -> Unit)? = null

    override fun setDataSource(url: String) {
        mediaPlayer.reset()
        mediaPlayer.apply {
            setDataSource(url)
            setOnPreparedListener { onPreparedListener?.invoke() }
            setOnCompletionListener { onCompletionListener?.invoke() }
            prepareAsync()
        }
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun release() {
        mediaPlayer.release()
    }
}