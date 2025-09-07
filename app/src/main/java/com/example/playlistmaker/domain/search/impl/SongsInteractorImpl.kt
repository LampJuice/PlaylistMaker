package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SongsInteractor
import com.example.playlistmaker.domain.search.SongsRepository
import java.util.concurrent.Executor

class SongsInteractorImpl(private val repository: SongsRepository, private val executor: Executor) : SongsInteractor {

    override fun searchSongs(
        expression: String,
        consumer: SongsInteractor.SongsConsumer
    ) {
        executor.execute {
            try {
                val results = repository.searchSongs(expression)
                consumer.consume(results)
            } catch (e: Exception){
                consumer.onError(e)
            }
        }
    }
}