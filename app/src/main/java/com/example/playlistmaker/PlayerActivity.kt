package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlayerActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player)

        val song = intent.getParcelableExtra<Song>("TRACK_KEY")

        val trackName = findViewById<TextView>(R.id.track_name)
        val artistName = findViewById<TextView>(R.id.artist_name)
        val time = findViewById<TextView>(R.id.time2)
        val albumName = findViewById<TextView>(R.id.album_name2)
        val releaseDate = findViewById<TextView>(R.id.release_date2)
        val genre = findViewById<TextView>(R.id.genre_name2)
        val country = findViewById<TextView>(R.id.country2)
        val backButton = findViewById<ImageView>(R.id.back_arrow)
        val likeButton = findViewById<ImageView>(R.id.like_button)
        val playButton = findViewById<ImageView>(R.id.play_button)
        val releaseGroup = findViewById<Group>(R.id.release_group)
        val cover = findViewById<ImageView>(R.id.cover)
        val cornerRadiusPx = resources.getDimensionPixelSize(R.dimen.corner_radius_player_cover)


        Glide.with(this)
            .load(song?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .fitCenter()
            .transform(RoundedCorners(cornerRadiusPx))
            .placeholder(R.drawable.placeholder_45)
            .into(cover)
        time.text = song?.trackTimeMillis?.toMinutesAndSeconds()
        albumName.text = song?.collectionName ?: ""
        releaseDate.text = song?.releaseDate?.substring(0, 4) ?: ""
        if (releaseDate.text.isEmpty()) releaseGroup.visibility = View.GONE
        genre.text = song?.primaryGenreName
        country.text = song?.country

        trackName.text = song?.trackName
        artistName.text = song?.artistName

        var isPlayed = false
        playButton.setOnClickListener {
            isPlayed = !isPlayed
            val icPlay = if (isPlayed) {
                R.drawable.ic_pause_100
            } else {
                R.drawable.ic_play_100
            }
            playButton.setImageResource(icPlay)
        }

        backButton.setOnClickListener { finish() }

        var isLiked = false
        likeButton.setOnClickListener {
            isLiked = !isLiked
            val icRes = if (isLiked) {
                R.drawable.ic_like_pressed_51
            } else {
                R.drawable.ic_like_51
            }
            likeButton.setImageResource(icRes)
        }

    }
}