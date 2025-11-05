package com.example.playlistmaker.ui.search.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongUi(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val trackId: Int,
    val collectionName: String,
    val releaseDate: String,
    val country: String,
    val primaryGenreName: String,
    val previewUrl: String,
    val addedAt: Long,
    val isLiked: Boolean
) : Parcelable