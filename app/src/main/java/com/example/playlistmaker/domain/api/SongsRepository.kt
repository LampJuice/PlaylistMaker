package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Song

interface SongsRepository {
    fun searchSongs(expression: String): List<Song>
}