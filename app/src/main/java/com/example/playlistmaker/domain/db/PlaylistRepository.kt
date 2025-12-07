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
    suspend fun saveSongPlaylist(song: Song,playlistId: Int)
    suspend fun getSongsForPlaylist(playlistId: Int): List<Song>
    suspend fun getSavedSongsByIds(ids: List<Int>): List<Song>
    suspend fun removeSongFromPlaylist(plId: Int, songId: Int)
    suspend fun deletePlaylist(id: Int)
    suspend fun deleteSongById(id: Int)
    fun observePlaylistById(id: Int): Flow<Playlist?>
    fun observeSavedSongs(): Flow<List<Song>>
    fun observeSongsForPlaylist(playlistId: Int):  Flow<List<Song>>
}