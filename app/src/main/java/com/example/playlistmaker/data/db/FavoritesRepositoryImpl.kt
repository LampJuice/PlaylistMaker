package com.example.playlistmaker.data.db

import com.example.playlistmaker.data.db.dao.SongDao
import com.example.playlistmaker.data.mappers.SongDbMapper
import com.example.playlistmaker.domain.db.FavoritesRepository
import com.example.playlistmaker.domain.search.models.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val songDao: SongDao,
    private val songDbMapper: SongDbMapper
) : FavoritesRepository {
    override suspend fun addFavoriteSong(song: Song) {
        songDao
            .insertSong(songDbMapper.map(song.copy(addedAt = System.currentTimeMillis())))
    }

    override suspend fun deleteFavoriteSong(song: Song) {
        songDao.deleteSong(songDbMapper.map(song))
    }

    override fun favoriteSongs(): Flow<List<Song>> =
        songDao.getSongs()
            .map { songs -> songs.map { songDbMapper.map(it) } }

    override suspend fun getSongsIds(): List<Int> {
        return songDao.getSongsId().map { it.toInt() }
    }

}