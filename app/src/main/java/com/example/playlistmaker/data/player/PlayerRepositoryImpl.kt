package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.PlayerRepository


class PlayerRepositoryImpl(
    private var mediaPlayer: MediaPlayer
) : PlayerRepository {

    override var onCompletionListener: (() -> Unit)? = null
    override var onPreparedListener: (() -> Unit)? = null

    override fun setDataSource(url: String) {

        if (url.isBlank()) return

        mediaPlayer.apply{
            try {

                reset()
                setDataSource(url)
                setOnPreparedListener { onPreparedListener?.invoke() }
                setOnCompletionListener { onCompletionListener?.invoke() }
                prepareAsync()

            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun getCurrentPosition(): Int = try {mediaPlayer.currentPosition } catch (_: Exception) {0}

    override fun isPlaying(): Boolean = try {mediaPlayer.isPlaying } catch (_: Exception) {false}

    override fun release() {
        mediaPlayer.release()
    }

}