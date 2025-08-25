package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.Song

interface SongsInteractor {
    fun searchSongs(expression: String, consumer: SongsConsumer)

    interface SongsConsumer {
        fun consume(foundSongs: List<Song>)
        fun onError(error: Throwable)
    }
}