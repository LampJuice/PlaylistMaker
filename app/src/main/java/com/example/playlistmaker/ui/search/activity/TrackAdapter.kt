package com.example.playlistmaker.ui.search.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.search.models.Song

class TrackAdapter(
    private val tracks: MutableList<Song>,
    val onTrackClick: (Song) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder.from(parent)

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], onTrackClick)

    }

    fun updateData(newSongs: List<Song>) {
        tracks.clear()
        tracks.addAll(newSongs)
        notifyDataSetChanged()
    }

}