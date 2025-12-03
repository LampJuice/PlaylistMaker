package com.example.playlistmaker.ui.search.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.ui.search.models.SongUi

class TrackViewHolder(private val binding: TrackViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val cornerRadiusPx =
        itemView.context.resources.getDimensionPixelSize(R.dimen.round_corners_cover)

    fun bind(model: SongUi, onTrackClick: (SongUi) -> Unit, onTrackLongClick: (SongUi) -> Unit) {
        binding.sourceName.text = model.trackName
        binding.sourceBand.text = model.artistName
        binding.sourceTime.text = model.trackTime
        Glide.with(binding.root)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(cornerRadiusPx))
            .placeholder(R.drawable.placeholder_45)
            .into(binding.sourceImg)
        itemView.setOnClickListener { onTrackClick(model) }
        itemView.setOnLongClickListener {
            onTrackLongClick(model)
            true
        }
    }

    companion object {
        fun from(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TrackViewBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding)
        }
    }
}