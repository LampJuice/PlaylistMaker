package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.SongDao
import com.example.playlistmaker.data.db.entity.SongEntity

@Database(version = 1, entities = [SongEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao() : SongDao
}