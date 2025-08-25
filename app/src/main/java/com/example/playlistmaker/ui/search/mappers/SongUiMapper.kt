package com.example.playlistmaker.ui.search.mappers

import com.example.playlistmaker.domain.search.models.Song
import com.example.playlistmaker.ui.search.models.SongUi

fun Song.toUi(): SongUi{
    return SongUi(
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTimeMillis?.let { formatTrackTime(it) } ?: "",
        artworkUrl100 = artworkUrl100,
        trackId = trackId,
        collectionName = collectionName,
        releaseDate = releaseDate,
        country = country,
        primaryGenreName = primaryGenreName,
        previewUrl = previewUrl
    )

}

private fun formatTrackTime(mills: Long): String{
    val mins = mills/1000/60
    val seconds = (mills/1000)%60
    return "%d:%02d".format(mins, seconds)
}

fun SongUi.toDomain(): Song {
    return Song(
        trackName = trackName ?:"",
        artistName = artistName ?:"",
        trackTimeMillis =parseTrackTime(trackTime),
        artworkUrl100 = artworkUrl100 ?:"",
        trackId = trackId ?: 0,
        collectionName = collectionName ?: "",
        releaseDate = releaseDate ?:"",
        country = country?:"",
        primaryGenreName = primaryGenreName ?: "",
        previewUrl = previewUrl ?: ""
    )
}

private fun parseTrackTime(trackTime: String?): Long {
    if (trackTime.isNullOrEmpty()) return 0L
    val parts = trackTime.split(":")
    return if (parts.size == 2) {
        val mins = parts[0].toLongOrNull() ?: 0
        val secs = parts[1].toLongOrNull() ?: 0
        (mins * 60 + secs) * 1000
    } else 0L
}