package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.Song
import kotlinx.coroutines.flow.Flow

interface SongsInteractor {
    fun searchSongs(expression: String): Flow<List<Song>>
}