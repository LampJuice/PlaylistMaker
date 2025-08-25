package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.search.models.Song
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.google.gson.Gson

class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: ActivityPlayerBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val songJson = intent.getStringExtra(SearchActivity.Companion.EXTRA_TRACK)
        val song = Gson().fromJson(songJson, Song::class.java)

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getFactory(Creator.providePlayerInteractor())
        )[PlayerViewModel::class.java]

        viewModel.setTrackUrl(song?.previewUrl ?: "")

        viewModel.observePlayerState.observe(this) { state ->
            val icon = when (state) {
                PlayerState.PLAYING -> R.drawable.ic_pause_100
                else -> R.drawable.ic_play_100
            }
            binding.playButton.setImageResource(icon)
        }

        viewModel.observePlayTime.observe(this) { time ->
            binding.playTime.text = time
        }

        viewModel.observeIsLiked.observe(this) { liked ->
            val icon = if (liked) R.drawable.ic_like_pressed_51 else R.drawable.ic_like_51
            binding.likeButton.setImageResource(icon)
        }




        val cornerRadiusPx = resources.getDimensionPixelSize(R.dimen.corner_radius_player_cover)

        Glide.with(this)
            .load(song?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .fitCenter()
            .transform(RoundedCorners(cornerRadiusPx))
            .placeholder(R.drawable.placeholder_45)
            .into(binding.cover)
        binding.apply {
            trackName.text = song?.trackName
            albumName2.text = song?.collectionName ?: ""
            releaseDate2.text = song?.releaseDate?.substring(0, 4) ?: ""
            genreName2.text = song?.primaryGenreName
            country2.text = song?.country
            artistName.text = song?.artistName
            time2.text = song?.trackTimeMillis

            playButton.setOnClickListener { viewModel.onPlayPauseClick() }
            likeButton.setOnClickListener { viewModel.onLikeClick() }
            backArrow.setOnClickListener { finish() }
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.omPause()
    }




}