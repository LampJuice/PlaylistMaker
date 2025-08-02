package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {

    @GET("search?entity=song")
    fun searchSong(@Query("term") text: String): Call<SearchResponse>

}