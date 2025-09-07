package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.search.models.SongUi
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private val viewModel by viewModel<PlayerViewModel>()
    private val gson: Gson by inject()
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val songJson = intent.getStringExtra(SearchActivity.Companion.EXTRA_TRACK)
        val song = gson.fromJson(songJson, SongUi::class.java)

        viewModel.setTrackUrl(song?.previewUrl ?: "")

        viewModel.observeUiState.observe(this) { state ->
            val iconPlay = when (state.playerState) {
                PlayerState.PLAYING -> R.drawable.ic_pause_100
                else -> R.drawable.ic_play_100
            }
            binding.playButton.setImageResource(iconPlay)
            val iconLike = if (state.isLiked) R.drawable.ic_like_pressed_51 else R.drawable.ic_like_51
            binding.likeButton.setImageResource(iconLike)

            binding.playTime.text = state.playTime
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
            time2.text = song?.trackTime

            playButton.setOnClickListener { viewModel.onPlayPauseClick() }
            likeButton.setOnClickListener { viewModel.onLikeClick() }
            backArrow.setOnClickListener { finish() }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}