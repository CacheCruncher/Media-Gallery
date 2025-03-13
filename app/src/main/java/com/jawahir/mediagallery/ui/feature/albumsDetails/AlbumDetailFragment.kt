package com.jawahir.mediagallery.ui.feature.albumsDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.databinding.FragmentAlbumDetailBinding
import com.jawahir.mediagallery.ui.adapter.AlbumAdapter
import com.jawahir.mediagallery.ui.uimodels.AlbumDetailUIModel
import com.jawahir.mediagallery.ui.uimodels.AlbumVisibilityUIModel
import com.jawahir.mediagallery.ui.uimodels.ContainerUIModel
import com.jawahir.mediagallery.ui.uimodels.MediaModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumDetailFragment : Fragment(R.layout.fragment_album_detail) {

    private val viewModel: AlbumDetailViewModel by viewModels()
    private val args: AlbumDetailFragmentArgs by navArgs()

    private val albumAdapter: AlbumAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AlbumAdapter(::onItemClick)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAlbumDetailBinding.bind(view).apply {
            mediaListRv.apply {
                adapter = albumAdapter
                layoutManager = layoutManager ?: GridLayoutManager(context, 2)
            }
        }
        observeViewModelState(binding)
    }

    private fun observeViewModelState(
        binding: FragmentAlbumDetailBinding
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { mediaResult ->
                    updateUI(mediaResult, binding)
                }
            }
        }
    }

    private fun updateUI(
        mediaResult: MediaResult<List<AlbumDetailUIModel>>,
        binding: FragmentAlbumDetailBinding
    ) {
        binding.uiModel = AlbumVisibilityUIModel(mediaResult)
        if (mediaResult is MediaResult.Success) {
            albumAdapter.submitList(mediaResult.data)
        }
    }

    fun onItemClick(uiModel: MediaModels) {
        val model = uiModel as AlbumDetailUIModel
        val action = when (uiModel.getContainerType()) {
            ContainerUIModel.IMAGE ->
                AlbumDetailFragmentDirections.actionAlbumDetailFragmentToImageViewerFragment(model)

            else ->
                AlbumDetailFragmentDirections.actionAlbumDetailFragmentToVideoPlayerFragment(model)
        }
        findNavController().navigate(action)
    }


    override fun onStart() {
        super.onStart()
        viewModel.onStart(args.albumUIModel.getId())
    }
}