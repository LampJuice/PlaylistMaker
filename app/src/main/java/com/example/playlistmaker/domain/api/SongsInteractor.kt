package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Song


interface SongsInteractor {
    fun searchSongs(expression: String, consumer: SongsConsumer)

    interface SongsConsumer {
        fun consume(foundSongs: List<Song>)
    }
}