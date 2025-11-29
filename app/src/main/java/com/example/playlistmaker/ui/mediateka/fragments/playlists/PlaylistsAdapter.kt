package com.example.playlistmaker.ui.mediateka.fragments.playlists

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.playlist.models.Playlist

class PlaylistsAdapter : RecyclerView.Adapter<PlaylistViewHolder>() {

    var items: List<Playlist> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder = PlaylistViewHolder.from(parent)


    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}