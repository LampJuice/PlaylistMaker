package com.example.playlistmaker.ui.player.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetPlaylistViewBinding
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.utils.pluralizeTracks

class BottomSheetPlaylistsViewHolder(private val binding: BottomSheetPlaylistViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Playlist, onItemClick: (Playlist) -> Unit) {
        binding.name.text = item.name
        binding.songs.text = pluralizeTracks(item.songCount)


        if (item.coverPath == null) binding.cover.setImageResource(R.drawable.placeholder_45)
        else binding.cover.setImageURI(item.coverPath.toUri())
        itemView.setOnClickListener { onItemClick(item) }

    }

    companion object {
        fun from(parent: ViewGroup): BottomSheetPlaylistsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = BottomSheetPlaylistViewBinding.inflate(inflater, parent, false)
            return BottomSheetPlaylistsViewHolder(binding)
        }
    }

}