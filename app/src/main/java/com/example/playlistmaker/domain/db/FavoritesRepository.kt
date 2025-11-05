package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.models.Song
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun addFavoriteSong(song: Song)
    suspend fun deleteFavoriteSong(song: Song)
    fun favoriteSongs(): Flow<List<Song>>
    suspend fun getSongsIds(): List<Int>
}