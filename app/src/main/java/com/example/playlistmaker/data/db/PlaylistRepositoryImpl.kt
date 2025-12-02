package com.example.playlistmaker.data.db

import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.dao.SavedSongsDao
import com.example.playlistmaker.data.db.dao.SongDao
import com.example.playlistmaker.data.mappers.PlaylistDbMapper
import com.example.playlistmaker.data.mappers.SongDbMapper
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Song
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val mapper: PlaylistDbMapper,
    private val songDao: SongDao,
    private val songDbMapper: SongDbMapper,
    private val savedSongsDao: SavedSongsDao,
    private val gson: Gson
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

    override suspend fun getSongsForPlaylist(playlistId: Int): List<Song> {
        val entities = savedSongsDao.getSongsForPlaylist(playlistId.toString())
        return entities.map { songDbMapper.mapSaved(it) }
    }

    override suspend fun getSavedSongsByIds(ids: List<Int>): List<Song> {
        val allSaved = savedSongsDao.getSavedSongs()
        val filtered = allSaved.filter { ids.contains(it.id.toInt()) }
        return filtered.map { entity -> songDbMapper.mapSaved(entity) }
    }

    override suspend fun removeSongFromPlaylist(plId: Int, songId: Int) {
        val playlist = playlistDao.getPlaylist(plId) ?: return

        val current = mapper.map(playlist)

        val updatedIds = current.songIds.filter { it != songId }

        val updatedPlaylist =
            playlist.copy(songIdsJson = gson.toJson(updatedIds), songCount = updatedIds.size)
        playlistDao.updatePlaylist(updatedPlaylist)
        cleanLeftOvers(songId)
    }

    override suspend fun deletePlaylist(id: Int) {
        val entity = playlistDao.getPlaylist(id) ?: return
        playlistDao.deletePlaylist(entity)
    }

    override suspend fun deleteSongById(id: Int) {
        savedSongsDao.deleteById(id)
    }

    suspend fun cleanLeftOvers(songId: Int) {

        val allPlaylists = playlistDao.getAllPlaylist().first()
        val stillUsed = allPlaylists.any { playlist ->
            mapper.map(playlist).songIds.contains(songId)
        }
        if (!stillUsed) {
            savedSongsDao.deleteById(songId)
        }
    }


}