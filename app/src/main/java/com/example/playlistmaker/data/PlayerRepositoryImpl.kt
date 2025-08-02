package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerRepositoryImpl() : PlayerRepository {
    private var mediaPlayer: MediaPlayer? = null
    override var onCompletionListener: (() -> Unit)? = null
    override var onPreparedListener: (() -> Unit)? = null

    override fun setDataSource(url: String) {
        release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            setOnPreparedListener { onPreparedListener?.invoke() }
            setOnCompletionListener { onCompletionListener?.invoke() }
            prepareAsync()
        }
    }

    override fun start() {
        mediaPlayer?.start()
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}