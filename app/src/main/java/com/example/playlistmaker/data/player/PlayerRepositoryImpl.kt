package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.PlayerRepository
import java.io.IOException

class PlayerRepositoryImpl(private var mediaPlayer: MediaPlayer) : PlayerRepository {
    override var onCompletionListener: (() -> Unit)? = null
    override var onPreparedListener: (() -> Unit)? = null

    override fun setDataSource(url: String) {
        try {
            if (mediaPlayer == null){
                mediaPlayer = MediaPlayer()
            }else{
                mediaPlayer.reset()
            }
        } catch (e: IllegalStateException) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer()
        }

        mediaPlayer.apply {
            try {
                setDataSource(url)
                setOnPreparedListener { onPreparedListener?.invoke() }
                setOnCompletionListener { onCompletionListener?.invoke() }
                prepareAsync()
            }catch (e: IOException){
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