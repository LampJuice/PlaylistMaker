package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {

    @GET("search?entity=song")
    fun searchSong(@Query("term") text: String): Call<SearchResponse>

}