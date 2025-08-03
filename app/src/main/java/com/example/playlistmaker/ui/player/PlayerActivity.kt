package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Song
import com.example.playlistmaker.ui.search.SearchActivity
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var playerInteractor: PlayerInteractor


    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var time: TextView
    private lateinit var albumName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var backButton: ImageView
    private lateinit var likeButton: ImageView
    private lateinit var playButton: ImageView
    private lateinit var releaseGroup: Group
    private lateinit var cover: ImageView
    private lateinit var previewUrl: String
    private lateinit var playTime: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerInteractor = Creator.providePlayerInteractor()

        val song = intent.getParcelableExtra<Song>(SearchActivity.EXTRA_TRACK)

        trackName = findViewById<TextView>(R.id.track_name)
        artistName = findViewById<TextView>(R.id.artist_name)
        time = findViewById<TextView>(R.id.time2)
        albumName = findViewById<TextView>(R.id.album_name2)
        releaseDate = findViewById<TextView>(R.id.release_date2)
        genre = findViewById<TextView>(R.id.genre_name2)
        country = findViewById<TextView>(R.id.country2)
        backButton = findViewById<ImageView>(R.id.back_arrow)
        likeButton = findViewById<ImageView>(R.id.like_button)
        playButton = findViewById<ImageView>(R.id.play_button)
        releaseGroup = findViewById<Group>(R.id.release_group)
        cover = findViewById<ImageView>(R.id.cover)
        playTime = findViewById<TextView>(R.id.play_time)

        val cornerRadiusPx = resources.getDimensionPixelSize(R.dimen.corner_radius_player_cover)

        Glide.with(this)
            .load(song?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .fitCenter()
            .transform(RoundedCorners(cornerRadiusPx))
            .placeholder(R.drawable.placeholder_45)
            .into(cover)
        time.text = song?.trackTimeMillis
        albumName.text = song?.collectionName ?: ""
        releaseDate.text = song?.releaseDate?.substring(0, 4) ?: ""
        if (releaseDate.text.isEmpty()) releaseGroup.visibility = View.GONE
        genre.text = song?.primaryGenreName
        country.text = song?.country

        trackName.text = song?.trackName
        artistName.text = song?.artistName
        previewUrl = song?.previewUrl ?: ""

        playerInteractor.onStateChange = { state ->
            when (state) {
                PlayerState.PLAYING -> playButton.setImageResource(R.drawable.ic_pause_100)
                PlayerState.DEFAULT, PlayerState.PREPARED, PlayerState.PAUSED -> playButton.setImageResource(
                    R.drawable.ic_play_100
                )
            }
        }
        playerInteractor.onUpdateTime = { currentMs ->
            playTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentMs)
        }

        playButton.setOnClickListener {
            playerInteractor.playbackControl(previewUrl)
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

    override fun onPause() {
        super.onPause()
        playerInteractor.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.release()
    }

}