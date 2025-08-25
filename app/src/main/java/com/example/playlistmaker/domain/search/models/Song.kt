package com.example.playlistmaker.domain.search.models

data class Song(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: Int,
    val collectionName: String,
    val releaseDate: String,
    val country: String,
    val primaryGenreName: String,
    val previewUrl: String
)