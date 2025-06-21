package com.example.playlistmaker

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("resultCount")
    val resultCount : Int,
    @SerializedName("results")
    val results: List<Song>
)
