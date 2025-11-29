package com.example.playlistmaker.data.mappers

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.google.gson.Gson

class PlaylistDbMapper(private val gson: Gson) {
    fun map(entity: PlaylistEntity): Playlist {
        val ids = gson.fromJson(entity.songIdsJson, Array<Int>::class.java)?.toList() ?: emptyList()
        return Playlist(
            entity.id,
            entity.name,
            entity.description,
            entity.coverPath,
            ids,
            entity.songCount
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverPath,
            gson.toJson(playlist.songIds),
            playlist.songCount
        )

    }
}