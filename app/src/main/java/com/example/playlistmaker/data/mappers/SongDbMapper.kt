package com.example.playlistmaker.data.mappers

import com.example.playlistmaker.data.db.entity.SavedSongEntity
import com.example.playlistmaker.data.db.entity.SongEntity
import com.example.playlistmaker.domain.search.models.Song

class SongDbMapper {
    fun mapSaved(song: Song,playlistId: Int): SavedSongEntity {
        return SavedSongEntity(
            song.trackId.toString(),
            song.artworkUrl100,
            song.trackName,
            song.artistName,
            song.collectionName,
            song.releaseDate,
            song.primaryGenreName,
            song.country,
            song.trackTimeMillis.toString(),
            song.previewUrl,
            song.addedAt,
            song.isFavorite,
            playlistId
        )
    }
    fun mapSaved(song: SavedSongEntity): Song {
        return Song(
            song.trackName,
            song.artistName,
            song.trackTime.toLong(),
            song.albumCoverUrl,
            song.id.toInt(),
            song.albumName,
            song.releaseYear,
            song.country,
            song.genre,
            song.trackUrl,
            song.addedAt,
            song.isFavorite,
            song.playlistId
        )
    }

    fun map(song: Song): SongEntity {
        return SongEntity(
            song.trackId.toString(),
            song.artworkUrl100,
            song.trackName,
            song.artistName,
            song.collectionName,
            song.releaseDate,
            song.primaryGenreName,
            song.country,
            song.trackTimeMillis.toString(),
            song.previewUrl,
            song.addedAt,
            song.isFavorite
        )
    }

    fun map(song: SongEntity): Song {
        return Song(
            song.trackName,
            song.artistName,
            song.trackTime.toLong(),
            song.albumCoverUrl,
            song.id.toInt(),
            song.albumName,
            song.releaseYear,
            song.country,
            song.genre,
            song.trackUrl,
            song.addedAt,
            song.isFavorite,
            0
        )
    }
}