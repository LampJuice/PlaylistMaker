package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Song
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist): Int
    suspend fun updatePlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylistById(id: Int): Playlist?
    suspend fun saveSong(song: Song)
    suspend fun saveSongPlaylist(song: Song)
}