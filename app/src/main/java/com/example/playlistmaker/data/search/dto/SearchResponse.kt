package com.example.playlistmaker.data.search.dto

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val results: List<SongDto>
) : Response()