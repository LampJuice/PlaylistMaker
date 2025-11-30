package com.example.playlistmaker.ui.mediateka.fragments.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylstViewBinding
import com.example.playlistmaker.domain.playlist.models.Playlist

class PlaylistViewHolder(private val binding: PlaylstViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Playlist) {
        binding.name.text = item.name
        binding.desc.text = binding.root.context.resources.getQuantityString(R.plurals.tracks_count,item.songCount,item.songCount)

        if (item.coverPath == null) binding.cover.setImageResource(R.drawable.placeholder_45)
        else binding.cover.setImageURI(item.coverPath.toUri())

    }

    companion object {
        fun from(parent: ViewGroup): PlaylistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylstViewBinding.inflate(inflater, parent, false)
            return PlaylistViewHolder(binding)
        }
    }

}