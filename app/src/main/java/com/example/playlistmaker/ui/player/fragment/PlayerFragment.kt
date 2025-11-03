package com.example.playlistmaker.ui.player.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.example.playlistmaker.ui.search.models.SongUi
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(viewLifecycleOwner.lifecycleScope)
    }
    private val gson: Gson by inject()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val songJson = arguments?.getString(SearchFragment.Companion.EXTRA_TRACK)
        val song = gson.fromJson(songJson, SongUi::class.java)

        viewModel.setTrackUrl(song?.previewUrl ?: "")

        viewModel.observeUiState.observe(viewLifecycleOwner) { state ->
            val iconPlay = when (state.playerState) {
                PlayerState.PLAYING -> R.drawable.ic_pause_100
                else -> R.drawable.ic_play_100
            }
            binding.playButton.setImageResource(iconPlay)
            val iconLike =
                if (state.isLiked) R.drawable.ic_like_pressed_51 else R.drawable.ic_like_51
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
            backArrow.setOnClickListener {
                findNavController().popBackStack(
                    R.id.searchFragment2,
                    false
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}