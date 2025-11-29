package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.dao.SavedSongsDao
import com.example.playlistmaker.data.db.dao.SongDao
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.SavedSongEntity
import com.example.playlistmaker.data.db.entity.SongEntity

@Database(
    version = 3,
    entities = [SongEntity::class, PlaylistEntity::class, SavedSongEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun savedSongDao(): SavedSongsDao
}