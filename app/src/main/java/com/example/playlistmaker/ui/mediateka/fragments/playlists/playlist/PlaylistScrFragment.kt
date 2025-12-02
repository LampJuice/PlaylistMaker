package com.example.playlistmaker.ui.mediateka.fragments.playlists.playlist

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistScrBinding
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.ui.mediateka.fragments.playlists.PlaylistsFragment
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistScrState
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistScrViewModel
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.example.playlistmaker.ui.search.fragment.TrackAdapter
import com.example.playlistmaker.ui.search.models.SongUi
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistScrFragment : Fragment() {
    private var _binding: FragmentPlaylistScrBinding? = null
    private val binding get() = _binding!!

    private var bottomMenuCallback: BottomSheetBehavior.BottomSheetCallback? = null
    private var bottomMenu: BottomSheetBehavior<*>? = null

    private var bottomSongsListCallback: BottomSheetBehavior.BottomSheetCallback? = null
    private var bottomSongsList: BottomSheetBehavior<*>? = null

    private val viewModel: PlaylistScrViewModel by viewModel()

    private val gson: Gson by inject()

    private val adapter = TrackAdapter(mutableListOf(), { song ->
        findNavController().navigate(
            R.id.action_playlistScrFragment_to_playerFragment2, bundleOf(
                SearchFragment.Companion.EXTRA_TRACK to gson.toJson(song)
            )
        )
    }, { song ->
        showDeleteDialog(song)
    }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistScrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.songRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.songRecycler.adapter = adapter

        val playlistId = arguments?.getInt(PlaylistsFragment.Companion.EXTRA_PLAYLIST)

        viewModel.setPlaylist(playlistId ?: 0)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistScrState.Content -> render(state)
                PlaylistScrState.Error -> Unit
                PlaylistScrState.Loading -> Unit
            }
        }

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.shareBtn.setOnClickListener {
            val currentState =
                (viewModel.state.value as? PlaylistScrState.Content) ?: return@setOnClickListener

            if (currentState.song.isEmpty()) {
                Toast.makeText(
                    requireContext(), R.string.pl_is_empty,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.sharePlaylist(currentState.playlist, currentState.song)
            }
        }
        binding.bottomSheetShareBtn.setOnClickListener {
            val currentState =
                (viewModel.state.value as? PlaylistScrState.Content) ?: return@setOnClickListener
            bottomMenu?.state = BottomSheetBehavior.STATE_HIDDEN


            if (currentState.song.isEmpty()) {
                Toast.makeText(
                    requireContext(), R.string.pl_is_empty,
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                viewModel.sharePlaylist(currentState.playlist, currentState.song)

            }
        }
        binding.bottomSheetEditBtn.setOnClickListener {
            bottomMenu?.state = BottomSheetBehavior.STATE_HIDDEN
            val currentState =
                (viewModel.state.value as? PlaylistScrState.Content) ?: return@setOnClickListener
            val playlistJson = gson.toJson(currentState.playlist)
            val bundle = bundleOf(PlaylistsFragment.Companion.EXTRA_PLAYLIST to playlistJson)
            findNavController().navigate(
                R.id.action_playlistScrFragment_to_editPlaylistFragment,
                bundle
            )
        }
        binding.bottomSheetDeleteBtn.setOnClickListener {
            bottomMenu?.state = BottomSheetBehavior.STATE_HIDDEN
            val currentState =
                (viewModel.state.value as? PlaylistScrState.Content) ?: return@setOnClickListener
            showPlaylistDeleteDialog(currentState.playlist)

        }

        bottomMenu = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSongsList = BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        bottomMenuCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, p1: Int) {
                binding.overlay.visibility =
                    if (p1 == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
            }

            override fun onSlide(p0: View, p1: Float) {
                binding.overlay.alpha = ((p1 + 1) / 2).coerceIn(0f, 1f)
            }
        }

        bottomSongsListCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, p1: Int) {
                if (p1 == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
            }

            override fun onSlide(p0: View, p1: Float) {
            }
        }

        bottomMenu?.addBottomSheetCallback(bottomMenuCallback!!)
        binding.overlay.setOnClickListener {
            bottomMenu?.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.menuBtn.setOnClickListener {
            bottomMenu?.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun render(state: PlaylistScrState.Content) = with(binding) {
        plName.text = state.playlist.name
        plDesc.text = state.playlist.description
        plDesc.visibility =
            if (state.playlist.description?.isBlank() ?: true) View.GONE else View.VISIBLE

        included.name.text = state.playlist.name
        included.songs.text =
            resources.getQuantityString(R.plurals.tracks_count, state.trackCount, state.trackCount)
        state.playlist.coverPath?.let { path ->
            Glide.with(this@PlaylistScrFragment)
                .load(path)
                .placeholder(R.drawable.placeholder_45)
                .error(R.drawable.placeholder_45)
                .into(plCover)

            Glide.with(this@PlaylistScrFragment)
                .load(path)
                .placeholder(R.drawable.placeholder_45)
                .error(R.drawable.placeholder_45)
                .into(included.cover)
        } ?: run {
            plCover.setImageResource(R.drawable.placeholder_45)
            included.cover.setImageResource(R.drawable.placeholder_45)
        }

        summTime.text = resources.getQuantityString(
            R.plurals.minutes_count,
            state.duration.toInt(),
            state.duration.toInt()
        )
        songCount.text =
            resources.getQuantityString(R.plurals.tracks_count, state.trackCount, state.trackCount)

        if (state.song.isEmpty()) {
            songRecycler.visibility = View.GONE
            plPlaceholder.visibility = View.VISIBLE
        } else {
            songRecycler.visibility = View.VISIBLE
            plPlaceholder.visibility = View.GONE
            adapter.updateData(state.song)
        }
    }

    private fun showDeleteDialog(song: SongUi) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.pl_delete_title)
            .setNegativeButton(R.string.pl_delete_negative, null)
            .setPositiveButton(R.string.pl_delete_positive) { _, _ ->
                viewModel.removeSongFromPlaylist(song.trackId)
            }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                ?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_b_s_pl))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                ?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_b_s_pl))
        }
        dialog.show()
    }

    private fun showPlaylistDeleteDialog(playlist: Playlist) {
        val message = getString(R.string.pl_delete_playlist_msg)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.pl_delete_playlist_title)
            .setMessage(message)
            .setNegativeButton(R.string.dialog_exit_cancel, null)
            .setPositiveButton(R.string.dialog_delete) { _, _ ->
                viewModel.deletePlaylist()
                findNavController().popBackStack()
            }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                ?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_b_s_pl))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                ?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_b_s_pl))
        }
        dialog.show()
    }

    override fun onDestroy() {
        bottomMenuCallback?.let { callback ->
            bottomMenu?.removeBottomSheetCallback(callback)
        }
        bottomMenu = null
        bottomMenuCallback = null

        bottomSongsListCallback?.let { callback ->
            bottomSongsList?.removeBottomSheetCallback(callback)
        }
        bottomSongsList = null
        bottomSongsListCallback = null

        super.onDestroy()
        _binding = null
    }

}