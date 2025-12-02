package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.SavedSongEntity

@Dao
interface SavedSongsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: SavedSongEntity)

    @Query("SELECT * FROM saved_songs WHERE id = :id")
    suspend fun getSong(id: String): SavedSongEntity?
}