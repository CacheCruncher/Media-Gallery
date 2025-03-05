package com.jawahir.mediagallery.ui.feature.albums

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jawahir.mediagallery.R
import com.jawahir.mediagallery.data.MediaResult
import com.jawahir.mediagallery.databinding.FragmentAlbumBinding
import com.jawahir.mediagallery.ui.adapter.AlbumAdapter
import com.jawahir.mediagallery.ui.uimodels.AlbumUIModel
import com.jawahir.mediagallery.ui.uimodels.AlbumVisibilityUIModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumFragment : Fragment(R.layout.fragment_album) {

    private val viewModel: AlbumViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAlbumBinding.bind(view)
        val albumAdapter = AlbumAdapter { mediaUIModel ->
            val action =
                AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(mediaUIModel as AlbumUIModel)
            findNavController().navigate(action)
        }

        binding.apply {
            albumListRv.apply {
                adapter = albumAdapter
                layoutManager = GridLayoutManager(context, 3)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { mediaResult ->
                    val result = mediaResult ?: return@collectLatest
                    when (result) {
                        is MediaResult.Loading -> {
                            binding.uiModel = AlbumVisibilityUIModel(result)
                        }

                        is MediaResult.Success -> {
                            binding.uiModel = AlbumVisibilityUIModel(result)
                            result.data?.let {
                                albumAdapter.submitList(it)
                            }
                        }

                        is MediaResult.Error -> {
                            binding.uiModel = AlbumVisibilityUIModel(result)
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
}