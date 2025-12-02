package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.SavedSongEntity

@Dao
interface SavedSongsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: SavedSongEntity)

    @Delete
    suspend fun deleteSong(song: SavedSongEntity)

    @Query("DELETE FROM saved_songs WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM saved_songs WHERE id = :id")
    suspend fun getSong(id: String): SavedSongEntity?

    @Query("SELECT * FROM saved_songs WHERE id IN (:ids)")
    suspend fun getSongsByIds(ids: List<Int>): List<SavedSongEntity>

    @Query("SELECT * FROM saved_songs WHERE playlistId = :playlistId")
    suspend fun getSongsForPlaylist(playlistId: String): List<SavedSongEntity>

    @Query("SELECT * FROM saved_songs")
    suspend fun getSavedSongs(): List<SavedSongEntity>
}