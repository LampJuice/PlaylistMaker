package com.example.playlistmaker.domain.api

interface PlayerRepository {

    var onCompletionListener: (() -> Unit)?
    var onPreparedListener: (() -> Unit)?

    fun setDataSource(url: String)
    fun start()
    fun pause()
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
    fun release()
}