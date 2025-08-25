package com.example.playlistmaker.data.search.dto

import com.google.gson.annotations.SerializedName

data class SongDto(
    @SerializedName("trackName")
    val trackName: String?,
    @SerializedName("artistName")
    val artistName: String?,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long?,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String?,
    @SerializedName("trackId")
    val trackId: Int?,
    @SerializedName("collectionName")
    val collectionName: String?,
    @SerializedName("releaseDate")
    val releaseDate: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("primaryGenreName")
    val primaryGenreName: String?,
    @SerializedName("previewUrl")
    val previewUrl: String?
)