package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.SearchRequest

class RetrofitNetworkClient(private val api: ITunesAPI) : NetworkClient {
    override fun doRequest(dto: Any): Response {
        if (dto is SearchRequest) {
            val resp = api.searchSong(dto.expression).execute()
            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}