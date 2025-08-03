package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerState

interface PlayerInteractor {
    val playerState: PlayerState

    var onUpdateTime:((currentMs: Int)->Unit)?
    var onStateChange:((PlayerState)->Unit)?

    fun prepare(url: String)
    fun play()
    fun pause()
    fun release()
    fun playbackControl(url: String)
}