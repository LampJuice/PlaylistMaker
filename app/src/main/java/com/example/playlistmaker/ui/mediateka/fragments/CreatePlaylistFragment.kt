package com.example.playlistmaker.ui.mediateka.fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.ui.mediateka.view_model.CreatePlaylistViewModel
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.example.playlistmaker.ui.search.models.SongUi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment() {

    private val gson: Gson by inject()

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreatePlaylistViewModel by viewModel()
    private var isDataChanged = false

    private var song: SongUi? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.updateCover(it.toString()) }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        song = arguments?.getString(SearchFragment.Companion.EXTRA_TRACK)
            ?.let { gson.fromJson(it, SongUi::class.java) }

        initUi()
        observeViewModel()
        handleBackPress()
    }

    private fun initUi() = with(binding) {

        coverImg.setOnClickListener {
            pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        plName.addTextChangedListener { text ->
            viewModel.updateName(text.toString())
            isDataChanged = true

            nameTooltip.isVisible = !text.isNullOrEmpty()
        }

        plDesc.addTextChangedListener { text ->
            viewModel.updateDescription(text.toString())
            isDataChanged = true
            descTooltip.isVisible = !text.isNullOrEmpty()
        }

        btnCreatePl.setOnClickListener {
            viewModel.savePlaylist(song) { name ->
                Toast.makeText(
                    requireContext(),
                    getString(R.string.pl_created, name),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()

            }
        }

        backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun observeViewModel() {
        viewModel.statePlLiveData.observe(viewLifecycleOwner) { state ->

            binding.btnCreatePl.isEnabled = state.isCreateEnabled

            if (state.coverPath != null) {
                binding.cover.isVisible = false
                binding.coverImg.setImageURI(Uri.parse(state.coverPath))
            }
        }
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackPressed()
        }
    }

    private fun onBackPressed() {
        val state = viewModel.statePlLiveData.value
        val hasData = state?.name?.isNotBlank() == true ||
                state?.description?.isNotBlank() == true ||
                state?.coverPath != null

        if (!hasData) {
            findNavController().popBackStack()
        } else {
            showExitDialog()
        }
    }

    private fun showExitDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_exit_title)
            .setMessage(R.string.dialog_exit_message)
            .setNegativeButton(R.string.dialog_exit_cancel, null)
            .setPositiveButton(R.string.dialog_exit_confirm) { _, _ ->
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


}