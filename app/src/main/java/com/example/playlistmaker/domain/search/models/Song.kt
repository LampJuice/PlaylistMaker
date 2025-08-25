package com.example.playlistmaker.domain.search.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val trackId: Int?,
    val collectionName: String?,
    val releaseDate: String?,
    val country: String?,
    val primaryGenreName: String?,
    val previewUrl: String?
) : Parcelable