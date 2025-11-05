package com.example.playlistmaker.data.db

import com.example.playlistmaker.data.mappers.SongDbMapper
import com.example.playlistmaker.domain.db.FavoritesRepository
import com.example.playlistmaker.domain.search.models.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val songDbMapper: SongDbMapper
) : FavoritesRepository {
    override suspend fun addFavoriteSong(song: Song) {
        appDatabase.songDao()
            .insertSong(songDbMapper.map(song.copy(addedAt = System.currentTimeMillis())))
    }

    override suspend fun deleteFavoriteSong(song: Song) {
        appDatabase.songDao().deleteSong(songDbMapper.map(song))
    }

    override fun favoriteSongs(): Flow<List<Song>> =
        appDatabase.songDao().getSongs()
            .map { songs -> songs.map { songDbMapper.map(it) } }

    override suspend fun getSongsIds(): List<Int> {
        return appDatabase.songDao().getSongsId().map { it.toInt() }
    }

}