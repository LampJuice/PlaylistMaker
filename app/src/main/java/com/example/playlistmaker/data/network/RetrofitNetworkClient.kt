package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.SearchRequest

class RetrofitNetworkClient : NetworkClient {
    override fun doRequest(dto: Any): Response {
        if (dto is SearchRequest) {
            val resp = RetrofitSettings.iTunesAPI.searchSong(dto.expression).execute()
            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}