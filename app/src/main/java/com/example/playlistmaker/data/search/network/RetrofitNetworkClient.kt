package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.SearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RetrofitNetworkClient(private val api: ITunesAPI) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (dto is SearchRequest) {
            return withContext(Dispatchers.IO) {
                try {
                    val response = api.searchSong(dto.expression)
                    response.apply { resultCode = 200 }
                } catch (e: IOException) {
                    Response().apply { resultCode = -1 }
                } catch (e: HttpException) {
                    Response().apply { resultCode = e.code() }
                } catch (e: Exception) {
                    Response().apply { resultCode = 500 }
                }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}