package com.example.playlistmaker.data.player

import com.example.playlistmaker.domain.player.PlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerTimer(
    private val playerRepository: PlayerRepository,
    private val updateInterval: Long = 300L,
    private val coroutineScope: CoroutineScope

) {

    private var timerJob: Job? = null

    var onTick: ((Int) -> Unit)? = null

    fun start() {
        if (timerJob?.isActive == true) return
        timerJob = coroutineScope.launch {
            delay(100L)
            while (isActive) {
                val pos = playerRepository.getCurrentPosition()
                if (!isActive) break
                onTick?.invoke(pos)
                delay(updateInterval)
            }
        }
    }

    fun stop() {
        timerJob?.cancel()
        timerJob = null
    }
}