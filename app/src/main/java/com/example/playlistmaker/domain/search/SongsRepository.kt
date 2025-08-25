package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.Song

interface SongsRepository {
    fun searchSongs(expression: String): List<Song>
}