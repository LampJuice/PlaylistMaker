package com.example.playlistmaker.data.player

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.player.PlayerRepository

class PlayerTimer(
    private val playerRepository: PlayerRepository,
    private val updateInterval: Long = 300L
) {
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    var onTick: ((Int) -> Unit)? = null

    fun start() {
        if (runnable != null) return
        runnable = object : Runnable{
            override fun run() {
                onTick?.invoke(playerRepository.getCurrentPosition())
                handler.postDelayed(this, updateInterval)
            }

        }
        handler.post(runnable!!)
    }

    fun stop() {
        runnable?.let { handler.removeCallbacks(it) }
        runnable = null
    }
}