package com.example.playlistmaker.ui.player.fragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.playlist.models.Playlist

class BottomSheetPlaylistsAdapter(val onItemClick: (Playlist) -> Unit) :
    RecyclerView.Adapter<BottomSheetPlaylistsViewHolder>() {

    var items: List<Playlist> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetPlaylistsViewHolder = BottomSheetPlaylistsViewHolder.from(parent)

    override fun onBindViewHolder(
        holder: BottomSheetPlaylistsViewHolder,
        position: Int
    ) {
        holder.bind(items[position], onItemClick)
    }


    override fun getItemCount(): Int {
        return items.size
    }
}