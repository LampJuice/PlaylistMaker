package com.example.playlistmaker

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Parcelize
data class Song(
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
    val primaryGenreName: String?
) : Parcelable
