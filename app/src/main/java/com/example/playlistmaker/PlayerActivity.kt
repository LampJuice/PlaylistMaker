package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var playerHandler: Handler? = null
    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            playTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            playerHandler?.postDelayed(this, 300)
        }
    }

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
        time.text = song?.trackTimeMillis?.toMinutesAndSeconds()
        albumName.text = song?.collectionName ?: ""
        releaseDate.text = song?.releaseDate?.substring(0, 4) ?: ""
        if (releaseDate.text.isEmpty()) releaseGroup.visibility = View.GONE
        genre.text = song?.primaryGenreName
        country.text = song?.country

        trackName.text = song?.trackName
        artistName.text = song?.artistName

        previewUrl = song?.previewUrl ?: ""
        playerHandler = Handler(Looper.getMainLooper())

        playButton.setOnClickListener { playbackControl() }
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
        preparePlayer()

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerHandler?.removeCallbacks(updateTimerRunnable)
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }

    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { playerState = STATE_PREPARED }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_play_100)
            playTime.text = "00:00"
            playerHandler?.removeCallbacks(updateTimerRunnable)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_pause_100)
        playerState = STATE_PLAYING
        playerHandler?.post(updateTimerRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.ic_play_100)
        playerState = STATE_PAUSED
        playerHandler?.removeCallbacks(updateTimerRunnable)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}