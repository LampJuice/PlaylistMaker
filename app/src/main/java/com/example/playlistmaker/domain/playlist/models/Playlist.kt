package com.example.playlistmaker.domain.playlist.models

data class Playlist(
    val id: Int = 0,
    val name: String,
    val description: String?,
    val coverPath: String?,
    val songIds: List<Int>,
    val songCount: Int
)
