package com.example.playlistmaker.ui.player.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.example.playlistmaker.ui.search.models.SongUi
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback? = null
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    private val adapter = BottomSheetPlaylistsAdapter({ playlist ->
        viewModel.addSongToPlaylist(playlist.id)
    })

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

        viewModel.setTrack(song)

        binding.playlistsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.playlistsRecycler.adapter = adapter

        viewModel.playlist.observe(viewLifecycleOwner) { list ->
            adapter.items = list
            adapter.notifyDataSetChanged()

        }

        viewModel.closeBottomSheet.observe(viewLifecycleOwner) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }



        viewModel.observeUiState.observe(viewLifecycleOwner) { state ->
            binding.playbackButton.setPlaying(
                state.playerState == PlayerState.PLAYING
            )
            val iconLike =
                if (state.isLiked) R.drawable.ic_like_pressed_51 else R.drawable.ic_like_51
            binding.likeButton.setImageResource(iconLike)

            binding.playTime.text = state.playTime
        }

        val cornerRadiusPx = resources.getDimensionPixelSize(R.dimen.corner_radius_player_cover)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, p1: Int) {
                binding.overlay.visibility =
                    if (p1 == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
            }

            override fun onSlide(p0: View, p1: Float) {
                binding.overlay.alpha = ((p1 + 1) / 2).coerceIn(0f, 1f)
            }
        }

        bottomSheetBehavior?.addBottomSheetCallback(bottomSheetCallback!!)
        binding.overlay.setOnClickListener {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.plCreateBtn.setOnClickListener {

            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.newPlaylistButton.setOnClickListener {
            val track = viewModel.currentTrack ?: return@setOnClickListener
            val trackJson = gson.toJson(track)
            val bundle = Bundle().apply {
                putString(SearchFragment.Companion.EXTRA_TRACK, trackJson)
            }
            findNavController().navigate(
                R.id.action_playerFragment2_to_createPlaylistFragment,
                bundle
            )

            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }

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

            playbackButton.onClick = { viewModel.onPlayPauseClick() }
            likeButton.setOnClickListener { viewModel.onLikeClick() }
            backArrow.setOnClickListener {
                findNavController().popBackStack(
                )
            }
        }
        viewModel.addToPlaylistStatus.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

        }
    }


    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroyView() {
        bottomSheetCallback?.let { callback ->
            bottomSheetBehavior?.removeBottomSheetCallback(callback)
        }
        bottomSheetCallback = null
        bottomSheetBehavior = null
        super.onDestroyView()
        _binding = null
    }


}