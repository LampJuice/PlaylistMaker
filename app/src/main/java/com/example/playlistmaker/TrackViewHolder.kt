package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    private val sourceName: TextView = itemView.findViewById(R.id.sourceName)
    private val sourceImg: ImageView = itemView.findViewById(R.id.sourceImg)
    private val sourceBand: TextView = itemView.findViewById(R.id.sourceBand)
    private val sourceTime: TextView = itemView.findViewById(R.id.sourceTime)

    private val cornerRadiusPx = itemView.context.resources.getDimensionPixelSize(R.dimen.round_corners_cover)

    fun bind(model: Song, onTrackClick:(Song)-> Unit){
        val formatTime = model.trackTimeMillis?.toMinutesAndSeconds() ?: "00:00"
        sourceName.text = model.trackName
        sourceBand.text = model.artistName
        sourceTime.text = formatTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(cornerRadiusPx))
            .placeholder(R.drawable.placeholder_45)
            .into(sourceImg)
        itemView.setOnClickListener { onTrackClick(model) }
    }
}