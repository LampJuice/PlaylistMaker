package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.search.dto.SearchRequest
import com.example.playlistmaker.data.search.dto.SearchResponse
import com.example.playlistmaker.domain.search.NoNetworkException
import com.example.playlistmaker.domain.search.SongsRepository
import com.example.playlistmaker.domain.search.models.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SongsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) : SongsRepository {
    override fun searchSongs(expression: String): Flow<List<Song>> = flow {
        val response = networkClient.doRequest(SearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                with(response as SearchResponse) {
                    val favoriteIds = appDatabase.songDao().getSongsId()
                    val data = results.map {
                        val songId = it.trackId ?: 0
                        Song(
                            it.trackName ?: "",
                            it.artistName ?: "",
                            it.trackTimeMillis ?: 0L,
                            it.artworkUrl100 ?: "",
                            songId,
                            it.collectionName ?: "",
                            it.releaseDate ?: "",
                            it.country ?: "",
                            it.primaryGenreName ?: "",
                            it.previewUrl ?: "",
                            0L,
                            false,
                            0
                        )
                    }
                    emit(data)
                }
            }

            -1 -> {
                throw NoNetworkException()
            }

            else -> {
                emit(emptyList())
            }
        }
    }

}