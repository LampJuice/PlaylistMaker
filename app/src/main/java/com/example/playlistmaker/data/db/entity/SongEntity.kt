package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song_table")
data class SongEntity(
    @PrimaryKey
    val id: String,
    val albumCoverUrl: String,
    val trackName: String,
    val artistName: String,
    val albumName: String,
    val releaseYear: String,
    val genre: String,
    val country: String,
    val trackTime: String,
    val trackUrl: String,
    val addedAt: Long,
    val isFavorite: Boolean
    )
