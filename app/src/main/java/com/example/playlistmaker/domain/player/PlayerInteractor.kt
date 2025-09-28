package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.models.PlayerState

interface PlayerInteractor {
    val playerState: PlayerState

    var onUpdateTime: ((currentMs: Int) -> Unit)?
    var onStateChange: ((PlayerState) -> Unit)?

    fun prepare(url: String)
    fun play()
    fun pause()
    fun release()
    fun playbackControl(url: String)
}