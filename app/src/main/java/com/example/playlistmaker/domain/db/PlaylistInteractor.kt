package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Song
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addTrackToPlaylist(playlistId: Int, song: Song): Boolean
    suspend fun createPlaylist(name: String, description: String?, coverPath: String?): Int
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getPlayListById(id: Int): Playlist?
    suspend fun saveCover(uriString: String): String?
}