package com.example.playlistmaker

import com.google.gson.annotations.SerializedName

data class Song(
    @SerializedName("trackName")
    val trackName: String?,
    @SerializedName("artistName")
    val artistName: String?,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long?,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String?

)
