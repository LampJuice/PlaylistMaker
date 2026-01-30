package com.example.playlistmaker.service

import com.example.playlistmaker.domain.player.models.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface PlayerController {
    val playerState: StateFlow<PlayerState>
    val currentTime: StateFlow<Int>
    fun prepareAndPlay(url: String)
    fun play()

    fun pause()
    fun startForegroundMode()
    fun stopForegroundMode()

    fun initTrack(artist: String, title: String)
}