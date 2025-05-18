package com.example.playlistmaker

import android.content.Context
import android.util.TypedValue
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

    fun bind(model: Track){
        sourceName.text = model.trackName
        sourceBand.text = model.artistName
        sourceTime.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2F, itemView.context)))
            .placeholder(R.drawable.placeholder_45)
            .into(sourceImg)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}