package com.example.playlistmaker.data.db

import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.dao.SavedSongsDao
import com.example.playlistmaker.data.db.dao.SongDao
import com.example.playlistmaker.data.mappers.PlaylistDbMapper
import com.example.playlistmaker.data.mappers.SongDbMapper
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val mapper: PlaylistDbMapper,
    private val songDao: SongDao,
    private val songDbMapper: SongDbMapper,
    private val savedSongsDao: SavedSongsDao
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist): Int {
        val entity = mapper.map(playlist)
        val id = playlistDao.insertPlaylist(entity)
        return id.toInt()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.updatePlaylist(mapper.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylist().map { list ->
            list.map { mapper.map(it) }
        }
    }

    override suspend fun getPlaylistById(id: Int): Playlist? {
        val entity = playlistDao.getPlaylist(id) ?: return null
        return mapper.map(entity)
    }

    override suspend fun saveSong(song: Song) {
        songDao.insertSong(songDbMapper.map(song))
    }

    override suspend fun saveSongPlaylist(song: Song) {
        savedSongsDao.insertSong(songDbMapper.mapSaved(song))
    }


}